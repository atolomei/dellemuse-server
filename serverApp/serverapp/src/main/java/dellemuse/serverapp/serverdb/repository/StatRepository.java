package dellemuse.serverapp.serverdb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dellemuse.serverapp.serverdb.model.stat.Stat;

@Repository
public interface StatRepository extends CrudRepository<Stat, Long> {

}
