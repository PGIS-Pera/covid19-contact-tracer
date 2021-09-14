package lk.covid19.contact_tracer.asset.user_details.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.common_asset.model.FileInfo;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Gender;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.StopActive;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Title;
import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "UserDetails" )
public class UserDetails extends AuditEntity {

  @Size( min = 5, message = "Your name cannot be accepted" )
  private String name;

  @Column( nullable = false )
  private String number;

  @Size( min = 5, message = "At least 5 characters should be included calling name" )
  private String callingName;

  @Size( max = 12, min = 10, message = "NIC number is contained numbers between 9 and X/V or 12 " )
  @Column( unique = true )
  private String nic;

  @Size( max = 10, message = "Mobile number length should be contained 10 and 9" )
  private String mobileOne;

  private String mobileTwo;

  private String land;

  @Column( unique = true )
  private String email;

  @Column( unique = true )
  private String officeEmail;

  @Column( columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NULL", length = 255 )
  private String address;

  @Enumerated( EnumType.STRING )
  private Title title;

  @Enumerated( EnumType.STRING )
  private Gender gender;

  @Enumerated( EnumType.STRING )
  private StopActive stopActive;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate dateOfBirth;

  @OneToOne(mappedBy = "userDetails")
  private User user;

  @Transient
  private MultipartFile file;

  @Transient
  private FileInfo fileInfo;


}
