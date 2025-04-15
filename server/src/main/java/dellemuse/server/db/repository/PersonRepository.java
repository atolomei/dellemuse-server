package dellemuse.server.db.repository;

import org.springframework.data.repository.CrudRepository;

import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.Floor;
import dellemuse.server.db.model.FloorType;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.db.model.InstitutionType;
import dellemuse.server.db.model.InstitutionalContent;
import dellemuse.server.db.model.Person;

/**
 *  <S extends T> S save(S entity);
 *  <S extends T> Iterable<S> saveAll(Iterable<S> entities);
 *  Optional<T> findById(ID id);
 *  boolean existsById(ID id);
 *  Iterable<T> findAll();
 *  Iterable<T> findAllById(Iterable<ID> ids);
 *  long count();
 *  void deleteById(ID id);
 *  void delete(T entity);
 *  void deleteAllById(Iterable<? extends ID> ids);
 *  void deleteAll(Iterable<? extends T> entities);
 *  void deleteAll();
 * 
 */
public interface PersonRepository extends CrudRepository<Person, Long> {

}
