package dellemuse.serverapp.global;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.serverdb.model.User;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import wktui.base.InvisiblePanel;
import wktui.base.LabelLinkPanel;
import wktui.base.ModelPanel;


public class GlobalTopPanel extends ModelPanel<User> {

	private static final long serialVersionUID = 1L;

	
	private boolean isSearch = false;
	private String srcUrl;
	
	private Panel userGlobalTopPanel;
	private Panel languagePanel;
	
	
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

		LabelLinkPanel logo = new LabelLinkPanel("item", getLabel( "dellemuselogo")) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onClick() {
                setResponsePage(new RedirectPage("/home"));
            }
		};
		
		nav.addNoCollapseLeft(logo);
		
		logo.setLinkStyle("text-decoration: none;");
		logo.setStyle("font-size: 0.65em; font-weight: 500; color: orange; text-transform:uppercase; font-size: 0.65em; font-style: normal;");
		
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
        
        
        this.languagePanel = new LanguagePanel("languagePanel");
        add(this.languagePanel);
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
