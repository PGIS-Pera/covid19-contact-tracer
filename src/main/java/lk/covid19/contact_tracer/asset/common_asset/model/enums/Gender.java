package lk.covid19.contact_tracer.asset.common_asset.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
  MALE("Male"),
  FEMALE("Female");

  private final String gender;
}
