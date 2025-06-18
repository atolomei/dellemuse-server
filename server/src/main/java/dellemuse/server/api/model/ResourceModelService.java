package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import dellemuse.model.InstitutionModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.model.Site;

@Service
public class ResourceModelService extends ModelService<Resource, ResourceModel> {

    public ResourceModelService(Settings settings) {
        super(settings, Resource.class, ResourceModel.class);
    }
}
