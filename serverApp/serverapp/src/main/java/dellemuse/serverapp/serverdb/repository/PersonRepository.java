package dellemuse.serverapp.serverdb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;

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
public interface PersonRepository extends ListCrudRepository<Person, Long> {

	
	/*
	@EntityGraph(attributePaths = {"artworks"})
	@Query("""
	    select distinct p
	    from Person p
	    join p.artworks aw
	    where aw.site.id = :siteId order by p.sortlastfirstname
	""")
	List<Person> findDistinctPersonsBySiteId(@Param("siteId") Long siteId);
	*/
	
	 /**
	@Query("""
	        SELECT DISTINCT aa.person
	        FROM ArtWork Artist aa
	        JOIN aa.artwork a
	        WHERE a.site.id = :siteId 
	        order by aa.person.sortlastfirstname
	    """)
	    List<Person> findDistinctPersonsBySiteId(@Param("siteId") Long siteId);
	 
	
	@Query("""
	        SELECT aa.artwork
	        FROM ArtWork Artist aa
	        WHERE aa.person.id = :personId 
	        order by aa.artwork.name
	    """)
	    List<ArtWork> findDistinctArtWorkByPersonId(@Param("personId") Long personId);
	
	 
	@Query("""
	        SELECT DISTINCT aa.person
	        FROM ArtWork Artist aa
	        WHERE aa.artwork.id = :artworkId
	        order by aa.person.sortlastfirstname
	    """)
	    List<Person> getArworkArtists(@Param("siteId") Long artworkId);
	**/
	
}


/**


 JOIN aa.person p

public interface SiteArtistsRepository extends JpaRepository<Person, Long> {

	@Query("""
	        SELECT DISTINCT aa.person
	        FROM Artwork Artist aa
	        JOIN aa.artwork a
	        WHERE a.site.id = :siteId
	    """)
	    List<Person> findDistinctPersonsBySiteId(@Param("siteId") Long siteId);
	
	
}
**/
