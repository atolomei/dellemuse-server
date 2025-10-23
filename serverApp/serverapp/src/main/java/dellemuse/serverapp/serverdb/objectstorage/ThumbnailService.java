package dellemuse.serverapp.serverdb.objectstorage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServerDBConstant;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.serverdb.util.MediaUtil;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class ThumbnailService extends BaseService {

    static private dellemuse.model.logging.Logger logger = Logger.getLogger(ThumbnailService.class.getName());

    private class ThumbnailFrame {
        public int th_w;
        public int th_h;
        public int img_w;
        public int img_h;
        public int wo;
        public int ho;
        public boolean use_image = false;
    }

    public ThumbnailService(ServerDBSettings settings) {
        super(settings);
    }

    public File create(Long id, File file, ThumbnailSize size) throws IOException {

        if (!MediaUtil.isImage(file.getName()))
            return null;

        ThumbnailFrame frame = getThumbnailFrame(file, size);

        if (frame == null)
            return null;

        File thDir = new File(getSettings().getWorkDir() + File.separator + ServerConstant.THUMBNAIL_BUCKET);
        String ext = FilenameUtils.getExtension(file.getName());
        File fileOut = new File(thDir, String.valueOf(id) + "." + ext);

        if (frame.use_image) {
            Files.copy(file, fileOut);
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

                
                if (size==ThumbnailSize.EYEFISH) {
                
                	int delta_w = 0;
                    int delta_h = (ret.img_h - ret.th_h);

                    if (delta_h < delta_w) {
                        ret.ho = ret.th_h;
                        ret.wo = (ret.img_h > 0) ? ret.img_w * ret.ho / ret.img_h : (int) (ret.th_w);
                    } else {
                        ret.wo = ret.th_w;
                        ret.ho = (ret.img_w > 0) ? ret.img_h * ret.wo / ret.img_w : (int) (ret.th_h);
                    }

                }
                else {
                	
                    int delta_w = (ret.img_w - ret.th_w);
                    int delta_h = (ret.img_h - ret.th_h);

                    if (delta_h < delta_w) {
                        ret.ho = ret.th_h;
                        ret.wo = (ret.img_h > 0) ? ret.img_w * ret.ho / ret.img_h : (int) (ret.th_w);
                    } else {
                        ret.wo = ret.th_w;
                        ret.ho = (ret.img_w > 0) ? ret.img_h * ret.wo / ret.img_w : (int) (ret.th_h);
                    }
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
