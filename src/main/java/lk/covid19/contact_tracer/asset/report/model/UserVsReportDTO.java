package lk.covid19.contact_tracer.asset.report.model;

import lk.covid19.contact_tracer.asset.common_asset.model.AttributeAndCount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder( toBuilder = true )
@AllArgsConstructor
@NoArgsConstructor
public class UserVsReportDTO {
  private String name;
  private String email;
  private LocalDate localDate;
  private List< AttributeAndCount > attributeAndCounts;

}
