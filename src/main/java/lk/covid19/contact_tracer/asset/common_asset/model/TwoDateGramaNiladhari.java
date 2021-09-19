package lk.covid19.contact_tracer.asset.common_asset.model;

import lk.covid19.contact_tracer.asset.common_asset.model.enums.StopActive;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TwoDateGramaNiladhari {
  @Enumerated( EnumType.STRING )
  private StopActive stopActive;

  @DateTimeFormat( pattern = "yyyy-MM-dd'T'HH:mm" )
  private LocalDateTime arrivalAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd'T'HH:mm" )
  private LocalDateTime leaveAt;

  private GramaNiladhari gramaNiladhari;
}
