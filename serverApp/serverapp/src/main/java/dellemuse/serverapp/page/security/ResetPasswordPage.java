package dellemuse.serverapp.page.security;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ResetPasswordPage extends WebPage {

    private static final long serialVersionUID = 1L;

    public ResetPasswordPage(PageParameters params) {
        super(params);
        String username = params.get("user").toString("User");
        add(new Label("message", "Hello " + username + ", please enter your new password (page placeholder)."));
    }
}
