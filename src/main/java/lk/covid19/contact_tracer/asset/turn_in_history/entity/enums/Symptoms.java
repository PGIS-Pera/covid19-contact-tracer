package lk.covid19.contact_tracer.asset.turn_in_history.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Symptoms {

  FEVER("Fever"),
  COUGH("Cough"),
  TIREDNESS("tiredness"),
  DRY_COUGH("Dry Cough"),
  ACHES("Aches"),
  HEADACHE("Headache"),
  HIGH_FEVER("High Fever"),
  MILD_FEVER("Mild Fever"),
  LOSS_OF_SMELL("Loss of smell"),
  LOSS_OF_TASTE("Loss of taste"),
  PAINS("Pains"),
  SORE_THROAT("Sore Throat"),
  NASAL_CONGESTION("Nasal Congestion"),
  RED_EYES("Red Eyes"),
  DIARRHOEA("Diarrhoea"),
  SKIN_RASH("Skin Rash"),
  NO("No any complain");


  private final String symptoms;
}
