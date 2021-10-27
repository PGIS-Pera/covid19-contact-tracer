package lk.covid19.contact_tracer.asset.user_details.service;

import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;


import java.util.List;

public interface UsersDetailsService {
  List< UserDetails > findAll();

  UserDetails findById(Integer id);

  UserDetails persist(UserDetails userDetails);

  boolean delete(Integer id);

  List< UserDetails > search(UserDetails userDetails);

  UserDetails findByNic(String nic);

  UserDetails findLastUserDetails();

  UserDetails findByEmail(String email);
}
