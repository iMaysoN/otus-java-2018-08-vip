package orm.data;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "street")
    private String street;
//    @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE })
//    @JoinColumn(name = "user_id")
//    private User user;

    public Address() {
    }

    public Address(String street) {
        this.street = street;
    }

//    public void setUser(User user) {
//        this.user = user;
//    }

//    public User getUser() {
//        return user;
//    }

    @Override
    @Transient
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", address='" + street + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
