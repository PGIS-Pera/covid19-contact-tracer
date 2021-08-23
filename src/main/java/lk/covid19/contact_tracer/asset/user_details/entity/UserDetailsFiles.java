package lk.covid19.contact_tracer.asset.user_details.entity;

import lk.covid19.contact_tracer.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsFiles extends AuditEntity {

  private String name, mimeType, newName;

  @Column( unique = true )
  private String newId;

  @Lob
  private byte[] pic;
  @ManyToOne
  @JoinColumn( name = "user_details_id", nullable = false, foreignKey = @ForeignKey( name =
      "fk_user_details_file_vs_user_details" ) )
  private UserDetails userDetails;

  public UserDetailsFiles(String name, String mimeType, byte[] pic, String newName, String newId) {
    this.name = name;
    this.mimeType = mimeType;
    this.pic = pic;
    this.newName = newName;
    this.newId = newId;
  }

}
