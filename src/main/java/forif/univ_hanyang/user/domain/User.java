package forif.univ_hanyang.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user") // 테이블 이름 지정
public class User {
    @Id
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_name")
    private String name;

    @Column(name = "department")
    private String department;

    @Column(name = "phone_num")
    private String phoneNumber;

    private Integer authLv;
    private String imgUrl;
}