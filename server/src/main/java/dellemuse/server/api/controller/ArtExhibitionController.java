package dellemuse.server.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.ArtExhibitionGuideModelService;
import dellemuse.server.api.model.ArtExhibitionModelService;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.service.ArtExhibitionDBService;
import dellemuse.server.error.ObjectNotFoundException;
import dellemuse.server.security.SecurityService;

@RestController
@RequestMapping(value = "/artexhibition")
public class ArtExhibitionController extends BaseController<ArtExhibition, ArtExhibitionModel> {

    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(ArtExhibitionController.class.getName());

    @JsonIgnore
    @Autowired
    private final ArtExhibitionDBService dbService;

    @JsonIgnore
    @Autowired
    private final ArtExhibitionModelService modelService;

    
    @JsonIgnore
    @Autowired
    private final ArtExhibitionGuideModelService guideModelService;
    
    
    @JsonIgnore
    @Autowired
    private final SecurityService securityService;

    public ArtExhibitionController(ArtExhibitionDBService db, ArtExhibitionModelService modelService, SecurityService securityService, ArtExhibitionGuideModelService guideModelService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = db;
        this.securityService=securityService;
        this.guideModelService=guideModelService;
    
    }
    
    @RequestMapping(value = "/artexhibitionguides/{artexhibitionid}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<List<ArtExhibitionGuideModel>> getGuides(@PathVariable("artexhibitionid") Long id) {

        Optional<ArtExhibition> o_ex = this.getDBService().getRepository().findById(id);
        
        if (o_ex.isEmpty())
            throw new ObjectNotFoundException(id.toString());
            
        List<ArtExhibitionGuide> list = this.getDBService().getArtExhibitionGuides(o_ex.get());
        
        List<ArtExhibitionGuideModel> m_list = new ArrayList<ArtExhibitionGuideModel>();
        
        if (m_list!=null) {
            list.forEach(i->m_list.add(guideModelService.model(i)));
        }
        
        
        
        return new ResponseEntity<List<ArtExhibitionGuideModel>>(m_list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getbyname/{name}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<Optional<ArtExhibitionModel>> get(@PathVariable("name") String name) {
        List<ArtExhibition> list = this.getDBService().getByName(name);
        if (list == null || list.isEmpty())
            return new ResponseEntity<Optional<ArtExhibitionModel>>(Optional.empty(), HttpStatus.OK);
        return new ResponseEntity<Optional<ArtExhibitionModel>>(Optional.of(this.getModelService().model(list.get(0))), HttpStatus.OK);
    }

    @Override
    public ArtExhibitionDBService getDBService() {
        return dbService;
    }

    public ArtExhibitionModelService getModelService() {
        return modelService;
    }
    
    protected SecurityService getSecurityService() {
        return securityService;
    }
}

