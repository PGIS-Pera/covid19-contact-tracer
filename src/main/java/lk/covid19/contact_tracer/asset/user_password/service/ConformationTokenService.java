package lk.covid19.contact_tracer.asset.user_password.service;


import lk.covid19.contact_tracer.asset.user_password.dao.ConformationTokenDao;
import lk.covid19.contact_tracer.asset.user_password.entity.ConformationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConformationTokenService {
  private final ConformationTokenDao conformationTokenDao;

  public ConformationToken createToken(ConformationToken conformationToken) {
    return conformationTokenDao.save(conformationToken);
  }

  public ConformationToken findByToken(String token) {
    return conformationTokenDao.findByToken(token);
  }

  public ConformationToken findByEmail(String email) {
    return conformationTokenDao.findByEmail(email);
  }

  public void deleteByConformationToken(ConformationToken conformationToken) {
    conformationTokenDao.delete(conformationToken);
  }
}
