package lk.covid19.contact_tracer.util.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString //Without this we cannot take any object id related with this audit class
@MappedSuperclass
@EntityListeners( AuditingEntityListener.class )
@JsonIgnoreProperties( value = {"createdAt", "updatedAt", "createdBy", "updatedBy"}, allowGetters = true )
public abstract class AuditEntity {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;

  @CreatedBy
  @Basic( optional = false )
  @Column( updatable = false, nullable = false, length = 20 )
  private String createdBy;

  @CreatedDate
  @Basic( optional = false )
  @Column( updatable = false, nullable = false )
  private LocalDateTime createdAt;

  @LastModifiedBy
  @Basic( optional = false )
  @Column( nullable = false, length = 20 )
  private String updatedBy;

  @LastModifiedDate
  @Basic( optional = false )
  @Column( nullable = false )
  private LocalDateTime updatedAt;
}
