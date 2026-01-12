package dellemuse.serverapp.page;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;

import org.apache.wicket.model.IModel;

import org.apache.wicket.request.Url;

import org.apache.wicket.request.resource.UrlResourceReference;

import io.wktui.struct.list.ListPanelMode;

import wktui.base.InvisiblePanel;

import wktui.base.ModelPanel;

public abstract class ObjectListItemExpandedPanel<T> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;

	private IModel<String> subtitle;
	private String icon;
	// private IModel<String> iconCss;
	private Link<T> imageLink;
	private Image image;
	private ListPanelMode mode;
	private boolean imageVisible = true;

	private WebMarkupContainer imageContainer;
	private WebMarkupContainer titleTextContainer;
	private WebMarkupContainer textContainer;

	public ObjectListItemExpandedPanel(String id, IModel<T> model, ListPanelMode mode) {
		super(id, model);
		this.mode = mode;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		this.imageContainer = new WebMarkupContainer("imageContainer");
		add(this.imageContainer);

		this.titleTextContainer = new WebMarkupContainer("titleTextContainer");
		add(this.titleTextContainer);

		this.titleTextContainer.add(new org.apache.wicket.AttributeModifier("class", isImageVisible() ? "mt-2 col-xxl-12  col-xl-12  col-lg-12  col-md-12  col-sm-12 text-lg-start text-md-start text-xs-center"
				: "mt-2 col-xxl-12 col-xl-12 col-lg-12 col-md-12 col-sm-12 text-lg-start text-md-start text-xs-center"));

		this.textContainer = new WebMarkupContainer("textContainer");
		this.titleTextContainer.add(this.textContainer);

		this.imageLink = new Link<>("image-link", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				ObjectListItemExpandedPanel.this.onClick();
			}
		};

		this.imageContainer.add(imageLink);

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
		this.textContainer.setVisible(true);

		this.imageVisible = getImageSrc() != null;

		this.imageContainer.setVisible(this.isImageVisible());

		if (getIcon() != null) {
			WebMarkupContainer ic = new WebMarkupContainer("icon");
			ic.add(new org.apache.wicket.AttributeModifier("class", getIcon()));
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

	protected String getTitleLinkCss() {
		return "title-link";
	}

	protected abstract IModel<String> getInfo();

	protected IModel<String> getObjectSubtitle() {
		return subtitle;
	}

	public void onClick() {
	}

	protected String getImageSrc() {
		return null;
	}

	protected String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setObjectSubtitle(IModel<String> subtitle) {
		this.subtitle = subtitle;
	}

	public boolean isImageVisible() {
		return imageVisible;
	}

	public void setImageVisible(boolean imageVisible) {
		this.imageVisible = imageVisible;
	}

}
