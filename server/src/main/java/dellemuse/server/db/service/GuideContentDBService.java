package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtExhibitionItem;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.transaction.Transactional;

@Service
public class GuideContentDBService extends DBService<GuideContent, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(GuideContentDBService.class.getName());

    public GuideContentDBService(CrudRepository<GuideContent, Long> repository, Settings settings) {
        super(repository,  settings);
    }

    
    
    @Transactional
    public GuideContent create(ArtExhibitionGuide guide, ArtExhibitionItem item, String name,User createdBy) {
        GuideContent c = new GuideContent();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setArtExhibitionGuide(guide);
        c.setArtExhibitionItem(item);
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        
        return getRepository().save(c);
        
    }
    
    /**
     * <p>
     * Annotation Transactional is required to store values into the Database
     * </p>
     * 
     * @param name
     * @param createdBy
     */
    @Transactional
    @Override
    public GuideContent create(String name,User createdBy) {
        GuideContent c = new GuideContent();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    /**
     * @param name
     * @return
     */
    public List<GuideContent> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<GuideContent> getEntityClass() {
        return GuideContent.class;
    }

    /**
     * Returns the ArtWork associated with a GuideContent via its ArtExhibitionItem.
     * Returns {@code Optional.empty()} if the GuideContent has no ArtExhibitionItem
     * or the ArtExhibitionItem has no ArtWork.
     */
    @Transactional
    public Optional<ArtWork> getArtWork(GuideContent g) {
        if (g == null || g.getId() == null)
            return Optional.empty();
        // Re-fetch within current session to avoid LazyInitializationExceptionatDB
        GuideContent managed = getEntityManager().find(GuideContent.class, g.getId());
        if (managed == null)
            return Optional.empty();
        ArtExhibitionItem item = managed.getArtExhibitionItem();
        if (item == null)
            return Optional.empty();
        ArtWork aw = item.getArtwork();
        if (aw == null)
            return Optional.empty();
        return Optional.of(aw);
    }
}
