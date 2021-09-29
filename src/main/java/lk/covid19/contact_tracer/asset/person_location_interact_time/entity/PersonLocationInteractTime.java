package lk.covid19.contact_tracer.asset.person_location_interact_time.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.StopActive;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "PersonLocationInteractTime" )
public class PersonLocationInteractTime extends AuditEntity {

  @Enumerated( EnumType.STRING )
  private StopActive stopActive;

  @DateTimeFormat( pattern = "yyyy-MM-dd'T'HH:mm" )
  private LocalDateTime arrivalAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd'T'HH:mm" )
  private LocalDateTime leaveAt;

  @ManyToOne
  @JoinColumn( name = "person_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_person_vs_person_location_interact_time" ) )
  private Person person;

  @ManyToOne
  @JoinColumn( name = "location_interact_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_visited_place_vs_person_location_interact_time" ) )
  private LocationInteract locationInteract;

}
