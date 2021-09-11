package lk.covid19.contact_tracer.asset.common_asset.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TwoDate {

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate startDate;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate endDate;
}