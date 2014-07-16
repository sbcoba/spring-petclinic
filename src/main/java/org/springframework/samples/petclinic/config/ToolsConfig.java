package org.springframework.samples.petclinic.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.samples.petclinic.util.CallMonitoringAspect;

@EnableAspectJAutoProxy
@EnableCaching
@EnableMBeanExport
@Configuration
public class ToolsConfig implements CachingConfigurer{

    /**
     * Call monitoring aspect that monitors call count and call invocation time
     * @return
     */
    @Bean
    public CallMonitoringAspect callMonitor() {
        return new CallMonitoringAspect();
    }

    @Override
    public CacheManager cacheManager() {
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
        ehCacheCacheManager.setCacheManager(ehcache().getObject());
        return ehCacheCacheManager;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return null;
    }

    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("cache/ehcache.xml"));
        return factoryBean;
    }
}
