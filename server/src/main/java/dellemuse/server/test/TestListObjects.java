package dellemuse.server.test;

import java.lang.invoke.ConstantBootstraps;
import java.util.Iterator;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.Constant;
import dellemuse.model.logging.Logger;
import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
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
import dellemuse.server.model.ModelService;

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
            Tester <Person, PersonDBService> tester = new Tester<Person, PersonDBService>(Person.class, PersonDBService.class);
            tester.test();
        }

       

        
    
        {
            Tester <Floor, FloorDBService> tester = new Tester<Floor , FloorDBService>(Floor.class, FloorDBService.class);
            tester.test();
        }



        
        
        {
            Tester <GuideContent, GuideContentDBService> tester = new Tester<GuideContent, GuideContentDBService>(GuideContent.class, GuideContentDBService.class);
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
            Tester <Site, SiteDBService> tester = new Tester<Site, SiteDBService>(Site.class, SiteDBService.class);
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

        {
            Tester <ArtWork, ArtWorkDBService> tester = new Tester<ArtWork, ArtWorkDBService>(ArtWork.class, ArtWorkDBService.class);
            tester.test();
        }

        {
            Tester <Institution, InstitutionDBService> tester = new Tester<Institution, InstitutionDBService>(Institution.class, InstitutionDBService.class);
            tester.test();
        }
        
        logger.debug("done");
        
        
    }

    
    private class TesterModel<T extends DelleMuseObject, K extends ModelService<?,?>> {
        
        final Class<T> typeParameterClass;
        final Class<K> modelServiceParameterClass;
        
        public TesterModel(Class<T> typeParameterClass, Class<K> modelServiceParameterClass)
        {
            this.typeParameterClass = typeParameterClass;
            this.modelServiceParameterClass = modelServiceParameterClass;
        }
        
        public void test() {
            
            
            
            
        }
        
        
        
    }
    
    
    

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
