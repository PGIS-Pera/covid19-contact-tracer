package lk.covid19.contact_tracer.asset.report.model;

import lk.covid19.contact_tracer.asset.common_asset.model.AttributeAndCount;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.entity.District;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder( toBuilder = true )
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceReportDTO {

  @Enumerated( EnumType.STRING )
  private Province province;

  private List< AttributeAndCount > attributeAndCounts;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate turnStartAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate turnEndAt;

  private List< DistrictReportDTO > districtReportDTOS;

}
