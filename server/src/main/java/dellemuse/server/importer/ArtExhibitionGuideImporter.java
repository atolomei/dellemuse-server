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
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.service.ArtExhibitionGuideDBService;
import dellemuse.server.db.service.GuideContentDBService;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.serializer.ArtExhibitionGuideImporterDeserialiser;
import dellemuse.server.objectstorage.ObjectStorageService;

@Service
public class ArtExhibitionGuideImporter extends BaseImporter {

    @JsonIgnore
    @Autowired
    private final PersonDBService personDBService;

    @JsonIgnore
    @Autowired
    private final ArtExhibitionGuideDBService artExhibitionGuideDBService;

    @JsonIgnore
    @Autowired
    private final GuideContentDBService guideContentDBService;

    public ArtExhibitionGuideImporter(Settings settings, PersonDBService personDBService, UserDBService userDBService,
            ObjectStorageService objectStorageService, ArtExhibitionGuideDBService artExhibitionGuideDBService,
            GuideContentDBService guideContentDBService) {
        super(settings, userDBService, objectStorageService, ArtExhibitionGuide.class.getSimpleName().toLowerCase());

        this.personDBService = personDBService;
        this.artExhibitionGuideDBService = artExhibitionGuideDBService;
        this.guideContentDBService = guideContentDBService;

        ArtExhibitionGuideImporterDeserialiser personDeserializer = new ArtExhibitionGuideImporterDeserialiser(
                ArtExhibitionGuide.class, this);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ArtExhibitionGuide.class, personDeserializer);
        getObjectMapper().registerModule(module);
    }

    @Override
    protected boolean read(File file) throws StreamReadException, DatabindException, IOException {
        ArtExhibitionGuide object = getObjectMapper().readValue(file, ArtExhibitionGuide.class);
        return true;
    }

    public ArtExhibitionGuideDBService getArtExhibitionGuideDB() {
        return this.artExhibitionGuideDBService;
    }

    public GuideContentDBService getGuideContentDBService() {
        return guideContentDBService;
    }

}
