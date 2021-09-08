package lk.covid19.contact_tracer.asset.turn_in_history.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TurnInHistoryNoteStatus {
  PERSONAL_NOTE("Personal Note"),
  RDT("Rapid Diagnostic Tests (RDT)"),
  RADIOLOGY_CT("Computer Tomography (CT)"),
  RADIOLOGY_GENERAL("Chest X-Ray"),
  PCR("Polymerase Chain Reaction (PCR)"),
  ECG("Electrocardiogram (ECG)"),
  US_SCAN("Ultra Sound Scan (US Scan)"),
  TEM_RECORD("Temperature Record");

  private final String turnInHistoryNoteStatus;
}
