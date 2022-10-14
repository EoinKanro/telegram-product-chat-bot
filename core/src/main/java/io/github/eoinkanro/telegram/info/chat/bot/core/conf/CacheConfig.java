package io.github.eoinkanro.telegram.info.chat.bot.core.conf;

import io.github.eoinkanro.telegram.info.chat.bot.core.model.data.ConfigPaths;
import io.github.eoinkanro.telegram.info.chat.bot.core.model.data.cache.UserKeyboardInfo;
import io.github.eoinkanro.telegram.info.chat.bot.core.service.UserKeyboardService;
import io.github.eoinkanro.telegram.info.chat.bot.core.utils.FileUtils;
import java.time.Duration;
import javax.cache.CacheManager;
import javax.cache.Caching;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  @Value("${ehcache.cacheDataSize:512}")
  private Long cacheDataSize;

  @Value("${ehcache.cacheDataExpiry:1}")
  private Long cacheDataExpiry;

  @Bean(destroyMethod = "close")
  public CacheManager ehcacheManagerBean(FileUtils fileUtils) {
    CacheConfiguration<String, UserKeyboardInfo> cacheConfig = CacheConfigurationBuilder
        .newCacheConfigurationBuilder(String.class,
            UserKeyboardInfo.class,
            ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(cacheDataSize, MemoryUnit.MB)
                //TODO for statistics
                //.disk(1000, MemoryUnit.MB, true)
                .build())
        .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofDays(cacheDataExpiry)))
        .build();

    PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerBuilder.persistence(fileUtils.getConfFile(ConfigPaths.CACHE_FOLDER.getPath())))
        .withCache(UserKeyboardService.CACHE_NAME, cacheConfig)
        .build(false);

    EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
    return provider.getCacheManager(provider.getDefaultURI(), persistentCacheManager.getRuntimeConfiguration());
  }

}
