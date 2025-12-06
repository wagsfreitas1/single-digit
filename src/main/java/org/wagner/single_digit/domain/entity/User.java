package org.wagner.single_digit.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SingleDigit> singleDigits = new ArrayList<>();

    public void updateFrom(User user) {
        setName(user.getName());
        setEmail(user.getEmail());
    }

    public void addSingleDigit(SingleDigit singleDigit) {
        singleDigits.add(singleDigit);
        singleDigit.setUser(this);
    }

}
