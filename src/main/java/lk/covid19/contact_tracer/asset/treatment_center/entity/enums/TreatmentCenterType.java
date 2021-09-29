package lk.covid19.contact_tracer.asset.treatment_center.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TreatmentCenterType {

  ARMY("SL Army"),
  GOV_HOSPITAL("Government Hospital");

  //todo all QuarantineCenter type should be needed to add here

  private final String treatmentCenterType;

}
