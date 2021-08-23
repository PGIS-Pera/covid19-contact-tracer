package lk.covid19.contact_tracer.asset.patient_visited_place_time.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.patient.entity.Patient;
import lk.covid19.contact_tracer.asset.visited_place.entity.VisitedPlace;
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
@JsonFilter( "PatientVisitedPlaceTime" )
public class PatientVisitedPlaceTime extends AuditEntity {

  private String name;

  @DateTimeFormat( pattern = "HH:mm" )
  private LocalDateTime reachAt;

  @DateTimeFormat( pattern = "HH:mm" )
  private LocalDateTime outAt;

  @ManyToOne
  @JoinColumn( name = "patient_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_patient_vs_visited_place_time" ) )
  private Patient patient;

  @ManyToOne
  @JoinColumn( name = "visited_place_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_visited_place_vs_visited_place_time" ) )
  private VisitedPlace visitedPlace;

}
