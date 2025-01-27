package com.exm.news.entity.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@NamedNativeQuery(
//        name="findLoginSameEmail",
//        query="select * from logins where email = :email"
//)
@Entity
@Table(name = "logins")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

	@NotBlank(message="please enter data")
    @Column(name = "email", nullable = false)
    private String email;

	@NotBlank(message="please enter data")
    @Column(name = "password", nullable = false)
    private String password;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
