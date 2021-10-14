package lk.covid19.contact_tracer.asset.treatment_center.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TreatmentCenterType {

  ARMY("SL Army"),
  GOV_HOSPITAL("Government Hospital");

  // all QuarantineCenter type should be needed to add here-> future development

  private final String treatmentCenterType;

}
