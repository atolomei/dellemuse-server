package dellemuse.serverapp.serverdb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dellemuse.serverapp.serverdb.model.RoomRecord;

@Repository
public interface RoomRecordRepository extends CrudRepository<RoomRecord, Long> {

}
