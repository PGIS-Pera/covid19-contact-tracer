package lk.covid19.contact_tracer.asset.common_asset.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Mail {

  private String from;
  private String mailTo;
  private String subject;
  private String htmlContent;
}
