package lk.covid19.contact_tracer.asset.grama_niladhari.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.ds_office.entity.DsOffice;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder( toBuilder = true )
@JsonFilter( "GramaNiladhari" )
public class GramaNiladhari {
  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;

  private String name;

  private String number;

  @ManyToOne
  @JoinColumn( name = "ds_office_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_grama_niladhari_vs_ds_office" ) )
  private DsOffice dsOffice;

  @OneToMany( mappedBy = "gramaNiladhari" )
  private List< Person > people;

  @OneToMany( mappedBy = "gramaNiladhari" )
  private List< LocationInteract > locationInteracts;

}
