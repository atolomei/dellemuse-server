package dellemuse.serverapp.service;

import java.io.File;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import dellemuse.serverapp.ServerDBSettings;

import dellemuse.serverapp.serverdb.service.base.BaseService;

import jakarta.annotation.PostConstruct;

@Service
public class PublicUrlCacheService extends BaseService {

	@JsonIgnore
	private Cache<String, String> cache;

	public PublicUrlCacheService(ServerDBSettings settings) {
		super(settings);
	}

	public long size() {
		return getCache().estimatedSize();
	}

	public String get(Long rid, String type) {
		return getCache().getIfPresent(getKey(rid, type));
	}

	public void put(Long rid, String type, String value) {
		getCache().put(getKey(rid, type), value);
	}

	public void remove(Long rid, String type) {
		getCache().invalidate(getKey(rid, type));
	}

	public boolean contains(Long rid, String type) {
		return getCache().getIfPresent(getKey(rid, type)) != null;
	}

	@PostConstruct
	protected void onInitialize() {
		try {
			this.cache = Caffeine.newBuilder().initialCapacity(1000).maximumSize(100000).expireAfterWrite(10, TimeUnit.DAYS).build();
		} finally {
		}
	}

	private Cache<String, String> getCache() {
		return this.cache;
	}

	private String getKey(Long oid, String type) {
		return oid.toString() + File.separator + type;
	}

}
