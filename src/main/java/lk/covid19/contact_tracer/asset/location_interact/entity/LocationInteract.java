package lk.covid19.contact_tracer.asset.location_interact.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "LocationInteract" )
@Builder
@ToString
public class LocationInteract extends AuditEntity {

  @Size( min = 2, message = "Your district name should be included more than two characters" )
  private String name;

  @ManyToOne( fetch = FetchType.EAGER )
  @JoinColumn( name = "grama_niladhari_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_location_interact_vs_grama_niladhari" ) )
  private GramaNiladhari gramaNiladhari;

  @OneToMany( mappedBy = "locationInteract" )
  @ToString.Exclude
  private List< PersonLocationInteractTime > personLocationInteractTimes;

}
