package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import dellemuse.model.InstitutionModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.service.GuideContentDBService;
import dellemuse.server.db.service.InstitutionDBService;

@Service
public class InstitutionModelService extends ModelService<Institution, InstitutionModel> {

    public InstitutionModelService(Settings settings, InstitutionDBService dbService) {
        super(settings, Institution.class, InstitutionModel.class, dbService);
    }

}
