package dellemuse.serverapp.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServerDBConstant;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import io.odilon.client.error.ODClientException;
import jakarta.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnails;


@Service
public class ResourceThumbnailService extends BaseService implements SystemService {

	 private static final Logger logger = Logger.getLogger(ResourceThumbnailService.class.getName());

	 @Autowired
	 ObjectStorageService objectStorageService;
	   
	 
	public ResourceThumbnailService(ServerDBSettings settings) {
		super(settings);
	}

	
	
    public String getPresignedThumbnailUrl(Resource resource, ThumbnailSize size) {
    	
    	 final String t_bucket = ServerDBConstant.THUMBNAIL_BUCKET;
         final String t_object = resource.getBucketName()+"-"+ String.valueOf(resource.getObjectName().hashCode()) + "-" + size.getLabel();
         int cacheDurationSecs = ServerDBConstant.THUMBNAIL_CACHE_DURATION_SECS;
         
         try {
             if (getObjectStorageService().getClient().existsObject(t_bucket, t_object)) {
                 return getObjectStorageService().getClient().getPresignedObjectUrl(t_bucket, t_object, Optional.of(cacheDurationSecs));
             }
         } catch (ODClientException e) {
             throw new RuntimeException ( e );
         } catch (IOException e) {
             throw new RuntimeException ( e );
         }
         
         if (!resource.getMedia().startsWith("image")) {
             throw new IllegalArgumentException( Resource.class.getSimpleName() + " is not image -> id: " + resource.getId().toString()  + " | media: " + resource.getMedia());
         }
         
         /** create thumbnail  */
         
         File sourceFile = new File(getSettings().getWorkDir(), resource.getName());

         try {
             try (InputStream in = getObjectStorageService().getClient().getObject(resource.getBucketName(), resource.getObjectName())) {
                 Files.copy(in, sourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
             } catch (ODClientException e) {
                 throw new RuntimeException (e);
             } catch (IOException e) {
                 throw new RuntimeException (e);
             }
             
             File thumbnail = null;
             try {
                 thumbnail = create( resource.getId(), sourceFile, size);
             } catch (IOException e) {
                 throw new RuntimeException ( e );
             }
     
             if (thumbnail==null)
                 throw new RuntimeException ("thumbnail is null");
             
             /** save into object storage  */
             
             try {
                 getObjectStorageService().getClient().putObject(t_bucket, t_object, thumbnail);
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
         
         try {
			
        	 return getObjectStorageService().getClient().getPresignedObjectUrl(t_bucket, t_object, Optional.of(cacheDurationSecs));
		
         } catch (ODClientException e) {

			throw new RuntimeException ( e );
		}
    }

	
	
	

	public ObjectStorageService getObjectStorageService() {
		return objectStorageService;
	}


	public void setObjectStorageService(ObjectStorageService objectStorageService) {
		this.objectStorageService = objectStorageService;
	}

	
	
	
	private class ThumbnailFrame {
        public int th_w;
        public int th_h;
        public int img_w;
        public int img_h;
        public int wo;
        public int ho;
        public boolean use_image = false;
    }
	
	
    static public boolean isGeneralImage(String string) {
        return string.toLowerCase().matches("^.*\\.(png|jpg|jpeg|gif|bmp|heic)$");
    }

    static public boolean isImage(String string) {
        return isGeneralImage(string) || string.toLowerCase().matches("^.*\\.(webp)$");
    }

    
	 public File create(Long id, File file, ThumbnailSize size) throws IOException {

	        if (!isImage(file.getName()))
	            return null;

	        ThumbnailFrame frame = getThumbnailFrame(file, size);

	        if (frame == null)
	            return null;

	        File thDir = new File(getSettings().getWorkDir() + File.separator + ServerDBConstant.THUMBNAIL_BUCKET);
	        String ext = FilenameUtils.getExtension(file.getName());
	        File fileOut = new File(thDir, String.valueOf(id) + "." + ext);

	        if (frame.use_image) {
	        	try (FileOutputStream out = new FileOutputStream(fileOut)) {
		            Files.copy(file.toPath(), out);
	        	}
	            // Files.copy(file, fileOut);
	            return fileOut;
	        }

	        try (FileOutputStream out = new FileOutputStream(fileOut)) {
	            Thumbnails.of(file).size(frame.wo, frame.ho).outputFormat(ext.toLowerCase().equals("png") ? "PNG" : "JPEG")
	                    .toOutputStream(out);
	        }
	        return fileOut;
	    }

	    /**
	     * 
	     * @param srcfile
	     * @param size
	     * @return
	     * @throws IOException
	     */

	    private ThumbnailFrame getThumbnailFrame(File srcfile, ThumbnailSize size) throws IOException {

	        ThumbnailFrame ret = new ThumbnailFrame();

	        try {

	            String ex = FilenameUtils.getExtension(srcfile.getName()).toLowerCase();
	            if (ex.equals("webp")) {
	                ret.use_image = true;
	                return ret;
	            }

	            if (ex.equals("svg")) {
	                ret.use_image = true;
	                return ret;
	            }
	            
	            BufferedImage bimg = ImageIO.read(srcfile);

	            if (bimg == null)
	                return null;

	            ret.th_w = size.getWidth();
	            ret.th_h = size.getHeight();
	            ret.img_w = bimg.getWidth();
	            ret.img_h = bimg.getHeight();

	            // if image is smaller that Thumbnail -> use actual image
	            if (ret.img_w <= ret.th_w && ret.img_h <= ret.th_h) {
	                ret.use_image = true;
	                return ret;
	            }

	            if (ret.th_w != 0 && ret.th_h != 0) {

	                // Si la imagen es más chica que el thumbnail requerido -> devuelve la imagen
	                //
	                if (ret.img_w <= ret.th_w && ret.th_h <= ret.img_h) {
	                    ret.wo = ret.img_w;
	                    ret.ho = ret.img_h;
	                    return ret;
	                }

	                // Si la imagen es más grande que el thumbnail requerido, recorta
	                //

	                int delta_w = (ret.img_w - ret.th_w);
	                int delta_h = (ret.img_h - ret.th_h);

	                if (delta_h < delta_w) {
	                    ret.ho = ret.th_h;
	                    ret.wo = (ret.img_h > 0) ? ret.img_w * ret.ho / ret.img_h : (int) (ret.th_w);
	                } else {
	                    ret.wo = ret.th_w;
	                    ret.ho = (ret.img_w > 0) ? ret.img_h * ret.wo / ret.img_w : (int) (ret.th_h);
	                }

	                return ret;
	            }

	            // Si h es 0
	            //
	            else if (ret.th_h == 0 && ret.th_w != 0) {

	                int delta_w = (ret.img_w - ret.th_w);

	                // Si el ancho del th es mas grande que el ancho de la imagen devuelve la imagen
	                if (delta_w < 0) {
	                    ret.ho = ret.img_h;
	                    ret.wo = ret.img_w;
	                    return ret;
	                } else {

	                    // Si el ancho del th es mas chico que el ancho de la imagen resizea
	                    // proporcional

	                    ret.wo = ret.th_w;
	                    ret.ho = (ret.img_w > 0) ? ret.img_h * ret.wo / ret.img_w : (int) (ret.img_h);
	                    if (ret.ho <= 0)
	                        ret.ho = ret.img_h;

	                    return ret;
	                }
	            }
	            // Si w es 0
	            //
	            else {

	                int delta_h = (ret.img_h - ret.th_h);

	                // Si el alto del th es mas grande que el alto de la imagen devuelve la imagen
	                //
	                if (delta_h < 0) {
	                    ret.ho = ret.img_h;
	                    ret.wo = ret.img_w;
	                    return ret;
	                } else {

	                    // Si el alto del th es mas chico que el alto de la imagen resizea proporcional
	                    //
	                    ret.ho = ret.th_h;
	                    ret.wo = (ret.img_h > 0) ? ret.img_w * ret.ho / ret.img_h : (int) (ret.img_w);
	                    if (ret.wo <= 0)
	                        ret.wo = ret.img_w;

	                    return ret;
	                }
	            }

	        } catch (Exception e) {
	            logger.error(e);
	            return null;
	        }

	    }
	
	
	
	
	
	
}
