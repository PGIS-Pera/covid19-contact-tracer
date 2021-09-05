package lk.covid19.contact_tracer.asset.user_password.dao;


import lk.covid19.contact_tracer.asset.user_password.entity.ConformationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConformationTokenDao extends JpaRepository< ConformationToken, Integer > {
  ConformationToken findByToken(String token);

  ConformationToken findByEmail(String email);
}
