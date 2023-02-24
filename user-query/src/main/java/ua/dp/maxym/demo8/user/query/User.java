package ua.dp.maxym.demo8.user.query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "userProjection") // "user" is reserved keyword in H2
@Data
public final class User {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Double money;

    public User(String email, String firstName, String lastName, Double money) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.money = money;
    }

    public User() {
    }
}
