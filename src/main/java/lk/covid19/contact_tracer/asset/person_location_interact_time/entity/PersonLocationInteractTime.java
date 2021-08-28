package lk.covid19.contact_tracer.asset.person_location_interact_time.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "PersonLocationInteractTime" )
public class PersonLocationInteractTime extends AuditEntity {

  @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm" )
  private LocalDateTime reachAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm" )
  private LocalDateTime outAt;

  @ManyToOne
  @JoinColumn( name = "person_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_person_vs_person_location_interact_time" ) )
  private Person person;

  @ManyToOne
  @JoinColumn( name = "location_interact_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_visited_place_vs_person_location_interact_time" ) )
  private LocationInteract locationInteract;

}
