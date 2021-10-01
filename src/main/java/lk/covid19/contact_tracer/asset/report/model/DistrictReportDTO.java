package lk.covid19.contact_tracer.asset.report.model;

import lk.covid19.contact_tracer.asset.common_asset.model.AttributeAndCount;
import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.ds_office.entity.DsOffice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder( toBuilder = true )
@AllArgsConstructor
@NoArgsConstructor
public class DistrictReportDTO {

  private District district;

  private List< AttributeAndCount > attributeAndCounts;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate turnStartAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate turnEndAt;

  private List< DsOfficeReportDTO > dsOfficeReportDTOS;

}
