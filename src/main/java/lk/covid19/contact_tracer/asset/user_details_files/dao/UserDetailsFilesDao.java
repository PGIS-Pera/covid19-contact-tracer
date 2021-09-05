package lk.covid19.contact_tracer.asset.user_details_files.dao;


import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.asset.user_details_files.entity.UserDetailsFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsFilesDao extends JpaRepository< UserDetailsFiles, Integer > {
  UserDetailsFiles findByUserDetails(UserDetails userDetails);

  UserDetailsFiles findByName(String filename);

  UserDetailsFiles findByNewName(String filename);

  UserDetailsFiles findByNewId(String filename);
}
