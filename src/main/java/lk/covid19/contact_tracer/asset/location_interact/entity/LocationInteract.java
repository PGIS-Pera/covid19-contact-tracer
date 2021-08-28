package lk.covid19.contact_tracer.asset.location_interact.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "LocationInteract" )
public class LocationInteract extends AuditEntity {

  @Size( min = 2, message = "Your district name should be included more than two characters" )
  private String name;

  @ManyToOne
  @JoinColumn( name = "grama_niladhari_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_grama_niladhari_vs_location_interact" ) )
  private GramaNiladhari gramaNiladhari;

  @OneToMany( mappedBy = "locationInteract" )
  private List< PersonLocationInteractTime > personLocationInteractTimes;

}
