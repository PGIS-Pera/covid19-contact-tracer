package lk.covid19.contact_tracer.asset.person.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonStatus {
  GENERAL("General"), //-> general register one
  SUSPECT("Suspect"), //-> volunteer suspect register
  INFECTED("Infected"), //-> infected register
  DEAD("Dead"), //-> infected dead register
  RECOVER("Recover"); //-> infected recover register

  private final String personStatus;
}

