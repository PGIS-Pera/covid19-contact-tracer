package lk.covid19.contact_tracer.asset.user_treatment_center.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.StopActive;
import lk.covid19.contact_tracer.asset.treatment_center.entity.TreatmentCenter;
import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder( toBuilder = true )
@JsonFilter( "UserTreatmentCenter" )
@ToString
public class UserTreatmentCenter extends AuditEntity {

  @Enumerated( EnumType.STRING )
  private StopActive stopActive;

  @ManyToOne
  @JoinColumn( name = "treatment_center_id", foreignKey =
  @ForeignKey( name = "fk_user_treatment_center_vs_treatment_center" ) )
  private TreatmentCenter treatmentCenter;

  @ManyToOne
  @JoinColumn( name = "user_id", foreignKey =
  @ForeignKey( name = "fk_user_treatment_center_vs_user" ) )
  private User user;

}