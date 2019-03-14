package nfq.bidding.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class User {

    public User() {

    }

    public User(String userName) {
        this.userName = userName;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY/*, generator = "userIdSequence"*/)
    /*@SequenceGenerator(name = "userIdSequence", sequenceName = "auto_increment", allocationSize = 1)*/
    private Long id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;
}
