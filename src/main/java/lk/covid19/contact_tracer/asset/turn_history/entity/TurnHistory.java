package lk.covid19.contact_tracer.asset.turn_history.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.stay_location.entity.StayLocation;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn_history.entity.enums.VaccinatedStatus;
import lk.covid19.contact_tracer.asset.turn_in_history.entity.TurnInHistoryNote;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder( toBuilder = true )
@JsonFilter( "TurnHistory" )
@ToString
public class TurnHistory extends AuditEntity {

  @Enumerated( EnumType.STRING )
  private VaccinatedStatus vaccinatedStatus;

  private String vaccineName;

  private int doseNumber;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate vaccinatedDate;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate turnStartAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate turnEndAt;

  @Enumerated( EnumType.STRING )
  private PersonStatus personStatus;

  private String remark;

  @ManyToOne
  @JoinColumn( name = "turn_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_turn_vs_turn_history" ) )
  @ToString.Exclude
  private Turn turn;

  @ManyToOne
  @JoinColumn( name = "stay_location_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_turn_vs_turn_stay_location" ) )
  @ToString.Exclude
  private StayLocation stayLocation;

  @OneToMany( mappedBy = "turnHistory" )
  private List< TurnInHistoryNote > turnInHistories;

}
