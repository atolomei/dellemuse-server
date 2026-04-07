package dellemuse.serverapp.serverdb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dellemuse.serverapp.serverdb.model.ElevenLabsRequest;

@Repository
public interface ElevenLabsRequestRepository extends CrudRepository<ElevenLabsRequest, Long> {

}
