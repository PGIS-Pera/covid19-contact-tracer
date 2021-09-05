package lk.covid19.contact_tracer.asset.attempt.entity;

import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attempt extends AuditEntity {

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate identifiedDate;

  private String remark;

  @ManyToOne
  @JoinColumn( name = "person_id", nullable = false, foreignKey = @ForeignKey( name = "fk_attempt_vs_person" ) )
  private Person person;
}
