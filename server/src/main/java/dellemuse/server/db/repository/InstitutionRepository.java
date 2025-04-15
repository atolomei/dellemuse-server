package dellemuse.server.db.repository;

import org.springframework.data.repository.CrudRepository;

import dellemuse.server.db.model.Institution;

public interface InstitutionRepository extends CrudRepository<Institution, Long> {

}
