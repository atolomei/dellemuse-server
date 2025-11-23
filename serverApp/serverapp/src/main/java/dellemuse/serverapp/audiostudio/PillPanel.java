package dellemuse.serverapp.audiostudio;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class PillPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private WebMarkupContainer pillContainer;

	private IModel<String> title;
	private IModel<String> text;
	private String css = null;

	public PillPanel(String id, IModel<String> title, IModel<String> text) {
		super(id);
		this.title = title;
		this.text = text;
		this.setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		pillContainer = new WebMarkupContainer("pillContainer");
		add(pillContainer);

		pillContainer.add(new Label("title", getTitle()));
		pillContainer.add(new Label("text", getText()));
	}

	public void onBeforeRender() {
		super.onBeforeRender();

		if (getCss() != null)
			pillContainer.add(new org.apache.wicket.AttributeModifier("class", getCss()));
	}

	public IModel<String> getTitle() {
		return this.title;
	}

	public IModel<String> getText() {
		return this.text;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getCss() {
		return css;
	}
}
