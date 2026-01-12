package dellemuse.serverapp.global;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseServer;
import dellemuse.serverapp.branded.panel.BrandedArtExhibitionGuidePanel;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.media.InvisibleImage;
import io.wktui.nav.menu.NavBar;
import wktui.base.InvisiblePanel;
import wktui.base.LabelLinkPanel;
import wktui.base.ModelPanel;


public class GlobalTopPanel extends ModelPanel<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(GlobalTopPanel.class.getName());
	
	private boolean isSearch = false;
	private String srcUrl;
	
	private Panel userGlobalTopPanel;
	//private Panel languagePanel;
	
	
	public GlobalTopPanel(String id) {
		this(id, null,  null);
	}
	
	
	public GlobalTopPanel(String id, IModel<User> model) {
		super(id, model);
	}
	
	
	public GlobalTopPanel(String id,  IModel<User> userModel, String srcUrl) {
		super(id, userModel);
		this.srcUrl=srcUrl;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		NavBar<Void> nav = new NavBar<Void>("navbarLeft");

	 	LabelLinkPanel logo = new LabelLinkPanel("item") {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onClick() {
                setResponsePage(new RedirectPage("/home"));
            }
		};
		
		logo.setLabel(Model.of("dellemuse"));
		logo.setSubtitle(getLabel("dellemuse-tagline"));
		logo.setSubtitleCss("subtitle d-none d-lg-inline d-xl-inline d-xxl-inline");
		
		//String presignedThumbnail = 
		
		
		//Url url = Url.parse(presignedThumbnail);
		//String p = "/Users/alejandrotolomei/eclipse-workspace/dellemuse-server/serverApp/serverapp/src/main/java/dellemuse/serverapp/dellemuse-logo-blanco.png";
		//path = Paths.get(p);
		
		
		//Path path;
		// try {
			 
			
			/**  
			Image image = new Image(
				    "image",
				    new org.apache.wicket.request.resource.PackageResourceReference(
				        DellemuseServer.class,
				        "dellemuse-logo-blanco.png"
				    )
				);
			 **/
			 
			//logo.setImage(image);
			// addOrReplace(image);
			
			 //logo.setIconCss("fa-duotone fa-solid fa-club");
			
		//} catch (Exception e) {
	//		logger.error(e);
			//addOrReplace ( new InvisibleImage( "image"));
	//	}
		
		nav.addNoCollapseLeft(logo);
		
		// 
		
		logo.setLinkStyle("text-decoration: none;");
		//logo.setStyle(" margin-bottom:-2px; font-weight: 600; color: #eeeeef;  font-size: 0.65em; font-style: normal;");
		
		add(nav);

		Link<Void> find = new Link<Void>("find") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                    //setResponsePage(new SiteSearchArtWorkPage(getSiteModel()));   
            }
            public boolean isVisible() {
            	return isSearch();
            }
        };
        add(find);

        
		Link<Void> sup = new Link<Void>("signup") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                    //setResponsePage( new DellemuseWebSignupPage());   
            }
        };
        
        sup.setVisible(false);
        add(sup);
        
        
        Link<Void> si = new Link<Void>("signin") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick() {
                   //setResponsePage( new DellemuseWebSigninPage());
            }
        };
        
        si.setVisible(false);
        add(si);
        
        if (getModel()!=null) {
        	this.userGlobalTopPanel = new UserGlobalTopPanel("userGlobalTopPanel", getModel());
       }
        else {
        	this.userGlobalTopPanel = new InvisiblePanel("userGlobalTopPanel");
       }
	   
        add(this.userGlobalTopPanel);
        
        
        //this.languagePanel = new LanguagePanel("languagePanel");
        //add(this.languagePanel);
   }
	
/**
 * 
 * @return
 	private  NavDropDownMenu<Void> getSitesMenu() {
			
			NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("item", null, new Model<String>("Instituciones"));
			
			
			
			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Void> getItem(String id) {

					return new  LinkMenuItem<Void>(id) {

						private static final long serialVersionUID = 1L;
						
						@Override
						public void onClick() {
							setResponsePage(new RedirectPage("/sites"));
						}

						@Override
						public IModel<String> getLabel() {
							return new Model<String>("Instituciones");
						}

						@Override
						public String getBeforeClick() {
							return null;
						}
					};
				}
			});

		return menu;
	}
	
	private  NavDropDownMenu<Void> getAboutMenu() {
	
	    NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("item", null, new Model<String>("Acerca"));
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("https://odilon.io/dellemuse"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Acerca");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});


		
		return menu;
	}
	
	private  NavDropDownMenu<Void> getContactMenu() {

		NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("item", null, new Model<String>("Contacto"));
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick() {
					    setResponsePage(new RedirectPage("https://odilon.io/dellemuse"));
					}

					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Contáctenos");
					}

					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		return menu;
	}
*/
	
	public boolean isSearch() {
		return isSearch;
	}


	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}
	
	
	
	

}
