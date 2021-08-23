package lk.covid19.contact_tracer.asset.patient.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Gender;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.patient.entity.enums.DeadLive;
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
public class Patient extends AuditEntity {

  @Column( nullable = false )
  private String code;

  @NotNull
  @Column( nullable = false )
  @Field
  private String name;

  private int age;

  @Size( max = 12, min = 10, message = "NIC number is contained numbers between 9 and X/V or 12 " )
  @Column( unique = true )
  private String nic;

  @Size( max = 10, message = "Mobile number length should be contained 10 and 9" )
  private String mobile;

  @Field
  private String address;

  @Enumerated( EnumType.STRING )
  private DeadLive deadLive;

  @Enumerated( EnumType.STRING )
  private Gender gender;

  @ManyToOne
  @JoinColumn( name = "grama_niladhari_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_patient_vs_grama_niladhari" ) )
  private GramaNiladhari gramaNiladhari;


  //@ContainedIn
  @OneToMany( mappedBy = "patient", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER )
  private List< Attempt > attempts;

  @Transient
  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate identifiedDate;

  @Transient
  private String remarks;

  @Transient
  private String policeStationName;

  @Transient
  private DeadLive activeInactive;

  @Transient
  private Attempt attempt;

  @Transient
  private MultipartFile multipartFile;

}
