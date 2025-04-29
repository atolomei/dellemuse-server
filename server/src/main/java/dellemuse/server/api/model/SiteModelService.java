package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import dellemuse.model.InstitutionModel;
import dellemuse.model.SiteModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Site;

@Service
public class SiteModelService extends ModelService<Site, SiteModel> {

    public SiteModelService(Settings settings) {
        super(settings, Site.class, SiteModel.class);
    }
}
