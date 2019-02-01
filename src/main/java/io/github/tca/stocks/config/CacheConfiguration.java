package io.github.tca.stocks.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(io.github.tca.stocks.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.domain.Product.class.getName(), jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.domain.Product.class.getName() + ".assets", jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.domain.ProductSku.class.getName(), jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.domain.ProductAsset.class.getName(), jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.domain.StockOrder.class.getName(), jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.domain.StockOrder.class.getName() + ".items", jcacheConfiguration);
            cm.createCache(io.github.tca.stocks.domain.StockItem.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
