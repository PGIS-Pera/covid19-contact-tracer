package lk.covid19.contact_tracer.asset.stay_location.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.turn_history.entity.TurnHistory;
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
@Builder( toBuilder = true )
@JsonFilter( "StayLocation" )
@ToString
public class StayLocation extends AuditEntity {
  // this mean where patient was being before infected
  @Size( min = 2, max = 60, message = "Your Stream length should be 12" )
  private String name;

  private String address;
  private String land;
  private String email;

  @ManyToOne
  @JoinColumn( name = "distric_id", nullable = false, foreignKey =
  @ForeignKey( name = "fk_stay_location_vs_district" ) )
  private District district;

  @OneToMany( mappedBy = "stayLocation" )
  private List< TurnHistory > turnHistories;
}
