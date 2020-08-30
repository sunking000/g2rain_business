package com.g2rain.business.gateway.route;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.g2rain.business.gateway.route.mapper.RouteDefinitionMapper;
import com.g2rain.business.gateway.route.po.RouteDefinitionPo;
import com.g2rain.business.gateway.utils.DateFormatUtil;

import reactor.core.publisher.Mono;

@Service
public class RouteDefinitionReader implements ApplicationEventPublisherAware {
	private static final Logger log = LoggerFactory.getLogger(RouteDefinitionReader.class);

	private RouteDefinitionRepository routeDefinitionRepository;

	@Autowired
	private RouteDefinitionMapper routeDefinitionMapper;

	private ApplicationEventPublisher applicationEventPublisher;

	private Map<String, RouteDefinitionDomain> routeDefinitionDomainMap;

	@Autowired
	public RouteDefinitionReader(RouteDefinitionRepository routeDefinitionRepository) {
		this.routeDefinitionRepository = routeDefinitionRepository;
		this.routeDefinitionDomainMap = new HashMap<>();
	}

	@Scheduled(fixedRate = 600000L)
	public void fixRateInit() {
		log.info("{}", DateFormatUtil.format(new Date()));
		List<RouteDefinitionPo> list = this.routeDefinitionMapper.selectAll();
		Set<String> dbRouteDefinitionIdSet = new HashSet<>();
		if (CollectionUtils.isNotEmpty(list))
			list.forEach(item -> {
				dbRouteDefinitionIdSet.add(item.getRouteDefinitionId());
				RouteDefinitionDomain routeDefinitionDomain = this.routeDefinitionDomainMap
						.get(item.getRouteDefinitionId());
				if (routeDefinitionDomain == null
						|| item.getUpdateTime().after(routeDefinitionDomain.getUpdateTime())) {
					RouteDefinitionDomain domainItem = new RouteDefinitionDomain(item);
					RouteDefinition routeDefinition = domainItem.toRouteDefinition();
					this.routeDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
					this.routeDefinitionDomainMap.put(domainItem.getId(), domainItem);
					this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
				}
			});
		Set<String> keySet = this.routeDefinitionDomainMap.keySet();
		Set<String> damainSet = new HashSet<>(keySet);
		damainSet.removeAll(dbRouteDefinitionIdSet);
		if (CollectionUtils.isNotEmpty(damainSet))
			for (String item : damainSet) {
				this.routeDefinitionRepository.delete(Mono.just(item)).subscribe();
				this.routeDefinitionDomainMap.remove(item);
				this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
			}
	}

	@PostConstruct
	public void init() {
		List<RouteDefinitionPo> list = this.routeDefinitionMapper.selectAll();
		if (CollectionUtils.isNotEmpty(list))
			for (RouteDefinitionPo item : list) {
				RouteDefinitionDomain domainItem = new RouteDefinitionDomain(item);
				RouteDefinition routeDefinition = domainItem.toRouteDefinition();
				this.routeDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
				this.routeDefinitionDomainMap.put(domainItem.getId(), domainItem);
			}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}
