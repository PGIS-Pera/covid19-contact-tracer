package lk.covid19.contact_tracer.asset.visited_place.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.patient_visited_place_time.entity.PatientVisitedPlaceTime;
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
@JsonFilter( "VisitedPlace" )
public class VisitedPlace extends AuditEntity {

  @Size( min = 2, message = "Your district name should be included more than two characters" )
  private String name;

  @ManyToOne
  @JoinColumn( name = "grama_niladhari_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_grama_niladhari_vs_visited_place" ) )
  private GramaNiladhari gramaNiladhari;

  @OneToMany( mappedBy = "visitedPlace" )
  private List< PatientVisitedPlaceTime > patientVisitedPlaceTimes;

}
