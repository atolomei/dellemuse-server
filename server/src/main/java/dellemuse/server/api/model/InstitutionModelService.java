package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import dellemuse.model.InstitutionModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Institution;

@Service
public class InstitutionModelService extends ModelService<Institution, InstitutionModel> {

    public InstitutionModelService(Settings settings) {
        super(settings, Institution.class, InstitutionModel.class);
    }

}
