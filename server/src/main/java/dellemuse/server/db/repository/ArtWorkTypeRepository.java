package dellemuse.server.db.repository;


import org.springframework.data.repository.CrudRepository;

import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtWorkType;



/**
 * 
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
public interface ArtWorkTypeRepository extends CrudRepository<ArtWorkType, Long> {

}
