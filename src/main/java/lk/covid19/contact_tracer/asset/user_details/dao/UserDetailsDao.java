package lk.covid19.contact_tracer.asset.user_details.dao;


import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsDao extends JpaRepository< UserDetails, Integer > {
  UserDetails findFirstByOrderByIdDesc();

  UserDetails findByNic(String nic);

  UserDetails findByEmail(String email);
}
