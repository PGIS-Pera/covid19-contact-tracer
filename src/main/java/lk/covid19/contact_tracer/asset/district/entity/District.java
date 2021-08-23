package lk.covid19.contact_tracer.asset.district.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "District" )
public class District {
  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;

  @Column( unique = true )
  @Size( min = 2, message = "Your district name should be included more than two characters" )
  private String name;

  @Enumerated( EnumType.STRING )
  private Province province;

}
