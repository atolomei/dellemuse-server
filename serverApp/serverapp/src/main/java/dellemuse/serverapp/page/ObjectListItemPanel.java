package dellemuse.serverapp.page;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import io.wktui.nav.menu.DropDownMenu;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.menu.SeparatorMenuItem;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.BasePanel;
import wktui.base.InvisiblePanel;
import wktui.base.LabelPanel;
import wktui.base.ModelPanel;

public class ObjectListItemPanel<T extends DelleMuseObject> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;

	private IModel<String> subtitle;
	private IModel<String> icon;
	private IModel<String> iconCss;
	private Link<T> imageLink;
	private Image image;
	ListPanelMode mode;
	private boolean imageVisible = true;
	
	private WebMarkupContainer imageContainer;
	private WebMarkupContainer titleTextContainer;
	private WebMarkupContainer textContainer;
	
	 

	public ObjectListItemPanel(String id, IModel<T> model, ListPanelMode mode) {
		super(id, model);
		this.mode=mode;
		this.imageVisible= (mode==ListPanelMode.TITLE_TEXT_IMAGE);
		
	}

	
	public void onBeforeRender() {
		super.onBeforeRender();

		if (getSubtitle() != null) {
			WebMarkupContainer subtitleContainer = new WebMarkupContainer("subtitle-container");
			titleTextContainer.addOrReplace(subtitleContainer);
			Label subtitleLabel = new Label("subtitle", getSubtitle());
			subtitleContainer.add(subtitleLabel);
		} else {
			titleTextContainer.addOrReplace(new InvisiblePanel("subtitle-container"));
		}

		if (getIcon() != null) {
			WebMarkupContainer ic = new WebMarkupContainer("icon");
			imageLink.addOrReplace(ic);

		} else {
			imageLink.addOrReplace(new InvisiblePanel("icon"));
		}

		if (isImageVisible()) {
			String imageSrc = getImageSrc();

			if (imageSrc != null) {
				Url url = Url.parse(imageSrc);
				UrlResourceReference resourceReference = new UrlResourceReference(url);
				image = new Image("image", resourceReference);

				imageLink.addOrReplace(image);

			} else {
				imageLink.addOrReplace(new InvisiblePanel("image"));
				imageLink.setVisible(false);
			}

			WebMarkupContainer noimage = new WebMarkupContainer("noimage") {
				private static final long serialVersionUID = 1L;

				public boolean isVisible() {
					return !imageLink.isVisible();
				}
			};
			imageContainer.addOrReplace(noimage);
		}

	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		this.imageContainer = new WebMarkupContainer("imageContainer");
		add(this.imageContainer);
		this.imageContainer.setVisible(isImageVisible());
		
		this.titleTextContainer = new WebMarkupContainer("titleTextContainer");
		add(this.titleTextContainer);
		this.titleTextContainer.add(new org.apache.wicket.AttributeModifier("class", isImageVisible() ? 
									"col-xxl-9  col-xl-9  col-lg-9  col-md-7  col-sm-12 text-lg-start text-md-start text-xs-center" : 
					  			   	"col-xxl-12 col-xl-12 col-lg-12 col-md-12 col-sm-12 text-lg-start text-md-start text-xs-center"));
			
		
		this.textContainer = new WebMarkupContainer("textContainer");
		this.titleTextContainer.add(this.textContainer);
		
		
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
		
		if (getTitleLinkCss()!=null)
			titleLink.add( new org.apache.wicket.AttributeModifier( "class",getTitleLinkCss()));
		
		this.titleTextContainer.add(titleLink);

		Label title = new Label("title", getObjectTitle() );
		title.setEscapeModelStrings(false);
		titleLink.add(title);

		Label text = new Label("text", getInfo());
		text.setEscapeModelStrings(false);
		this.textContainer.add(text);
		this.textContainer.setVisible( this.mode!=ListPanelMode.TITLE);
		
	}

	protected String getTitleLinkCss() {
		return "title-link";
	}


	protected IModel<String> getObjectTitle() {
		return new Model<String> (getModel().getObject().getDisplayname() );
	}

	protected IModel<String> getInfo() {
		return new Model<String>( getModel().getObject().toJSON());
	}

	public void onClick() {

	}

	protected String getImageSrc() {
		return null;
	}
	

	public IModel<String> getSubtitle() {
		return subtitle;
	}

	public IModel<String> getIcon() {
		return icon;
	}

	public void setIcon(IModel<String> icon) {
		this.icon = icon;
	}

	public IModel<String> getIconCss() {
		return iconCss;
	}

	public void setIconCss(IModel<String> iconCss) {
		this.iconCss = iconCss;
	}

	public void setSubtitle(IModel<String> subtitle) {
		this.subtitle = subtitle;
	}

	public boolean isImageVisible() {
		return imageVisible;
	}

	public void setImageVisible(boolean imageVisible) {
		this.imageVisible = imageVisible;
	}

}
