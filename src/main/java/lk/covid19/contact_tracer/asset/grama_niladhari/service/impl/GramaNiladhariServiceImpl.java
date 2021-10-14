package lk.covid19.contact_tracer.asset.grama_niladhari.service.impl;


import lk.covid19.contact_tracer.asset.grama_niladhari.dao.GramaNiladhariDao;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@CacheConfig( cacheNames = "gramaNiladhari" )
@RequiredArgsConstructor
public class GramaNiladhariServiceImpl implements GramaNiladhariService {
  private final GramaNiladhariDao gramaNiladhariDao;

  //@Cacheable
  public List< GramaNiladhari > findAll() {
    return gramaNiladhariDao.findAll();
  }

  //@Cacheable
  public GramaNiladhari findById(Integer id) {
    return gramaNiladhariDao.getById(id);
  }

  //@Caching( evict = {//@CacheEvict( value = "gramaNiladhari", allEntries = true )},      put = {@CachePut( value = "gramaNiladhari", key = "#gramaNiladhari.id" )} )
  public GramaNiladhari persist(GramaNiladhari gramaNiladhari) {
    return gramaNiladhariDao.save(gramaNiladhari);
  }

  //@CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    gramaNiladhariDao.deleteById(id);
    return false;
  }

  //@Cacheable
  public List< GramaNiladhari > search(GramaNiladhari gramaNiladhari) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< GramaNiladhari > gramaNiladhariExample = Example.of(gramaNiladhari, matcher);
    return gramaNiladhariDao.findAll(gramaNiladhariExample);
  }

  //@Cacheable
  public Page< GramaNiladhari > findAllPageable(Pageable pageable) {
    return gramaNiladhariDao.findAll(pageable);
  }

}
