package dellemuse.serverapp.help;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;

import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

public class HelpDropDownMenu extends DropDownMenuToolbarItem<User> {

	private static final long serialVersionUID = 1L;

	public HelpDropDownMenu(String id, IModel<User> model) {
		super(id, model);
		setIconCss( Icons.help );
		// "fa-sharp fa-light fa-circle-question"
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		 addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new TitleMenuItem<User>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return HelpDropDownMenu.this.getLabel("tutorials");
					}
				};
			}
		});
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new LinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						 setResponsePage(new RedirectPage( getLabel("link-concepts").getObject() ));
					}

					@Override
					public IModel<String> getLabel() {
					 return HelpDropDownMenu.this.getLabel("concepts");
					}
					
					 @Override
					public String getTarget() {
						return "_blank";
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new  LinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						 setResponsePage(new RedirectPage( getLabel("link-exhibitions").getObject() ));
						// setResponsePage(new RedirectPage("https://youtu.be/OyXFbTFCe4k?si=Qu-_hJ53Y0ucKh7a"));
					}

					@Override
					public IModel<String> getLabel() {
						  return HelpDropDownMenu.this.getLabel("exhibitions-tutorials");
					}

					@Override
					public String getTarget() {
						return "_blank";
					}
				};
			}
		});
		
	 
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new  LinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						 setResponsePage(new RedirectPage( getLabel("link-audioguides-tutorials").getObject() ));
						// setResponsePage(new RedirectPage("https://youtu.be/OyXFbTFCe4k?si=Qu-_hJ53Y0ucKh7a"));
					}

					@Override
					public IModel<String> getLabel() {
						  return HelpDropDownMenu.this.getLabel("create-audio-guide");
					}

					@Override
					public String getTarget() {
						return "_blank";
					}
				};
			}
		});

		
		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new  LinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						 setResponsePage(new RedirectPage( getLabel("link-use-audio-studio").getObject() ));
						// setResponsePage(new RedirectPage("https://youtu.be/OyXFbTFCe4k?si=Qu-_hJ53Y0ucKh7a"));
					}

					@Override
					public IModel<String> getLabel() {
						  return HelpDropDownMenu.this.getLabel("use-audio-studio");
					}

					@Override
					public String getTarget() {
						return "_blank";
					}
				};
			}
		});
		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new  LinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						 setResponsePage(new RedirectPage( getLabel("link-publish-public-portal").getObject() ));
						// setResponsePage(new RedirectPage("https://youtu.be/OyXFbTFCe4k?si=Qu-_hJ53Y0ucKh7a"));
					}

					@Override
					public IModel<String> getLabel() {
						  return HelpDropDownMenu.this.getLabel("publish-public-portal");
					}

					@Override
					public String getTarget() {
						return "_blank";
					}
				};
			}
		});

		
		
		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new  LinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						 setResponsePage(new RedirectPage( getLabel("link-create-public-portal").getObject() ));
						// setResponsePage(new RedirectPage("https://youtu.be/OyXFbTFCe4k?si=Qu-_hJ53Y0ucKh7a"));
					}

					@Override
					public IModel<String> getLabel() {
						  return HelpDropDownMenu.this.getLabel("create-public-portal");
					}

					@Override
					public String getTarget() {
						return "_blank";
					}
				};
			}
		});
		
		/**
		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<User>(id);
			}
		});
		 **/	
	}
}
