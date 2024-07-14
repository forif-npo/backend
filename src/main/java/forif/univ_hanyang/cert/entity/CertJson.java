package forif.univ_hanyang.cert.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cert_json")
public class CertJson {

    @Id
    private String certJsonId;
    private String name;
    private String study;
    private String image;
}
