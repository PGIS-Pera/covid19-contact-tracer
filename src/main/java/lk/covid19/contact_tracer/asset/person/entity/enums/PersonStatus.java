package lk.covid19.contact_tracer.asset.person.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonStatus {
  GENERAL("General"),
  SUSPECT("Suspect"),
  SELF_QUARANTINE("Self Quarantine"),
  QUARANTINE("Quarantine"),
  AFTER_QUARANTINE("After Quarantine"),
  INFECTED("Infected"),
  DEAD("Dead"),
  RECOVER("Recover");

  private final String personStatus;
}

