package dellemuse.serverapp.serverdb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
 
import dellemuse.serverapp.serverdb.model.RoomType;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.SiteRecord;

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
public interface SiteRecordRepository extends CrudRepository<SiteRecord, Long> {

}
