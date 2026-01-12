package dellemuse.serverapp.serverdb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;


@Repository
public interface ArtExhibitionItemRecordRepository extends CrudRepository<ArtExhibitionItemRecord, Long> {

}
