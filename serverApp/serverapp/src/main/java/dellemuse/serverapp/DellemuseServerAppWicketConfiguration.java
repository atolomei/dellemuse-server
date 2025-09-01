package dellemuse.serverapp;

import org.apache.wicket.protocol.http.WebApplication;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;

@ApplicationInitExtension
public class DellemuseServerAppWicketConfiguration implements WicketApplicationInitConfiguration {

    @Override
	public void init(WebApplication webApplication) {
	    webApplication.getCspSettings().blocking().disabled();
	}
}
