package dellemuse.server.importer;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.module.SimpleModule;

import dellemuse.server.Settings;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.db.service.ArtExhibitionGuideDBService;
import dellemuse.server.db.service.GuideContentDBService;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.ResourceDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.serializer.GuideContentImporterDeserialiser;
import dellemuse.server.objectstorage.ObjectStorageService;

@Service
public class GuideContentImporter extends BaseImporter {

    @JsonIgnore
    @Autowired
    private final PersonDBService personDBService;

    @JsonIgnore
    @Autowired
    private final ArtExhibitionGuideDBService artExhibitionGuideDBService;

    @JsonIgnore
    @Autowired
    private final GuideContentDBService guideContentDBService;

    @JsonIgnore
    @Autowired
    private final ResourceDBService resourceDBService;

    public GuideContentImporter(Settings settings, PersonDBService personDBService, UserDBService userDBService,
            ObjectStorageService objectStorageService, ArtExhibitionGuideDBService artExhibitionGuideDBService,
            GuideContentDBService guideContentDBService, ResourceDBService resourceDBService) {
        super(settings, userDBService, objectStorageService, GuideContent.class.getSimpleName().toLowerCase());

        this.personDBService = personDBService;
        this.artExhibitionGuideDBService = artExhibitionGuideDBService;
        this.guideContentDBService = guideContentDBService;
        this.resourceDBService = resourceDBService;

        GuideContentImporterDeserialiser personDeserializer = new GuideContentImporterDeserialiser(GuideContent.class, this);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(GuideContent.class, personDeserializer);
        getObjectMapper().registerModule(module);
    }

    @SuppressWarnings("unused")
    @Override
    protected boolean read(File file) throws StreamReadException, DatabindException, IOException {
        GuideContent object = getObjectMapper().readValue(file, GuideContent.class);
        return true;
    }

    public ArtExhibitionGuideDBService getArtExhibitionGuideDB() {
        return this.artExhibitionGuideDBService;
    }

    public GuideContentDBService getGuideContentDBService() {
        return guideContentDBService;
    }

    public ResourceDBService getResourceDBService() {
        return resourceDBService;

    }

}
