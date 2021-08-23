package lk.covid19.contact_tracer.asset.common_asset.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Province {
  WP("Western Province"),
  CP("Central Province"),
  NW("North Western Province"),
  UP("Uva Province"),
  SP("Southern Province"),
  SG("Sabaragamuwa Province"),
  NC("North Central Province"),
  NP("Northern Province"),
  EP("Eastern Province");

  private final String province;
}
