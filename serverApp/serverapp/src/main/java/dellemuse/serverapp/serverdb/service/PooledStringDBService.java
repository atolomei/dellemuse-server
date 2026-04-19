package dellemuse.serverapp.serverdb.service;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.PooledString;
import jakarta.transaction.Transactional;

@Service
public class PooledStringDBService extends BaseDBService<PooledString, Long> {

	static private Logger logger = Logger.getLogger(PooledStringDBService.class.getName());

	public PooledStringDBService(CrudRepository<PooledString, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	@Transactional
	public <S extends PooledString> S save(S entity) {
		return getRepository().save(entity);
	}

	@Transactional
	public Optional<PooledString> findById(Long id) {
		return getRepository().findById(id);
	}

	@Transactional
	public boolean existsById(Long id) {
		return getRepository().existsById(id);
	}

	@Transactional
	public Iterable<PooledString> findAll() {
		return getRepository().findAll();
	}

	@Transactional
	public void delete(PooledString o) {
		getRepository().delete(o);
	}

	@Override
	protected Class<PooledString> getEntityClass() {
		return PooledString.class;
	}
}
