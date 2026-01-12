package dellemuse.serverapp.serverdb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dellemuse.serverapp.serverdb.model.SiteArtist;
import dellemuse.serverapp.serverdb.model.Person;

/**
 * <S extends T> S save(S entity); <S extends T> Iterable<S> saveAll(Iterable<S>
 * entities); Optional<T> findById(ID id); boolean existsById(ID id);
 * Iterable<T> findAll(); Iterable<T> findAllById(Iterable<ID> ids); long
 * count(); void deleteById(ID id); void delete(T entity); void
 * deleteAllById(Iterable<? extends ID> ids); void deleteAll(Iterable<? extends
 * T> entities); void deleteAll();
 * 
 */
@Repository
public interface SiteArtistRepository extends ListCrudRepository<SiteArtist, Long> {
	
	
	
	 
	
	
}
