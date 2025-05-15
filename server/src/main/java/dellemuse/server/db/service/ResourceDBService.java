package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.server.ServerConstant;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.model.ResourceId;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.model.User;
import dellemuse.server.object.service.ResourceService;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Constant;
import dellemuse.model.util.FSUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;


@Service
public class ResourceDBService extends DBService<Resource, Long> implements ApplicationContextAware {

    static private Logger logger = Logger.getLogger(ResourceDBService.class.getName());

    @JsonIgnore
    ApplicationContext applicationContext;
    
    public ResourceDBService(CrudRepository<Resource, Long> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
        super(repository, entityManagerFactory, settings);
        
    }

    @Transactional
    public Long newId() {
        Long nextVal = (Long) getSessionFactory().getCurrentSession().createNativeQuery("SELECT nextval('objectstorage_id')", Long.class).getSingleResult();
        return nextVal;
    }
    

    public String normalizeFileName(String name) {
        String str = name.replaceAll("[^\\x00-\\x7F]|[\\s]+", "-").toLowerCase().trim();
        if (str.length() < 100)
            return str;
        return str.substring(0, 100);
    }

    public String getMimeType(String fileName) {

        if (FSUtil.isImage(fileName)) {
            String str = FilenameUtils.getExtension(fileName);

            if (str.equals("jpg"))
                return "image/jpeg";

            if (str.equals("jpeg"))
                return "image/jpeg";

            return "image/" + str;
        }

        if (FSUtil.isPdf(fileName))
            return "application/pdf";

        if (FSUtil.isVideo(fileName))
            return "video/" + FilenameUtils.getExtension(fileName);

        if (FSUtil.isAudio(fileName))
            return "audio/" + FilenameUtils.getExtension(fileName);

        return "";
    }
    
    /**
     * <p>
     * Annotation Transactional is required to store values into the Database
     * </p>
     * 
     * @param name
     * @param createdBy
     */


    @Override
    @Transactional
    public Resource create(String objectName,  User createdBy) {
        return create(ServerConstant.MEDIA_BUCKET, objectName, objectName, null, createdBy);
    }
    
    
    @Transactional
    public Resource create(String objectName, String name, User createdBy) {
        return create(ServerConstant.MEDIA_BUCKET, objectName, name, null, createdBy);
    }
    
    
    
    public void test() {
    }
    
    
    @Transactional
    public Resource create(String bucketName, String objectName,  String name, String media, User createdBy) {
        
        Resource c = new Resource();

        c.setBucketName(bucketName);
        c.setObjectName(objectName);
        c.setName(name);
        
        if (media!=null)
            c.setMedia(media);
        
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        
        return getRepository().save(c);
    }

    @Override
    protected Class<Resource> getEntityClass() {
        return Resource.class;
    }


    public ResourceService getResourceService(Resource resource) {
        return this.applicationContext.getBean(ResourceService.class, resource); 
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
        
    }

}
