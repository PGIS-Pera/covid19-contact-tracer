package lk.covid19.contact_tracer.asset.person.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Gender;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.*;
import org.hibernate.search.annotations.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


//@Indexed
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder( toBuilder = true )
@JsonFilter( "Patient" )
public class Person extends AuditEntity {

  @Column( nullable = false, unique = true )
  private String code;

  @NotNull
  @Column( nullable = false )
  @Field
  private String name;

  @Size( max = 12, min = 10, message = "NIC number is contained numbers between 9 and X/V or 12 " )
  @Column( unique = true )
  private String nic;

  @Size( max = 10, message = "Mobile number length should be contained 10 and 9" )
  private String mobile;

  @Field
  private String address;

  private String workingPlaceAddress;

  @Enumerated( EnumType.STRING )
  private PersonStatus personStatus;

  @Enumerated( EnumType.STRING )
  private Gender gender;

  //@ContainedIn
  @ManyToOne
  @JoinColumn( name = "grama_niladhari_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_patient_vs_grama_niladhari" ) )
  private GramaNiladhari gramaNiladhari;


  //@ContainedIn
  @OneToMany( mappedBy = "person", cascade = {CascadeType.MERGE, CascadeType.PERSIST} )
  private List< Attempt > attempts;

  @Transient
  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate identifiedDate;

  @Transient
  private String remarks;

  @Transient
  private String policeStationName;

  @Transient
  private PersonStatus activeInactive;

  @Transient
  private Attempt attempt;

  @Transient
  private MultipartFile multipartFile;

}
