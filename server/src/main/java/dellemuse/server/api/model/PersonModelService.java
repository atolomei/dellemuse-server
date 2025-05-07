package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.PersonModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Person;
import dellemuse.server.error.InternalErrorException;

@Service
public class PersonModelService extends ModelService<Person, PersonModel> {

    public PersonModelService(Settings settings) {
        super(settings, Person.class, PersonModel.class);
    }

    @Override
    public PersonModel model(Person person) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(person);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        PersonModel model;
        try {
            model = (PersonModel) getObjectMapper().readValue(json, PersonModel.class);
            return model;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person source(PersonModel model) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e, "write");
        }

        try {
            return (Person) getObjectMapper().readValue(json, Person.class);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e, "read");
        }

    }

}
