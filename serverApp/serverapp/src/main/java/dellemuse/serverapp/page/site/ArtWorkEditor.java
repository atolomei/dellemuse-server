package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.NumberFormatter;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.form.Form;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.BooleanField;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import jakarta.transaction.Transactional;
import wktui.base.ModelPanel;

public class ArtWorkEditor extends DBModelPanel<ArtWork> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkEditor.class.getName());
		
	private Form<Void> form;
	
	private TextField<String> nameField;
    private TextAreaField<String> introField;
    private TextAreaField<String> infoField;
    private  BooleanField useThumbnailField;
 	private IModel<Resource> photoModel;
	private  FileUploadSimpleField<Resource> photoField;
	private boolean uploadedPhoto = false;
	
	/**
	 * @param id
	 * @param model
	 */
	public ArtWorkEditor(String id, IModel<ArtWork> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		Optional<ArtWork> o_i = getArtWorkDBService().findByIdWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtWork>(o_i.get()));
		
		if (getModel().getObject().getPhoto()!=null) {
			Optional<Resource> o_r = getResourceDBService().findByIdWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}
	 	
	    this.form = new Form<Void>("form");
        add(this.form);
        
        nameField 			= new TextField<String>("name", 		new PropertyModel<String>(getModel(), "name"),  getLabel("name"));
        infoField  			= new TextAreaField<String>("info", 	new PropertyModel<String>(getModel(), "info"),  getLabel("info"), 8);
        introField 			= new TextAreaField<String>("intro", 	new PropertyModel<String>(getModel(), "intro"),  getLabel("intro"), 8);
        useThumbnailField 	= new BooleanField("useThumbnail", 	    new PropertyModel<Boolean>(getModel(), "info"), getLabel("useThumbnail"));

        
        // artists
        //
        photoField 			= new FileUploadSimpleField<Resource>("photo", getPhotoModel(), getLabel("photo")) {

        	private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return  ArtWorkEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {
				if (getPhotoModel()==null)
					return null;
				return ArtWorkEditor.this.getThumbnail(getPhotoModel().getObject());
			}

			public String getFileName() {
				return ArtWorkEditor.this.getPhotoFileName();
			}

			public boolean isThumbnail() {
				return true;
			}
		};
		
        form.add( nameField  );
        form.add( infoField  );
        form.add( introField  );
        form.add( useThumbnailField );
        form.add( photoField  );

        
        SubmitButton<Void> submit = new SubmitButton<Void>("submit") {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
            	ArtWorkEditor.this.onSubmit();
            }
            
            @Override
            public IModel<String> getLabel() {
            	return new StringResourceModel("submit");
            			
            }
        };
        
        form.add(submit);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		
		if (photoModel!=null)
			photoModel.detach();
	}
	

	protected IModel<Resource> getPhotoModel() {
		return this.photoModel;
	}
	protected void setPhotoModel(ObjectModel<Resource> model) {
			this.photoModel=model;
	}
	
	protected String getPhotoFileName() {
		if (getPhotoModel() == null)
			return null;
		return getPhotoModel().getObject().getDisplayname() + (getPhotoModel().getObject().getSize() != 0
				? " ( " +   NumberFormatter.formatFileSize(getPhotoModel().getObject().getSize()) + " )"
				: "");
	}
	
	protected void onSubmit() {
	        logger.debug("");
		    logger.debug("onSubmit");
	        logger.debug("");
	}
	 
	protected boolean processPhotoUpload(List<FileUpload> uploads) {

			if (this.uploadedPhoto)
				return false;

			if (uploads != null && !uploads.isEmpty()) {
				for (FileUpload upload : uploads) {
					try {
						
						logger.debug("name -> " + upload.getClientFileName());
						logger.debug("Size -> " + upload.getSize());

						String bucketName = ServerConstant.MEDIA_BUCKET;
						String objectName = getResourceDBService()
								.normalizeFileName(FileNameUtils.getBaseName(upload.getClientFileName())) + "-"
								+ String.valueOf(getResourceDBService().newId());

						Resource resource = uploadFile(upload.getInputStream(), bucketName, objectName, upload.getClientFileName(), upload.getSize());
						
						setPhotoModel(new ObjectModel<Resource>(resource));
						getModel().getObject().setPhoto(resource);

						uploadedPhoto=true;
				
					} catch (Exception e) {
						uploadedPhoto = false;
						error("Error saving file: " + e.getMessage());
					}
				}
			} else {
				info("No file uploaded.");
				logger.debug("No file uploaded.");
			}

			return uploadedPhoto;
		}
	
}
