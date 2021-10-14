package lk.covid19.contact_tracer.asset.user_details_files.service.impl;

import lk.covid19.contact_tracer.asset.common_asset.model.FileInfo;
import lk.covid19.contact_tracer.asset.user_details.controller.UserDetailsController;
import lk.covid19.contact_tracer.asset.user_details_files.dao.UserDetailsFilesDao;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.asset.user_details_files.entity.UserDetailsFiles;
import lk.covid19.contact_tracer.asset.user_details_files.service.UserDetailsFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;

@Service
//@CacheConfig( cacheNames = "employeeFiles" )
@RequiredArgsConstructor
public class UserDetailsFilesServiceImpl implements UserDetailsFilesService {
  private final UserDetailsFilesDao userDetailsFilesDao;


  public UserDetailsFiles findByName(String filename) {
    return userDetailsFilesDao.findByName(filename);
  }

  public void persist(UserDetailsFiles userDetailsFiles) {
    userDetailsFilesDao.save(userDetailsFiles);
  }

  public List< UserDetailsFiles > search(UserDetailsFiles userDetailsFiles) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< UserDetailsFiles > employeeFilesExample = Example.of(userDetailsFiles, matcher);
    return userDetailsFilesDao.findAll(employeeFilesExample);
  }

  public UserDetailsFiles findById(Integer id) {
    return userDetailsFilesDao.getById(id);
  }

  public UserDetailsFiles findByNewID(String filename) {
    return userDetailsFilesDao.findByNewId(filename);
  }

  public UserDetailsFiles findByUserDetails(UserDetails userDetails) {
    return userDetailsFilesDao.findByUserDetails(userDetails);
  }

  //@Cacheable
  public FileInfo userDetailsFileDownloadLinks(UserDetails userDetails) {
    UserDetailsFiles userDetailsFiles = userDetailsFilesDao.findByUserDetails(userDetails);
    if ( userDetailsFiles != null ) {
      String filename = userDetailsFiles.getName();
      String url = MvcUriComponentsBuilder
          .fromMethodName(UserDetailsController.class, "downloadFile", userDetailsFiles.getNewId())
          .build()
          .toString();
      return new FileInfo(filename, userDetailsFiles.getCreatedAt(), url);
    }
    return null;
  }
}

