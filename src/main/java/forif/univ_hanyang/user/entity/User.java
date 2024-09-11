package forif.univ_hanyang.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "user_name")
    private String name;
    private String department;
    @Column(name = "phone_num")
    private String phoneNumber;
    private Integer authLv;
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