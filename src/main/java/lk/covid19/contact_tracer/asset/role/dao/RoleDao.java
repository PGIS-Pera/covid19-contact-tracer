package lk.covid19.contact_tracer.asset.role.dao;

import lk.covid19.contact_tracer.asset.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository< Role, Integer > {
  Role findByRoleName(String roleName);
}
