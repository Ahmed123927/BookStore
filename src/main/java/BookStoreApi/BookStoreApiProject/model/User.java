package BookStoreApi.BookStoreApiProject.model;


import lombok.Data;


import javax.persistence.*;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.*;
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotEmpty
    @Column(nullable = false)
    private String firstName;

    private String lastName;
    @Email(message = "{errors.invalid_email}")
    @NotEmpty
    @Column(nullable = false,unique = true)
    private String email;


    private String password;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns = {@JoinColumn(name = "USER_ID",referencedColumnName = "ID")},inverseJoinColumns = {@JoinColumn(name = "ROLE_ID",referencedColumnName = "ID")})
    private List<Role> roles;


    public User(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }
    public User(){

    }
}
