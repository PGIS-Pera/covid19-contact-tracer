package lk.covid19.contact_tracer.asset.user_details.service;


import lk.covid19.contact_tracer.asset.common_asset.model.FileInfo;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetailsFiles;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailsFilesService {

  UserDetailsFiles findByName(String filename);

  void persist(UserDetailsFiles storedFile);

  List< UserDetailsFiles > search(UserDetailsFiles userDetailsFiles);

  UserDetailsFiles findById(Integer id);

  UserDetailsFiles findByNewID(String filename);

  UserDetailsFiles findByUserDetails(UserDetails userDetails);

  FileInfo userDetailsFileDownloadLinks(UserDetails userDetails);
}
