package dellemuse.server.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.PersonModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Person;

@Service
public class PersonModelService extends ModelService<Person, PersonModel> {

    public PersonModelService(Settings settings) {
        super(settings);
    }

    @Override
    public PersonModel getModel(Person person) {
        String json = null;
        
        try {
            json = getObjectMapper().writeValueAsString(person);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        
        PersonModel model;
        try {
            model = (PersonModel) getObjectMapper().readValue(json, dellemuse.model.PersonModel.class);
            return model;
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person getSource(PersonModel model) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        
        Person person;
        try {
            person = (Person) getObjectMapper().readValue(json, Person.class);
            return person;
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } 
        
        
    }

}
