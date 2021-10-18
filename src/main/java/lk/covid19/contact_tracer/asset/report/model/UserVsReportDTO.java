package lk.covid19.contact_tracer.asset.report.model;

import lk.covid19.contact_tracer.asset.common_asset.model.AttributeAndCount;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder( toBuilder = true )
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserVsReportDTO {
  private String name;
  private String email;
  private LocalDate localDate;
  private List< AttributeAndCount > attributeAndCounts;

}
