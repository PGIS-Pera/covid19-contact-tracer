package lk.covid19.contact_tracer.asset.common_asset.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
  private String filename;
  private LocalDateTime createAt;
  private String url;
}

