package dellemuse.server.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dellemuse.server.db.model.Institution;


@Repository
public interface InstitutionRepository extends CrudRepository<Institution, Long> {

}
