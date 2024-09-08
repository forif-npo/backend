package forif.univ_hanyang.user.entity;

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
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "user_name")
    public String name;
    public String department;
    @Column(name = "phone_num")
    public String phoneNumber;
    public Integer authLv;
    private String imgUrl;

}