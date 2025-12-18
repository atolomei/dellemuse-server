package dellemuse.serverapp.branded.panel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefResourceModel;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artexhibitionitem.ArtExhibitionItemPage;
import dellemuse.serverapp.branded.BrandedGuideContentPage;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionItemDBService;
import dellemuse.serverapp.serverdb.service.GuideContentDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.error.AlertPanel;
import io.wktui.error.ErrorPanel;
import io.wktui.event.UIEvent;
import io.wktui.form.FormState;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import io.wktui.text.ExpandableReadPanel;
import wktui.base.DummyBlockPanel;
import wktui.base.InvisiblePanel;

public class BrandedGuideContentPanel extends DBModelPanel<GuideContent> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedGuideContentPanel.class.getName());

	 
	
	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private IModel<ArtWork> artWorkModel;
	private IModel<ArtExhibitionItem> artExhibitionItemModel;
	private IModel<ArtExhibitionGuide> artExhibitionGuideModel;
	 
 
 
	 
	private List<ToolbarItem> listToolbar = null;
	private List<ToolbarItem> t_list = null;

	private WebMarkupContainer infoContainer;
	 
	
	public BrandedGuideContentPanel(String id, IModel<GuideContent> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel = siteModel;
		setOutputMarkupId(true);

	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		
		infoContainer = new WebMarkupContainer("infoContainer");
		add(infoContainer);
		infoContainer.add( new InvisiblePanel("error"));
		
		setUpModel();
		addAudio();
		addInfo();
	
	}
	
	 
	protected void addAudio() {
	 	
		WebMarkupContainer audioContainer = new WebMarkupContainer("audioContainer");
		infoContainer.addOrReplace(audioContainer);
		
		try {
			
			if (getModel().getObject().getAudio()!=null) {
			    WebMarkupContainer audioIntroContainer = new WebMarkupContainer("intro-audio");
			    audioContainer.add(audioIntroContainer);
		        String as =  getPresignedUrl(getModel().getObject().getAudio());
		        Url url = Url.parse(as);
	            UrlResourceReference resourceReference = new UrlResourceReference(url);
		        Audio audio = new Audio("audio", resourceReference);
		        audioIntroContainer.add(audio);
		        
			}
			else {
			    audioContainer.addOrReplace(new InvisiblePanel("intro-audio"));
		        infoContainer.addOrReplace( new AlertPanel<Void>("error", AlertPanel.WARNING, Model.of("no audio file") ));
		        audioContainer.setVisible(false);
			}

			
		} catch (Exception e) {
			logger.error(e);
			infoContainer.addOrReplace( new Label("intro", ""));
			audioContainer.addOrReplace(new InvisiblePanel("audio"));
			audioContainer.setVisible(false);
			infoContainer.addOrReplace(new ErrorPanel("error", e));
		}
	}
 

	protected void addInfo() {

		if (getModel().getObject().getInfo()!=null) {
			IModel<String> m = Model.of( TextCleaner.clean(getModel().getObject().getInfo()));
			WebMarkupContainer descContainer = new WebMarkupContainer("textContainer");
			infoContainer.addOrReplace(descContainer);
	        ExpandableReadPanel desc = new ExpandableReadPanel("info", m);
	        descContainer.add(desc);
		}
		else {
			infoContainer.addOrReplace(new InvisiblePanel("textContainer"));
		}
	}
	
	
	protected void addListeners() {
		super.addListeners();
	}

	@Override
	public void onDetach() {
		super.onDetach();
 
		if (siteModel != null)
			siteModel.detach();

		if (artExhibitionGuideModel != null)
			artExhibitionGuideModel.detach();

		if (artExhibitionModel != null)
			artExhibitionModel.detach();

		if (artExhibitionItemModel != null)
			artExhibitionItemModel.detach();

		if (artWorkModel != null)
			artWorkModel.detach();
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return null;
		//return t_list;
	}

	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> artExhibitionModel) {
		this.artExhibitionModel = artExhibitionModel;
	}
   
	protected List<ToolbarItem> getListToolbarItems() {

		return null;
		/**
		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(ObjectStateEnumSelector.ALL.getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);
		listToolbar.add(s);
		return listToolbar;
*/
	}

 
   
	protected boolean isAudio(IModel<GuideContent> model) {
		return model.getObject().getAudio() != null;
	}

	
	
	

	
	
	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
	public void setArtExhibitionItemModel(IModel<ArtExhibitionItem> artExhibitionItemModel) {
		this.artExhibitionItemModel = artExhibitionItemModel;
	}
	
	public IModel<ArtExhibitionItem> getArtExhibitionItemModel() {
		return artExhibitionItemModel;
	}
	public IModel<ArtExhibitionGuide> getArtExhibitionGuideModel() {
		return artExhibitionGuideModel;
	}

	public void setArtExhibitionGuideModel(IModel<ArtExhibitionGuide> artExhibitionGuideModel) {
		this.artExhibitionGuideModel = artExhibitionGuideModel;
	}

	public IModel<ArtWork> getArtWorkModel() {
		return artWorkModel;
	}

	public void setArtWorkModel(IModel<ArtWork> artWorkModel) {
		this.artWorkModel = artWorkModel;
	}

	private void setUpModel() {

		
		if (!getModel().getObject().isDependencies()) {
			Optional<GuideContent> o_i = getGuideContentDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<GuideContent>(o_i.get()));
		}
		
		ArtExhibitionItem item = getModel().getObject().getArtExhibitionItem();
		Optional<ArtExhibitionItem> o_i = getArtExhibitionItemDBService().findWithDeps(item.getId());
		this.setArtExhibitionItemModel(new ObjectModel<ArtExhibitionItem>(o_i.get()));

		ArtWork aw = getArtWorkDBService().findWithDeps(o_i.get().getArtWork().getId()).get();
		setArtWorkModel(new ObjectModel<ArtWork>(aw));

		ArtExhibitionGuide guide = getModel().getObject().getArtExhibitionGuide();
		Optional<ArtExhibitionGuide> o_g = getArtExhibitionGuideDBService().findWithDeps(guide.getId());
		this.setArtExhibitionGuideModel(new ObjectModel<ArtExhibitionGuide>(o_g.get()));

		ArtExhibition a = item.getArtExhibition();

		Optional<ArtExhibition> o_a = getArtExhibitionDBService().findWithDeps(a.getId());
		this.setArtExhibitionModel(new ObjectModel<ArtExhibition>(o_a.get()));
		
		Optional<Site> o_s = getSiteDBService().findWithDeps(getArtExhibitionModel().getObject().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));
	}
	
}
