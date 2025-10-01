package dellemuse.serverapp.global;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.Resource;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.menu.DropDownMenu;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.menu.SeparatorMenuItem;
import wktui.base.BasePanel;
import wktui.base.DummyBlockPanel;
import wktui.base.InvisiblePanel;
import wktui.base.LabelPanel;
import wktui.base.ModelPanel;


public class JumboPageHeaderPanel<T> extends DBModelPanel<T> {

	private static final long serialVersionUID = 1L;

	private IModel<String> title;
	private IModel<String> tagLine;// = Model.of("En el barrio de San Telmo, el museo alberga más de 7000 obras de arte argentino e internacional.");
							
	private IModel<String> context;
	
	private Image image;
	private WebMarkupContainer imageContainer;
	private Link<Resource> imageLink;
	private IModel<Resource> photo;
	private WebMarkupContainer frame = new WebMarkupContainer("frame");

	private WebMarkupContainer contextContainer;

	
	private boolean imageAdded = false;

	private boolean photoVisible = true;
	
	
	public JumboPageHeaderPanel(String id) {
		this(id, null, null);
	}

	public JumboPageHeaderPanel(String id, IModel<T> model) {
	       this(id, model, null);
	}

	public JumboPageHeaderPanel(String id, IModel<T> model, IModel<String> title) {
		super(id, model);
		this.title=title; 
		setOutputMarkupId(true); 
	}


	@Override
	public void onDetach() {
		super.onDetach();
		
		if (getPhotoModel()!=null)
		 getPhotoModel().detach();
	
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		Label title = new Label("title", getTitle());
		
		frame.add(title);
		add(frame);
		
		addImageAndInfo();
		
		frame.add(new org.apache.wicket.AttributeModifier("class", getCss()));
		
		 if (getTagline()!=null) {
	            WebMarkupContainer taglineContainer =  new WebMarkupContainer("taglineContainer");
	            taglineContainer.add((new Label("tagline", getTagline())).setEscapeModelStrings(false));
	            frame.addOrReplace(taglineContainer);
	            
	     }
	     else {
	       	frame.addOrReplace( new InvisiblePanel("taglineContainer"));
	    }
		 if (getContext()!=null) {
	            WebMarkupContainer taglineContainer =  new WebMarkupContainer("contextContainer");
	            taglineContainer.add((new Label("context", getContext())).setEscapeModelStrings(false));
	            frame.addOrReplace(taglineContainer);
	            
	     }
	     else {
	       	frame.addOrReplace( new InvisiblePanel("contextContainer"));
	    }
		 
		 
	}

	
    public void setContext(IModel<String> context) {
		this.context=context;
	}

    
    public IModel<String> getContext() {
		return this.context;
	}

	public void setPhotoVisible(boolean b) {
    	this.photoVisible=b;
    }

	@Override
    public void onBeforeRender() {
        super.onBeforeRender();
    
        if (frame.get("breadcrumb")==null) {
        	frame.addOrReplace(new InvisiblePanel("breadcrumb"));
        }
        
        
       // if (!imageAdded) {
       // 	addImageAndInfo();
	//		frame.add(new org.apache.wicket.AttributeModifier("class", getCss()));
      //  }
    }
    
   
    
	
    public void setBreadCrumb(Panel bc) {
        if (!bc.getId().equals("breadcrumb"))
            throw new IllegalArgumentException(" id must be breadcrumb -> " + bc.getId());
        frame.addOrReplace(bc);
    }
	
	
    public IModel<String> getTagline() {
        return this.tagLine;
    }
	
    public void setTagline(IModel<String> tag) {
        this.tagLine=tag;
    }

    public IModel<Resource> getPhotoModel() {
    	return this.photo;
    }

    public void  setPhotoModel(IModel<Resource> p) {
    	this.photo=p;
    }


    protected String getCss() {
    	return ((this.image!=null&&this.image.isVisible())?"mb-0 pb-4 border-bottom":"mb-0 pb-2 border-bottom");
	}

    
	
    private void addImageAndInfo() {

		imageAdded = true;
	
		this.imageContainer = new WebMarkupContainer("imageContainer") {
		 	private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				if (getPhotoModel() == null)
					return false;
				return isPhotoVisible();
			}
		};

		this.imageContainer.setOutputMarkupId(true);
		
		frame.addOrReplace(this.imageContainer);
		this.imageLink = new Link<Resource>("image-link", null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				// logger.debug("on click");
			}
		};
		
		
		if (imgCss!=null)
			this.imageLink.add( new AttributeModifier("class", imgCss));
		
		this.imageContainer.add(this.imageLink);
		
	 
		String presignedThumbnail = null;
		
		if (getPhotoModel()!=null && getPhotoModel().getObject() !=null) 
			presignedThumbnail = getPresignedThumbnail(getPhotoModel().getObject(), ThumbnailSize.W980) ;
			if (presignedThumbnail != null) {
				Url url = Url.parse(presignedThumbnail);
				UrlResourceReference resourceReference = new UrlResourceReference(url);
				this.image = new Image("image", resourceReference);
				this.imageLink.addOrReplace(this.image);
			} else {
				this.image = new Image("image", new UrlResourceReference(Url.parse("")));
				this.image.setVisible(false);
				this.imageLink.addOrReplace(image);
			}
		
				
			
	}
	
    
    
        
    
    protected boolean isPhotoVisible() {
		return photoVisible;
	}

	private IModel<String> getTitle() {
    
    	if (this.title!=null)
    		return this.title;
    	
    	if (getModel()!=null) {
    		if (getModel().getObject() instanceof DelleMuseObject) {
    			return new Model<String>( ((DelleMuseObject) getModel().getObject()).getDisplayname() );
    		}
    	}
    	return new Model<String>( getClass().getSimpleName());  
      
    }

	String imgCss = "jumbo-img jumbo-md mb-2 mb-lg-0 border bg-body-tertiary";
	
	public void setImageLinkCss(String css) {
		
		imgCss = css;
		
		if (this.imageLink!=null)
			this.imageLink.add( new AttributeModifier("class", imgCss));
	
	}
}
