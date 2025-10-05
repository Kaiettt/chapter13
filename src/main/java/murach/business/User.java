package murach.business;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
	@Column(name = "id") 
    @SequenceGenerator(name = "users_SEQ", sequenceName = "users_SEQ", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;
	@Column(name = "email")
    private String email;
	@Column(name = "firstname")
    private String firstName;
	@Column(name = "lastname")
    private String lastName;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}