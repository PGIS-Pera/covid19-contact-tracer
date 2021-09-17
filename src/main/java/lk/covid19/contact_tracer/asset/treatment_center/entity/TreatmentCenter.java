package lk.covid19.contact_tracer.asset.treatment_center.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.ds_office.entity.DsOffice;
import lk.covid19.contact_tracer.asset.treatment_center.entity.enums.TreatmentCenterType;
import lk.covid19.contact_tracer.asset.turn_in_history.entity.TurnInHistoryNote;
import lk.covid19.contact_tracer.asset.user_treatment_center.entity.UserTreatmentCenter;
import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder( toBuilder = true )
@JsonFilter( "TreatmentCenter" )
@ToString
public class TreatmentCenter extends AuditEntity {

  private String name;

  private String number;

  @Enumerated( EnumType.STRING )
  private TreatmentCenterType treatmentCenterType;

  @ManyToOne
  @JoinColumn( name = "ds_office_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_treatment_center_vs_ds_office" ) )
  @ToString.Exclude
  private DsOffice dsOffice;

  @OneToMany( mappedBy = "treatmentCenter" )
  private List< TurnInHistoryNote > turnInHistoryNotes;

  @OneToMany( mappedBy = "treatmentCenter" )
  private List< UserTreatmentCenter > userTreatmentCenters;

}
