package fororo.univ_hanyang.user.entity;

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
@Table(name = "user") // 테이블 이름 지정
public class User {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "department")
    private String department;

    @Column(name = "image")
    private String image;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserAuthorization userAuthorization;

}