package lk.covid19.contact_tracer.asset.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.role.entity.Role;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.configuration.sec_user_detail_service.entity.UserSessionLog;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties( value = "createdDate", allowGetters = true )
public class User extends AuditEntity {

  @Column( nullable = false, unique = true )
  @Size( min = 5, message = "user name should include at least five characters" )
  private String username;

  @Column( nullable = false )
  @Size( min = 4, message = "password should include four characters or symbols" )
  private String password;

  @Column( nullable = false )
  private boolean enabled;

  @OneToOne
  private UserDetails userDetails;

  @ManyToOne
  @JoinColumn( name = "person_id", nullable = false, foreignKey = @ForeignKey( name = "fk_user_vs_person" ) )
  private Person person;

  @OneToMany( mappedBy = "user", fetch = FetchType.EAGER )
  private List< UserSessionLog > userSessionLogs;

  @ManyToMany( fetch = FetchType.EAGER )
  @Fetch( FetchMode.SUBSELECT )
  @JoinTable( name = "user_role",
      joinColumns = @JoinColumn( name = "user_id" ),
      inverseJoinColumns = @JoinColumn( name = "role_id" ) )
  private List< Role > roles;


}
