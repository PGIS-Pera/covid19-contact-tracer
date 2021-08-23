package lk.covid19.contact_tracer.asset.attempt.entity;

import lk.covid19.contact_tracer.asset.patient.entity.Patient;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.*;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Date;

@Indexed
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attempt extends AuditEntity {

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate identifiedDate;

  private String sheetName, author, title, lastAuthor, remark;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private Date createdDate;

  @IndexedEmbedded
  @ManyToOne
  @JoinColumn( name = "patient_id", nullable = false, foreignKey = @ForeignKey( name = "fk_attempt_vs_patient" ) )
  private Patient patient;
}
