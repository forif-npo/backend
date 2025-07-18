package forif.univ_hanyang.domain.user.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Entity
@NoArgsConstructor
@Table(name = "tb_user") // 테이블 이름 지정
public class User {
    @Id
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false, length = 100)
    private String email;
    @Column(name = "user_name", length = 50)
    private String name;
    @Column(length = 50)
    private String department;
    @Column(name = "phone_num", length = 20)
    private String phoneNumber;
    @Column(nullable = false)
    private Integer authLv;
    @Column(length = 300)
    private String imgUrl;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getAuthLv() {
        return authLv;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}