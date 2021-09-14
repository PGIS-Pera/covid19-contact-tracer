package lk.covid19.contact_tracer.asset.turn_in_history.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.turn_history.entity.TurnHistory;
import lk.covid19.contact_tracer.asset.turn_in_history.entity.enums.Symptoms;
import lk.covid19.contact_tracer.asset.turn_in_history.entity.enums.TurnInHistoryNoteStatus;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder( toBuilder = true )
@JsonFilter( "TurnInHistoryNote" )
@ToString
public class TurnInHistoryNote extends AuditEntity {

  @Enumerated( EnumType.STRING )
  private Symptoms symptoms;

  @Enumerated( EnumType.STRING )
  private TurnInHistoryNoteStatus turnInHistoryNoteStatus;

  @Enumerated( EnumType.STRING )
  private PersonStatus personStatus;

  @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm" )
  private LocalDateTime feelingStartAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm" )
  private LocalDateTime feelingEndAt;

  private String remark;

  @ManyToOne
  @JoinColumn( name = "turn_history_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_turn_in_history_note_vs_turn_history" ) )
  @ToString.Exclude
  private TurnHistory turnHistory;

}
