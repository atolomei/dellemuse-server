package dellemuse.server.api.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.http.ResponseEntity.HeadersBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.ResourceModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.FSUtil;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.server.ServerConstant;
import dellemuse.server.Settings;
import dellemuse.server.api.model.ArtExhibitionModelService;
import dellemuse.server.api.model.ModelService;
import dellemuse.server.api.model.ResourceModelService;
import dellemuse.server.db.model.Resource;

import dellemuse.server.db.service.DBService;
import dellemuse.server.db.service.ResourceDBService;
import dellemuse.server.error.ObjectNotFoundException;
import dellemuse.server.objectstorage.ObjectStorageService;
import dellemuse.server.objectstorage.ThumbnailService;
import dellemuse.server.security.SecurityService;
import dellemuse.util.MediaUtil;
import io.odilon.client.error.ODClientException;
import io.odilon.model.ObjectMetadata;
import io.odilon.model.ObjectStatus;
import io.odilon.net.ErrorCode;
import io.odilon.net.ODHttpStatus;

@RestController
@RequestMapping(value = "/resource")
public class ResourceController extends BaseController<Resource, ResourceModel> {

     
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(ResourceController.class.getName());

    @JsonIgnore
    @Autowired
    private final ObjectStorageService objectService;

    @JsonIgnore
    @Autowired
    private final ResourceDBService dbService;

    @JsonIgnore
    @Autowired
    private final ResourceModelService modelService;

    @JsonIgnore
    @Autowired
    private final ThumbnailService thumbnailService;

    @JsonIgnore
    @Autowired
    private final Settings settings;
    
    
    public ResourceController(SecurityService securityService, ResourceDBService dbService, ResourceModelService modelService,
            ObjectStorageService objectService, ThumbnailService thumbnailService, Settings settings) {
        super(securityService);

        this.dbService = dbService;
        this.modelService = modelService;
        this.objectService = objectService;
        this.thumbnailService=thumbnailService;
        this.settings=settings;
    
    }

    @Override
    public DBService<Resource, Long> getDBService() {
        return this.dbService;
    }

    @Override
    public ModelService<Resource, ResourceModel> getModelService() {
        return this.modelService;
    }

    @RequestMapping(value = "/getpresigned/{id}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<String> presignedUrl(@PathVariable("id") String id) {

        Long lid = Long.valueOf(id);

        Optional<Resource> o = getDBService().findById(lid);

        if (o.isEmpty()) {
            throw new IllegalArgumentException("not found for id ->" + id.toString());
        }

        try {
            
            String in = getObjectService().getClient().getPresignedObjectUrl(o.get().getBucketName(), o.get().getObjectName());
            return new ResponseEntity<String>(in, HttpStatus.OK);
            
        } catch (ODClientException e) {
            throw new RuntimeException(e);
        }

    }
    
    
    @RequestMapping(value = "/getpresignedthumbnail/{size}/{id}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<String> presignedThumbnailUrl(@PathVariable("size") String size, @PathVariable("id") String id) {

        Long lid = Long.valueOf(id);

        Optional<Resource> o = getDBService().findById(lid);
        
        Resource res = o.get();
        
        if (o.isEmpty()) 
            throw new ObjectNotFoundException(Resource.class.getSimpleName() + " not found id ->" + id.toString());
    
        ThumbnailSize thumbnailSize = ThumbnailSize.of(size);
        
        final String t_bucket = ServerConstant.THUMBNAIL_BUCKET;
        final String t_object = res.getBucketName()+"-"+ String.valueOf(res.getObjectName().hashCode()) + "-" + thumbnailSize.getLabel();
        
        int cacheDurationSecs = ServerConstant.THUMBNAIL_CACHE_DURATION_SECS;
        
        /** if exists -> return existings thumbnail */
        try {
            if (getObjectService().getClient().existsObject(t_bucket, t_object)) {
                return ResponseEntity
                .ok()
                .cacheControl( CacheControl.maxAge(cacheDurationSecs, TimeUnit.SECONDS))
                .body(getObjectService().getClient().getPresignedObjectUrl(t_bucket, t_object, Optional.of(cacheDurationSecs)));
            }
        } catch (ODClientException e) {
            throw new RuntimeException ( e );
        } catch (IOException e) {
            throw new RuntimeException ( e );
        }
        
        
        if (!o.get().getMedia().startsWith("image")) {
            throw new IllegalArgumentException( Resource.class.getSimpleName() + " is not image -> id: " + o.get().getId().toString()  + " | media: " + o.get().getMedia());
        }
        
        
        /** create thumbnail  */
        
        File sourceFile = new File(getSettings().getWorkDir(), res.getName());

        try {
            try (InputStream in = getObjectService().getClient().getObject(res.getBucketName(), res.getObjectName())) {
                Files.copy(in, sourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (ODClientException e) {
                throw new RuntimeException (e);
            } catch (IOException e) {
                throw new RuntimeException (e);
            }
            
            File thumbnail = null;
            try {
                thumbnail = getThumbnailService().create(lid, sourceFile, thumbnailSize);
            } catch (IOException e) {
                throw new RuntimeException ( e );
            }
    
            if (thumbnail==null)
                throw new RuntimeException ("thumbnail is null");
            
            /** save into object storage  */
            
            try {
                getObjectService().getClient().putObject(t_bucket, t_object, thumbnail);
            } catch (ODClientException e) {
                throw new RuntimeException ( e );
            }
        } finally {
            if (sourceFile.exists()) {
                try {
                    FileUtils.forceDelete(sourceFile);
                } catch (IOException e) {
                        logger.error(e);
                }
            }
        }
        
        /** return existing  */
        try {
            return ResponseEntity
                    .ok()
                    .cacheControl( CacheControl.maxAge(cacheDurationSecs, TimeUnit.SECONDS))
                    .body(getObjectService().getClient().getPresignedObjectUrl(t_bucket, t_object, Optional.of(cacheDurationSecs)));
            
        } catch (ODClientException e) {
            throw new RuntimeException ( e );
        }
    }

    @RequestMapping(path = "/getstreambyid/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getObjectStreamById(@PathVariable("id") String id) {

        Long lid = Long.valueOf(id);

        Optional<Resource> o = getDBService().findById(lid);

        if (o.isEmpty()) {
            throw new IllegalArgumentException("not found for id ->" + id.toString());
        }

        MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
        InputStream in;
        try {
            in = getObjectService().getObject(o.get().getBucketName(), o.get().getObjectName());
            return ResponseEntity.ok().contentType(contentType).body(new InputStreamResource(in));
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @RequestMapping(path = "/getstream/{bucketName}/{objectName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getObjectStream(@PathVariable("bucketName") String bucketName,
            @PathVariable("objectName") String objectName) {

        // try {

        // if (!getObjectStorageService().existsObject(bucketName, objectName))
        // throw new OdilonObjectNotFoundException(String.format("object not found -> b:
        // %s | o:%s",
        // Optional.ofNullable(bucketName).orElse("null"),
        // Optional.ofNullable(objectName).orElse("null")));

        // ObjectMetadata meta = getObjectStorageService().getObjectMetadata(bucketName,
        // objectName);

        // if (meta == null || meta.status == ObjectStatus.DELETED || meta.status ==
        // ObjectStatus.DRAFT)
        // throw new OdilonObjectNotFoundException(String.format("object not found -> b:
        // %s | o:%s",
        // Optional.ofNullable(bucketName).orElse("null"),
        // Optional.ofNullable(objectName).orElse("null")));

        MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
        InputStream in;
        try {
            in = getObjectService().getObject(bucketName, objectName);
            return ResponseEntity.ok().contentType(contentType).body(new InputStreamResource(in));
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

        // } catch (DServerAPIException e1) {
        // throw e1;
        // } catch (Exception e) {
        // throw new OdilonServerAPIException(ODHttpStatus.INTERNAL_SERVER_ERROR,
        // ErrorCode.INTERNAL_ERROR, getMessage(e));
        // } finally {
        //
        // }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getPresignedObjectStream(@RequestParam("token") String stringToken) {

        return null;

        // try {
        // if (stringToken == null)
        // throw new OdilonServerAPIException("token is null");
        // AuthToken authToken = this.tokenService.decrypt(stringToken);
        
        /**
         * if (authToken == null) throw new OdilonServerAPIException("AuthToken is
         * null");
         * 
         * if (!authToken.isValid()) { logger.error(String.format("token expired -> t:
         * %s", authToken.toString())); throw new
         * OdilonServerAPIException(String.format("token expired -> t: %s",
         * authToken.toString())); }
         * 
         * String bucketName = authToken.bucketName; String objectName =
         * authToken.objectName;
         * 
         * VirtualFileSystemObject object =
         * getObjectStorageService().getObject(bucketName, objectName);
         * 
         * if (object == null) throw new
         * OdilonObjectNotFoundException(String.format("not found -> b: %s | o:%s",
         * Optional.ofNullable(bucketName).orElse("null"),
         * Optional.ofNullable(objectName).orElse("null")));
         * 
         * HttpHeaders responseHeaders = new HttpHeaders();
         * 
         * String f_name = object.getObjectMetadata().fileName.replace("[",
         * "").replace("]", ""); responseHeaders.set("Content-Disposition", "inline;
         * filename=\"" + f_name + "\"");
         * 
         * MediaType contentType =
         * MediaType.valueOf(object.getObjectMetadata().contentType);
         * 
         * if (object.getObjectMetadata().contentType() == null ||
         * object.getObjectMetadata().contentType.equals("application/octet-stream")) {
         * contentType = estimateContentType(f_name); }
         * 
         * InputStream in = object.getInputStream();
         * 
         * getSystemMonitorService().getGetObjectMeter().mark();
         * 
         * return
         * ResponseEntity.ok().headers(responseHeaders).contentType(contentType).body(new
         * InputStreamResource(in));
         * 
         * } catch (Exception e) { throw new
         * OdilonServerAPIException(ODHttpStatus.INTERNAL_SERVER_ERROR,
         * ErrorCode.INTERNAL_ERROR, getMessage(e));
         * 
         * } finally { getTrafficControlService().release(pass); mark(); }
         */
    }

    private ObjectStorageService getObjectService() {
        return this.objectService;
    }

    public ResourceDBService getDbService() {
        return dbService;
    }

    public ThumbnailService getThumbnailService() {
        return thumbnailService;
    }

    public Settings getSettings() {
        return settings;
    }
}
