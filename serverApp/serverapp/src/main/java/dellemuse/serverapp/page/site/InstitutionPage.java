package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.GuideContentModel;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ErrorPage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
 
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

/**
 * site foto Info - exhibitions
 */

@MountPath("/institution/${id}")
public class InstitutionPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionPage.class.getName());

	private StringValue stringValue;
	private IModel<Institution> institutionModel;

	private Image image;
	private WebMarkupContainer imageContainer;
	private Link<Resource> imageLink;
	private Link<Site> addSite;
	

	private  List<IModel<Site>> siteList;

	private List<IModel<Institution>> institutionList;
	private int current = 0;
	private WebMarkupContainer navigatorContainer;
	
	public InstitutionPage() {
		super();
	}

	public InstitutionPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}

	public InstitutionPage(IModel<Institution> model, List<IModel<Institution>> list) {
		setInstitutionModel(model);
		this.setInstitutionList(list);
		getPageParameters().add("id", model.getObject().getId().toString());
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		if (getInstitutionModel() == null) {
			if (stringValue != null) {
				Optional<Institution> o_site = getInstitution(Long.valueOf(stringValue.toLong()));
				if (o_site.isPresent()) {
					setInstitutionModel(new ObjectModel<Institution>(o_site.get()));
				}
			}
		}
		
		if (getInstitutionModel() == null) {
			setResponsePage(new ErrorPage(new RuntimeException("no " + Institution.class.getSimpleName())));
		}
		
		if (!getInstitutionModel().getObject().isDependencies()) {
			Optional<Institution> o_i = getInstitutionDBService().findByIdWithDeps(getInstitutionModel().getObject().getId());
			setInstitutionModel( new ObjectModel<Institution>(o_i.get()));
		}
		
		
		this.siteList = new ArrayList<IModel<Site>>();
		getSites(getInstitutionModel().getObject()).forEach(s -> siteList.add(new ObjectModel<Site>(s)));
		setCurrent();
		
		
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/institution/list", getLabel("institutions")));
		bc.addElement(new BCElement(new Model<String>(getInstitutionModel().getObject().getDisplayname())));
	
		
		
		if (getInstitutionList()!=null && getInstitutionList().size()>0) {
			Navigator<Institution> nav = new Navigator<Institution>("navigator", getCurrent(), getInstitutionList()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void navigate(int current) {
					setResponsePage( new InstitutionPage( getList().get(current), getList() ));
				}
			};
			bc.setNavigator(nav);
		}
		
		
		PageHeaderPanel<Institution> ph = new PageHeaderPanel<Institution>("page-header", getInstitutionModel(),
				new Model<String>(getInstitutionModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);
		add(ph);
		add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

		add(new InstitutionEditor("institutionEditor", getInstitutionModel()));
		

        
        ListPanel<Site> panel = new ListPanel<>("sites", getSiteList()) {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected Panel getListItemPanel(IModel<Site> model, ListPanelMode mode) {

                ObjectListItemPanel<Site> panel = new ObjectListItemPanel<>("row-element", model, mode) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                    	setResponsePage( new SitePage( getModel(), getSiteList() ) );
                    }
                    
                    protected IModel<String> getInfo() {
                        String str = TextCleaner.clean(model.getObject().getInfo(), 280);
                        return new Model<String>(str);
                    }
                    
                    @Override
                    protected String getImageSrc() {
                    	if ( getModel().getObject().getPhoto()!=null) {
                    		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
                    	    return getPresignedThumbnailSmall(photo);
                        }
                        return null;
                    }
                };
                return panel;
            }
            
            
            protected void onClick(IModel<Site> model) {
             	setResponsePage( new SitePage( model, getSiteList() ) );
                
            }
         
            
            @Override
            public IModel<String> getItemLabel(IModel<Site> model) {
                return new Model<String>(model.getObject().getDisplayname());
            }
            
            @Override
            protected List<IModel<Site>> filter(List<IModel<Site>> initialList, String filter) {
            	
            	List<IModel<Site>> list = new ArrayList<IModel<Site>>();

            	final String str = filter.trim().toLowerCase();

            	initialList.forEach(
                		s -> {
                if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
                	 list.add(s);
                	}
               }
               );
               return list; 
            }
            
        };
        add(panel);
        
        panel.setTitle(getLabel("list"));
        panel.setListPanelMode(ListPanelMode.TITLE);
		panel.setLiveSearch(false);
		panel.setSettings(true);
		
		
		addNavigator();
		
		this.addSite = new Link<Site>("addSite", null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				InstitutionPage.this.addSite();
			}
		};
		
		add(this.addSite);
		addImageAndInfo();
	}

	
	private void addNavigator() {
		this.navigatorContainer = new WebMarkupContainer("navigatorContainer");
		add(this.navigatorContainer);
		
		this.navigatorContainer.setVisible(getInstitutionList()!=null && getInstitutionList().size()>0);

		if (getInstitutionList() != null) {
			ListNavigator<Institution> nav = new ListNavigator<Institution>("navigator", this.current,
					getInstitutionList()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<String> getLabel(IModel<Institution> model) {
					return new Model<String>(model.getObject().getDisplayname());
				}

				@Override
				protected void navigate(int current) {
					setResponsePage(new InstitutionPage(getInstitutionList().get(current), getList()));
				}
			};
			this.navigatorContainer.add(nav);
		} else {
			this.navigatorContainer.add(new InvisiblePanel("navigator"));
		}
	}
	
	protected void addSite() {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		Site site;
		if (getSiteList().size()==0)
			site = service.create( getInstitutionModel().getObject(), getUserDBService().findRoot());
		else
			site = service.create( getInstitutionModel().getObject().getName() + " " + String.valueOf(getSiteList().size() ), getUserDBService().findRoot() );

		IModel<Site> m=new ObjectModel<Site>(site);

		getSiteList().add(m);
		
		setResponsePage(new SitePage(m, getSiteList()));
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (institutionModel != null)
			institutionModel.detach();
		
		if (siteList!=null)
			siteList.forEach(i-> i.detach());
		
		
		if (this.institutionList!=null)
			this.institutionList.forEach(i-> i.detach());
		
	}

	public IModel<Institution> getInstitutionModel() {
		return institutionModel;
	}

	public void setInstitutionModel(IModel<Institution> InstitutionModel) {
		this.institutionModel = InstitutionModel;
	}

	public List<IModel<Institution>> getInstitutionList() {
		return institutionList;
	}

	public void setInstitutionList(List<IModel<Institution>> institutionList) {
		this.institutionList = institutionList;
	}
	

	protected List<IModel<Site>> getSiteList() {
		return this.siteList;
	}

	private int getCurrent() {
		return this.current;
	}	
	
	private void setCurrent() {
		
		if (this.institutionList==null)
			return;
		
		if (getInstitutionModel()==null)
			return;
		
		int n = 0;
		for (IModel<Institution> m : this.institutionList) {
			if ( getInstitutionModel().getObject().getId().equals(m.getObject().getId())) {
				current = n;
				break;
			}
			n++;
		}
	}
	
	
	private void addImageAndInfo() {

		this.imageContainer = new WebMarkupContainer("imageContainer");
		this.imageContainer.setVisible(getInstitutionModel().getObject().getPhoto() != null);
		addOrReplace(this.imageContainer);
		this.imageLink = new Link<Resource>("image-link", null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				logger.debug("on click");
			}
		};
		
		this.imageContainer.add(this.imageLink);
		
		Label info = new Label("info", TextCleaner.clean(getInstitutionModel().getObject().getInfo()));
		info.setEscapeModelStrings(false);
		this.imageContainer.add(info);
		
		String presignedThumbnail = null;
		
		if (getInstitutionModel().getObject().getPhoto()!=null) 
			presignedThumbnail = getPresignedThumbnailSmall(getInstitutionModel().getObject().getPhoto());
		
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

	
}
