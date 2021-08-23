package lk.covid19.contact_tracer.asset.patient.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeadLive {
  DEAD("Dead"),
  LIVE("Live");

  private final String deadLive;
}

