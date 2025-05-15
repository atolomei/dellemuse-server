package dellemuse.server.test;

import java.lang.invoke.ConstantBootstraps;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.PersonModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Constant;
import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.api.model.ModelService;
import dellemuse.server.api.model.PersonModelService;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtExhibitionStatusType;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.DelleMuseObject;
import dellemuse.server.db.model.Floor;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.InstitutionalContent;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Room;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.model.User;
import dellemuse.server.db.service.ArtExhibitionDBService;
import dellemuse.server.db.service.ArtExhibitionStatusTypeDBService;
import dellemuse.server.db.service.ArtWorkDBService;
import dellemuse.server.db.service.ArtWorkTypeDBService;
import dellemuse.server.db.service.DBService;
import dellemuse.server.db.service.FloorDBService;
import dellemuse.server.db.service.GuideContentDBService;
import dellemuse.server.db.service.InstitutionDBService;
import dellemuse.server.db.service.InstitutionalContentDBService;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.RoomDBService;
import dellemuse.server.db.service.SiteDBService;
import dellemuse.server.db.service.UserDBService;

@Service
public class TestListObjects extends BaseService  implements ApplicationContextAware {
                
    static private Logger logger = Logger.getLogger(TestListObjects.class.getName());
    
    @JsonIgnore
    private ApplicationContext applicationContext;
    
    public TestListObjects(Settings settings) {
        super(settings);
    }
    
    
    public void test() {
      
   
        {
            Tester <GuideContent, GuideContentDBService> tester = new Tester<GuideContent, GuideContentDBService>(GuideContent.class, GuideContentDBService.class);
            tester.test();
        }

        
        {
            Tester <ArtWork, ArtWorkDBService> tester = new Tester<ArtWork, ArtWorkDBService>(ArtWork.class, ArtWorkDBService.class);
            tester.test();
        }
        
        {
            Tester <Institution, InstitutionDBService> tester = new Tester<Institution, InstitutionDBService>(Institution.class, InstitutionDBService.class);
            tester.test();
        }

        {   
            Tester <Site, SiteDBService> tester = new Tester<Site, SiteDBService>(Site.class, SiteDBService.class);
            tester.test();
        }

        
        
        {
            Tester <Person, PersonDBService> tester = new Tester<Person, PersonDBService>(Person.class, PersonDBService.class);
            tester.test();
        
            TesterModel<Person, PersonDBService, PersonModel, PersonModelService> testerModel = 
                    new TesterModel(    Person.class, 
                                        PersonDBService.class,
                                        PersonModel.class,
                                        PersonModelService.class);
            
            testerModel.test();
        }
    
        {
            Tester <Floor, FloorDBService> tester = new Tester<Floor , FloorDBService>(Floor.class, FloorDBService.class);
            tester.test();
        }

   
        {
            Tester <InstitutionalContent, InstitutionalContentDBService> tester = new Tester<InstitutionalContent, InstitutionalContentDBService>(InstitutionalContent.class, InstitutionalContentDBService.class);
            tester.test();
        }
        
        {
            Tester <Room, RoomDBService> tester = new Tester<Room, RoomDBService>(Room.class, RoomDBService.class);
            tester.test();
        }

        {   
            Tester <User, UserDBService> tester = new Tester<User, UserDBService>(User.class, UserDBService.class);
            tester.test();
        }

        
        {
            Tester <ArtWorkType, ArtWorkTypeDBService> tester = new Tester<ArtWorkType, ArtWorkTypeDBService>(ArtWorkType.class, ArtWorkTypeDBService.class);
            tester.test();
        }

        
        {
            Tester <ArtExhibition, ArtExhibitionDBService> tester = new Tester<ArtExhibition, ArtExhibitionDBService>(ArtExhibition.class, ArtExhibitionDBService.class);
            tester.test();
        }


        {
            Tester <ArtExhibitionStatusType, ArtExhibitionStatusTypeDBService> tester = new Tester<ArtExhibitionStatusType, ArtExhibitionStatusTypeDBService>(ArtExhibitionStatusType.class, ArtExhibitionStatusTypeDBService.class);
            tester.test();
        }

        

        
        testArtExhibitionGuide();
        testArtExhibitionGuideBySite();

        
        
        

        
        logger.debug("done");
        
        
    }

    private void testArtExhibitionGuideBySite() {
        

        logger.debug(ArtExhibitionGuide.class.getName() + " per Site ");

        
        SiteDBService site_service          = getApplicationContext().getBean(SiteDBService.class);
        ArtExhibitionDBService a_service    = getApplicationContext().getBean(ArtExhibitionDBService.class);
        
        
        Iterable<Site> a = site_service.findAll();
        
        logger.debug(Constant.SEPARATOR);
        
        if (a==null)
            return;
                
            Iterator<Site> it = a.iterator();

            while (it.hasNext()) {
                
                    Site item = it.next();
                    
                    List<ArtExhibition> list = site_service.getArtExhibitions(item);
                    
                    
                    for ( ArtExhibition ae: list) {
                        
                        List<ArtExhibitionGuide> guides = a_service.getArtExhibitionGuides(ae);
 
                        guides.forEach( guide -> 
                        { 
                            logger.debug(guide.toString());  
                        });
                    }
            }
        
        logger.debug(Constant.SEPARATOR);
        
    }
    

    
    
    private void testArtExhibitionGuide() {

        logger.debug(ArtExhibitionGuide.class.getName());

        ArtExhibitionDBService service = getApplicationContext().getBean(ArtExhibitionDBService.class);
        
        
        Iterable<ArtExhibition> a = service.findAll();
        
        
        logger.debug(Constant.SEPARATOR);
        
        if (a==null)
            return;
                
            Iterator<ArtExhibition> it = a.iterator();

            while (it.hasNext()) {

                
                ArtExhibition item = it.next();
                    
                    
                    List<ArtExhibitionGuide> list = service.getArtExhibitionGuides(item);
                    list.forEach( guide -> 
                    { 
                        logger.debug(guide.toString());  
                    });

            }
        
        logger.debug(Constant.SEPARATOR);
        

        
        
        
        
        
    }
    
    
    
    private class TesterModel<  T   extends DelleMuseObject, 
                                DB  extends DBService<T,?>, 
                                M   extends DelleMuseModelObject,  
                                MS  extends ModelService<T,M>> {
        
        final Class<T>   objectParameterClass;
        final Class<DB>  dbServiceParameterClass;
        final Class<M>   modelParameterClass;
        final Class<MS>  modelServiceParameterClass;
        
        
        public TesterModel( Class<T>  objectParameterClass, 
                            Class<DB> dbServiceParameterClass, 
                            Class<M>  modelParameterClass,
                            Class<MS> modelServiceParameterClass)
        {

            this.objectParameterClass = objectParameterClass;
            this.dbServiceParameterClass = dbServiceParameterClass;
            this.modelParameterClass =  modelParameterClass;
            this.modelServiceParameterClass=modelServiceParameterClass;
        }
        
        public void test() {

            ModelService<T,?> modelService = getApplicationContext().getBean(modelServiceParameterClass);
            
            if (modelService==null)
                return;

            DBService<?,?> service = getApplicationContext().getBean(dbServiceParameterClass);

            Iterable<?> a = service.findAll();
            
            int counter =  0;
            
            logger.debug(Constant.SEPARATOR);
            logger.debug("Object: " + this.objectParameterClass.getSimpleName() +  " | Model: " + this.modelParameterClass.getSimpleName());
            logger.debug(Constant.SEPARATOR);
            
            if (a==null)
                return;
                    
                Iterator<?> it = a.iterator();

                while (it.hasNext()) {
                        DelleMuseObject item = (DelleMuseObject) it.next();
                        logger.debug(String.valueOf(counter++) + ". " + item.toString());
                        
                        @SuppressWarnings("unchecked")
                        DelleMuseModelObject model = modelService.model((T) item);
                        logger.debug(String.valueOf(counter) + ". " + model.toString());
                        
                        logger.debug("");
                }
            
            logger.debug(Constant.SEPARATOR);
            
        }
        
    }
    
    
    
    /**
     * DB Test
     * 
     * @param <T>
     * @param <K>
     */
    private class Tester<T extends DelleMuseObject, K extends DBService<?,?>> 
     {
     
        final Class<T> typeParameterClass;
        final Class<K> dbServiceParameterClass;
        
        public Tester(Class<T> typeParameterClass, Class<K> dbServiceParameterClass)
        {
            this.typeParameterClass = typeParameterClass;
            this.dbServiceParameterClass = dbServiceParameterClass;
        }
        
        public void test() {
            
            DBService<?,?> service = getApplicationContext().getBean(dbServiceParameterClass);
            
            Iterable<?> a = service.findAll();
            
            int counter =  0;
            logger.debug(this.typeParameterClass.getSimpleName() + " " + this.dbServiceParameterClass.getSimpleName());
            
            if (a!=null)  {
                Iterator<?> it = a.iterator();
                while (it.hasNext()) {
                    DelleMuseObject item = (DelleMuseObject) it.next();
                    
                    logger.debug(String.valueOf(counter++) + ". " + item.toString());
                }
            }
            logger.debug(Constant.SEPARATOR);
        }
        
    }
    


    
    
    
    
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }



}
