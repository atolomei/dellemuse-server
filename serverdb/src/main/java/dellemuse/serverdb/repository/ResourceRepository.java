package dellemuse.serverdb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dellemuse.serverdb.model.ArtExhibition;
import dellemuse.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverdb.model.ArtWork;
import dellemuse.serverdb.model.ArtWorkArtist;
import dellemuse.serverdb.model.Floor;
import dellemuse.serverdb.model.FloorType;
import dellemuse.serverdb.model.GuideContent;
import dellemuse.serverdb.model.InstitutionType;
import dellemuse.serverdb.model.InstitutionalContent;
import dellemuse.serverdb.model.Person;
import dellemuse.serverdb.model.Resource;

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
@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long> {

}
