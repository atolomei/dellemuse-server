package dellemuse.serverapp;

import org.apache.wicket.protocol.http.WebApplication;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;

import dellemuse.serverapp.page.error.InternalErrorPage;
import dellemuse.serverapp.page.error.SessionExpiredPage;

@ApplicationInitExtension
public class DellemuseServerAppWicketConfiguration implements WicketApplicationInitConfiguration {

    @Override
	public void init(WebApplication webApplication) {
	   webApplication.getCspSettings().blocking().disabled();
	   webApplication.getApplicationSettings().setPageExpiredErrorPage(SessionExpiredPage.class);
	   webApplication.getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);
	}
}
