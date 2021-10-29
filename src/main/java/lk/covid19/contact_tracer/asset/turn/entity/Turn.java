package lk.covid19.contact_tracer.asset.turn.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.asset.turn_history.entity.TurnHistory;
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
@Builder
@JsonFilter( "Turn" )
public class Turn extends AuditEntity {

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate identifiedDate;

  private String remark;

  @Enumerated( EnumType.STRING )
  private PersonStatus personStatus;

  @ManyToOne( cascade = CascadeType.MERGE )
  @JoinColumn( name = "person_id", nullable = false, foreignKey = @ForeignKey( name = "fk_turn_vs_person" ) )
  private Person person;

  @OneToMany( mappedBy = "turn", cascade = {CascadeType.PERSIST, CascadeType.REFRESH} )
  private List< PersonLocationInteractTime > personLocationInteractTimes;

  @OneToMany( mappedBy = "turn" )
  private List< TurnHistory > turnHistories;


}
