package lk.covid19.contact_tracer.asset.report.model;

import lk.covid19.contact_tracer.asset.common_asset.model.AttributeAndCount;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder( toBuilder = true )
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GramaniladariReportDTO {

  private GramaNiladhari gramaNiladhari;

  private List< AttributeAndCount > attributeAndCounts;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate turnStartAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate turnEndAt;

}
