package dellemuse.serverapp.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import io.wktui.event.UIEvent;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import io.wktui.struct.list.ListPanelWicketEvent;
 
import wktui.base.InvisiblePanel;
import wktui.base.ModelPanel;

public class ObjectListItemPanel<T extends DelleMuseObject> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;

	private IModel<String> subtitle;
	private String icon;
	private Link<T> imageLink;
	private Image image;
	private ListPanelMode mode;
	private boolean imageVisible = true;

	private String titleIcon = null;
	
	private WebMarkupContainer imageContainer;
	private WebMarkupContainer titleTextContainer;
	private WebMarkupContainer textContainer;
	private WebMarkupContainer menu = null;

	/**
	 * @param id
	 * @param model
	 * @param mode
	 */

	public ObjectListItemPanel(String id, IModel<T> model, ListPanelMode mode) {
		super(id, model);
		this.mode = mode;
		this.imageVisible = (mode == ListPanelMode.TITLE_TEXT_IMAGE);
	}

	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ListPanelWicketEvent<T>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ListPanelWicketEvent<T> event) {
				if (event.getModel().getObject().getId()
						.equals(ObjectListItemPanel.this.getModel().getObject().getId())) {
					if (event.getName().equals(ListPanel.ITEM_EXPAND)) {
						ObjectListItemPanel.this.titleTextContainer.add(
								new AttributeModifier("class", getCss() + (event.isExpanded() ? " expanded " : "")));
						event.getTarget().add(ObjectListItemPanel.this.titleTextContainer);
					}
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ListPanelWicketEvent) {
					ListPanelWicketEvent<?> e = (ListPanelWicketEvent<?>) event;
					DelleMuseObject o = (DelleMuseObject) e.getModel().getObject();
					if (o.getId().equals(ObjectListItemPanel.this.getModel().getObject().getId()))
						return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onBeforeRender() {
		super.onBeforeRender();

		if (getIcon() != null) {
			WebMarkupContainer ic = new WebMarkupContainer("icon");
			ic.add( new org.apache.wicket.AttributeModifier("class", getIcon()));
			this.imageLink.addOrReplace(ic);

		} else {
			this.imageLink.addOrReplace(new InvisiblePanel("icon"));
		}

		
		if (isImageVisible()) {
			String imageSrc = getImageSrc();

			if (imageSrc != null) {
				Url url = Url.parse(imageSrc);
				UrlResourceReference resourceReference = new UrlResourceReference(url);
				this.image = new Image("image", resourceReference);

				this.imageLink.addOrReplace(image);

			} else {
				this.imageLink.addOrReplace(new InvisiblePanel("image"));
				this.imageLink.setVisible(false);
			}

			WebMarkupContainer noimage = new WebMarkupContainer("noimage") {
				private static final long serialVersionUID = 1L;

				public boolean isVisible() {
					return !imageLink.isVisible();
				}
			};
			this.imageContainer.addOrReplace(noimage);
		}

	}

	
	public void setTutleIcon( String titleIcon ) {
		this.titleIcon=titleIcon;
	}
	
	protected String getTitleIcon() {
		return this.titleIcon;
	}
	
	
	protected String getIcon() {
		return icon;
	}

	//public void setIcon(String icon) {
	//	this.icon = icon;
	//}


	public void setObjectSubtitle(IModel<String> subtitle) {
		this.subtitle = subtitle;
	}

	public boolean isImageVisible() {
		return imageVisible;
	}

	public void setImageVisible(boolean imageVisible) {
		this.imageVisible = imageVisible;
	}

	public boolean isMenuVisible() {
		return menu != null;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		this.imageContainer = new WebMarkupContainer("imageContainer");
		add(this.imageContainer);
		this.imageContainer.setVisible(isImageVisible());

		this.titleTextContainer = new WebMarkupContainer("titleTextContainer");
		add(this.titleTextContainer);
		this.titleTextContainer.setOutputMarkupId(true);

		this.titleTextContainer.add(new org.apache.wicket.AttributeModifier("class", getCss()));

		this.textContainer = new WebMarkupContainer("textContainer");
		this.titleTextContainer.add(this.textContainer);

		WebMarkupContainer menu = null;
		menu = getObjectMenu();
		if (menu == null)
			menu = new InvisiblePanel("menu");
		this.titleTextContainer.add(menu);

		this.imageLink = new Link<>("image-link", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				ObjectListItemPanel.this.onClick();
			}
		};

		this.imageContainer.add(imageLink);

		Link<T> titleLink = new Link<>("title-link", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				ObjectListItemPanel.this.onClick();
			}
		};

		if (getTitleLinkCss() != null)
			titleLink.add(new org.apache.wicket.AttributeModifier("class", getTitleLinkCss()));

		this.titleTextContainer.add(titleLink);

		Label title = new Label("title", getObjectTitle());
		title.setEscapeModelStrings(false);
		titleLink.add(title);

		
		//if (getTitleIcon()!=null) {
				WebMarkupContainer ti=new WebMarkupContainer("titleIcon") {
					private static final long serialVersionUID = 1L;

					public boolean isVisible() {
						return  getTitleIcon()!=null;
					}
				};
				
				ti.add( new org.apache.wicket.AttributeModifier("class", "float-none ps-3 pe-3 " + (getTitleIcon()!=null ? getTitleIcon() :"")));
				this.titleTextContainer.add(ti);
				
		//}
		//else {
		//	titleLink.add( new InvisibleIcon("titleIcon"));
		//}
		
		
		if (getObjectSubtitle() != null) {
			WebMarkupContainer subtitleContainer = new WebMarkupContainer("subtitle-container");
			titleTextContainer.addOrReplace(subtitleContainer);
			Label subtitleLabel = new Label("subtitle", getObjectSubtitle());
			subtitleContainer.add(subtitleLabel);
		} else {
			titleTextContainer.addOrReplace(new InvisiblePanel("subtitle-container"));
		}

		Label text = new Label("text", getInfo());
		text.setEscapeModelStrings(false);
		this.textContainer.add(text);
		this.textContainer.setVisible(this.mode != ListPanelMode.TITLE);

	}

	protected IModel<String> getObjectTitle() {
		return new Model<String>(getModel().getObject().getDisplayname());
	}

	protected WebMarkupContainer getObjectMenu() {
		return null;
	}

	protected IModel<String> getInfo() {
		return new Model<String>(getModel().getObject().toJSON());
	}

	protected IModel<String> getObjectSubtitle() {
		return subtitle;
	}

	protected String getTitleLinkCss() {
		return "title-link";
	}

	public void onClick() {

	}

	protected String getImageSrc() {
		return null;
	}

	protected String getCss() {

		StringBuilder str = new StringBuilder();

		if (isImageVisible())
			str.append("col-xxl-9  col-xl-9  col-lg-9  col-md-7  col-sm-12 text-lg-start text-md-start text-xs-center");
		else
			str.append("col-xxl-12 col-xl-12 col-lg-12 col-md-12 col-sm-12 text-lg-start text-md-start text-xs-center");

		if (this.isMenuVisible()) {
			str.append(" ismenu");
		}
		return str.toString();
	}

}
