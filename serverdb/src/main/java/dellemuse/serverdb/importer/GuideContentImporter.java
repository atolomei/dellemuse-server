package dellemuse.serverdb.importer;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.module.SimpleModule;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.importer.serializer.GuideContentImporterDeserialiser;
import dellemuse.serverdb.model.GuideContent;
import dellemuse.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverdb.service.GuideContentDBService;
import dellemuse.serverdb.service.PersonDBService;
import dellemuse.serverdb.service.ResourceDBService;
import dellemuse.serverdb.service.UserDBService;

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

    public GuideContentImporter(ServerDBSettings settings, PersonDBService personDBService, UserDBService userDBService,
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
