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
import dellemuse.serverdb.importer.serializer.ArtExhibitionGuideImporterDeserialiser;
import dellemuse.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverdb.service.GuideContentDBService;
import dellemuse.serverdb.service.PersonDBService;
import dellemuse.serverdb.service.UserDBService;

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

    public ArtExhibitionGuideImporter(ServerDBSettings settings, PersonDBService personDBService, UserDBService userDBService,
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
