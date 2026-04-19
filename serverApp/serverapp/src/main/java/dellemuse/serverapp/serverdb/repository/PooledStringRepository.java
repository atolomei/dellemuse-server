package dellemuse.serverapp.serverdb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dellemuse.serverapp.serverdb.model.PooledString;

@Repository
public interface PooledStringRepository extends CrudRepository<PooledString, Long> {

}
