package dellemuse.serverdb.model;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface Auditable {

    public OffsetDateTime getCreated();

    public OffsetDateTime getLastModified();

    public User getLastModifiedUser();

}
