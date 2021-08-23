package lk.covid19.contact_tracer.asset.user.service;

import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;

import java.util.List;

public interface UserService {

  List< User > findAll();

  User findById(Integer id);

  User persist(User user);

  boolean delete(Integer id);

  List< User > search(User user);

  Integer findByUserIdByUserName(String userName);

  User findByUserName(String name);

  User findUserByEmployee(UserDetails userDetails);

  boolean findByEmployee(UserDetails userDetails);
}
