package lk.covid19.contact_tracer.asset.user.dao;

import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository< User, Integer > {

  @Query( value = "select id from User where user_details_id=?1", nativeQuery = true )
  Integer findByUserDetailsId(@Param( "user_details_id" ) Integer id);

  @Query( "select id from User where username=?1" )
  Integer findUserIdByUserName(String userName);

  User findByUsername(String name);

  User findByUserDetails(UserDetails userDetails);

}
