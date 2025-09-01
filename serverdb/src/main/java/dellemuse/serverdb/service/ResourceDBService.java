package dellemuse.serverdb.service;

import java.time.OffsetDateTime;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.serverdb.ServerDBConstant;
import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.model.Resource;
import dellemuse.serverdb.model.User;
import dellemuse.serverdb.object.service.ResourceService;
import dellemuse.serverdb.service.base.ServiceLocator;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.FSUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;




@Service
public class ResourceDBService extends DBService<Resource, Long> implements ApplicationContextAware {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ResourceDBService.class.getName());

    @JsonIgnore
    private ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;

    public ResourceDBService(CrudRepository<Resource, Long> repository,
                             ServerDBSettings settings) {
        super(repository,  settings);
    }

    @PostConstruct
    protected void onInitialize() {
    	ServiceLocator.getInstance().register(getEntityClass(), this);
    }
    
    @Transactional
    public Long newId() {
        // Usamos EntityManager para la consulta nativa
        return ((Number) entityManager.createNativeQuery("SELECT nextval('objectstorage_id')").getSingleResult()).longValue();
    }

    public String normalizeFileName(String name) {
        String str = name.replaceAll("[^\\x00-\\x7F]|[\\s]+", "-").toLowerCase().trim();
        if (str.length() < 100)
            return str;
        return str.substring(0, 100);
    }

    public String getMimeType(String fileName) {
        if (FSUtil.isImage(fileName)) {
            String ext = FilenameUtils.getExtension(fileName).toLowerCase();

            if (ext.equals("jpg") || ext.equals("jpeg"))
                return "image/jpeg";

            return "image/" + ext;
        }

        if (FSUtil.isPdf(fileName))
            return "application/pdf";

        if (FSUtil.isVideo(fileName))
            return "video/" + FilenameUtils.getExtension(fileName);

        if (FSUtil.isAudio(fileName))
            return "audio/" + FilenameUtils.getExtension(fileName);

        return "";
    }

    @Override
    @Transactional
    public Resource create(String objectName, User createdBy) {
        return create(ServerDBConstant.MEDIA_BUCKET, objectName, objectName, null, createdBy);
    }

    @Transactional
    public Resource create(String objectName, String name, User createdBy) {
        return create(ServerDBConstant.MEDIA_BUCKET, objectName, name, null, createdBy);
    }

    @Transactional
    public Resource create(String bucketName, String objectName, String name, String media, User createdBy) {
        Resource c = new Resource();
        c.setBucketName(bucketName);
        c.setObjectName(objectName);
        c.setName(name);
        c.setNameKey(nameKey(name));
        if (media != null)
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
        this.applicationContext = applicationContext;
    }
}









/**
 * 

@Service
public class ResourceDBService extends DBService<Resource, Long> implements ApplicationContextAware {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ResourceDBService.class.getName());

    @JsonIgnore
    ApplicationContext applicationContext;

    public ResourceDBService(CrudRepository<Resource, Long> repository, EntityManagerFactory entityManagerFactory,
            Settings settings) {
        super(repository, entityManagerFactory, settings);

    }

    @Transactional
    public Long newId() {
        Long nextVal = (Long) getSessionFactory().getCurrentSession()
                .createNativeQuery("SELECT nextval('objectstorage_id')", Long.class).getSingleResult();
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
 

    @Override
    @Transactional
    public Resource create(String objectName, User createdBy) {
        return create(ServerConstant.MEDIA_BUCKET, objectName, objectName, null, createdBy);
    }

    @Transactional
    public Resource create(String objectName, String name, User createdBy) {
        return create(ServerConstant.MEDIA_BUCKET, objectName, name, null, createdBy);
    }

    public void test() {
    }

    @Transactional
    public Resource create(String bucketName, String objectName, String name, String media, User createdBy) {
        Resource c = new Resource();
        c.setBucketName(bucketName);
        c.setObjectName(objectName);
        c.setName(name);
        c.setNameKey(nameKey(name));
        if (media != null)
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
        this.applicationContext = applicationContext;
    }
    
    */

