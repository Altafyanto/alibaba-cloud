package com.springframework.gateway.service.impl;

import com.springframework.enums.rediskey.CacheNamePrefixEnum;
import com.springframework.gateway.domain.po.RouteConfigDO;
import com.springframework.gateway.event.CacheExpireFailEvent;
import com.springframework.gateway.service.RouteConfigCacheService;
import com.springframework.redis.GenericCacheRedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author summer
 * 2018/11/28
 */
@Service("routeConfigCacheService")
public class RouteConfigCacheServiceImpl extends GenericCacheRedisManager implements RouteConfigCacheService {
    private static final String CACHE_PREFIX = CacheNamePrefixEnum.GATEWAY.getValue();
    private ApplicationEventPublisher publisher;

    @Autowired
    public RouteConfigCacheServiceImpl(RedisTemplate redisTemplate, ApplicationEventPublisher publisher) {
        super(redisTemplate);
        this.publisher = publisher;
    }

    /**
     * 保存缓存
     *
     * @param routeConfig
     */
    @Override
    public void saveRouteConfigCache(RouteConfigDO routeConfig) {
        this.hset(CACHE_PREFIX, routeConfig.getServiceId(), routeConfig, DEFAULT_CACHE_TIME);
    }

    /**
     * 缓存查询
     *
     * @param serviceId
     * @return
     */
    @Override
    public RouteConfigDO findRouteConfigCache(String serviceId) {
        return (RouteConfigDO) this.hget(CACHE_PREFIX, serviceId);
    }

    /**
     * 缓存过期
     *
     * @param routeId
     */
    @Override
    public void expireRouteConfigCache(String routeId) {
        final Long hdelNum = this.hdel(CACHE_PREFIX, routeId);
        //缓存失败的发送失败事件，异步重试
        if (hdelNum <= 0) {
            publisher.publishEvent(new CacheExpireFailEvent(this, CACHE_PREFIX, routeId));
        }
    }
}
