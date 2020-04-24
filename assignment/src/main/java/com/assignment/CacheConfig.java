package com.assignment;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.config.CacheConfiguration;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport{
	@Bean
	public net.sf.ehcache.CacheManager ehCacheManager(){
		CacheConfiguration firstLevelCache=new CacheConfiguration();
		firstLevelCache.setName("first-level-cache");
		firstLevelCache.setMemoryStoreEvictionPolicy("LRU");
		firstLevelCache.maxEntriesLocalHeap(1000);
		firstLevelCache.timeToLiveSeconds(300);
		CacheConfiguration secondLevelCache=new CacheConfiguration();
		secondLevelCache.setName("second-level-cache");
		secondLevelCache.setMemoryStoreEvictionPolicy("LRU");
		secondLevelCache.maxEntriesLocalHeap(1000);
		secondLevelCache.timeToLiveSeconds(0);
		
		
		net.sf.ehcache.config.Configuration config=new net.sf.ehcache.config.Configuration();
		config.addCache(firstLevelCache);
		config.addCache(secondLevelCache);
		return net.sf.ehcache.CacheManager.newInstance(config);
	}
	@Override
	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheManager());
	}
}