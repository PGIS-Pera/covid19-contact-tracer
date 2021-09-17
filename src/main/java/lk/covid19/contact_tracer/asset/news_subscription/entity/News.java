package lk.covid19.contact_tracer.asset.news_subscription.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder( toBuilder = true )
@JsonFilter( "News" )
@ToString
public class News {
  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  private Integer id;

  private String mobile;

  @ManyToOne
  @JoinColumn( name = "grama_niladhari_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_person_vs_grama_niladhari" ) )
  @ToString.Exclude
  private GramaNiladhari gramaNiladhari;
}
