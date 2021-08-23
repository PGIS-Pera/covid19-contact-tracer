package lk.covid19.contact_tracer.asset.common_asset.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Title {
  MR("Mr. "),
  MRS("Mrs. "),
  MISS("Miss. "),
  MS("Ms. "),
  REV("Rev. "),
  DR("Dr. "),
  DRMRS("Dr(Mrs). "),
  PRO("Prof. "),
  SISTER("Sister. ");

  private final String title;
}
