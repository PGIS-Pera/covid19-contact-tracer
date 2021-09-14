package lk.covid19.contact_tracer.asset.turn_history.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VaccinatedStatus {
  YES("yes"),
  NO("no");

  private final String vaccinatedStatus;

}
