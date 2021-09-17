package lk.covid19.contact_tracer.asset.news_subscription.service.impl;

import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;

import lk.covid19.contact_tracer.asset.news_subscription.dao.NewsDao;
import lk.covid19.contact_tracer.asset.news_subscription.entity.News;
import lk.covid19.contact_tracer.asset.news_subscription.service.NewsService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CacheConfig( cacheNames = "news" )
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
  private final NewsDao newsDao;
  private final CommonService commonService;

  @Cacheable
  public List< News > findAll() {
    return newsDao.findAll();
  }

  @Cacheable
  public News findById(Integer id) {
    return newsDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "news", allEntries = true )},
      put = {@CachePut( value = "news", key = "#news.id" )} )
  public News persist(News news) {
    return newsDao.save(news);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    newsDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< News > search(News news) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< News > newsExample = Example.of(news, matcher);
    return newsDao.findAll(newsExample);
  }


  @Cacheable
  public List< News > findByGramaNiladhari(GramaNiladhari gramaNiladhari) {
    return newsDao.findByGramaNiladhari(gramaNiladhari);
  }

}

