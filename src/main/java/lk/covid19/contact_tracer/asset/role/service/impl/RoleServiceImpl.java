package lk.covid19.contact_tracer.asset.role.service.impl;

import lk.covid19.contact_tracer.asset.role.dao.RoleDao;
import lk.covid19.contact_tracer.asset.role.entity.Role;
import lk.covid19.contact_tracer.asset.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@CacheConfig( cacheNames = {"role"} )
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
  private final RoleDao roleDao;

  //@Cacheable
  public List< Role > findAll() {
    return roleDao.findAll();
  }

  //@Cacheable
  public Role findById(Integer id) {
    return roleDao.getById(id);
  }


  //@Caching( evict = {//@CacheEvict( value = "role", allEntries = true )},      put = {@CachePut( value = "role", key = "#role.id" )} )
  public Role persist(Role role) {
    role.setRoleName(role.getRoleName().toUpperCase());
    return roleDao.save(role);
  }

  //@CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    roleDao.deleteById(id);
    return true;
  }

  //@Cacheable
  public List< Role > search(Role role) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Role > roleExample = Example.of(role, matcher);
    return roleDao.findAll(roleExample);
  }

  //@Cacheable
  public Role findByRoleName(String roleName) {
    return roleDao.findByRoleName(roleName);
  }
}
