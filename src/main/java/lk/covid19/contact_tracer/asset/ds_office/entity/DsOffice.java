package lk.covid19.contact_tracer.asset.ds_office.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder( toBuilder = true )
@JsonFilter( "DsOffice" )
public class DsOffice {
  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;

  @Size( min = 2, max = 60, message = "Your Stream length should be 12" )
  private String name;

  private String address;
  private String land;
  private String email;

  @ManyToOne
  @JoinColumn( name = "distric_id", nullable = false, foreignKey = @ForeignKey( name = "fk_ds_office_vs_district" ) )
  private District district;

  @OneToMany( mappedBy = "dsOffice" )
  private List< GramaNiladhari > gramaNiladharis;

}
