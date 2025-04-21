package dellemuse.server.db.model;

import java.time.OffsetDateTime;
import java.util.Optional;


public interface Auditable {
	
	public OffsetDateTime getCreated();
	public OffsetDateTime getLastModified();
	public User getLastModifiedUser();
	public Optional<Long> getLastModifiedUserId();

}
