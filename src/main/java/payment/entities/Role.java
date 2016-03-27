package payment.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Dark on 27.03.2016.
 * Модель "Роль"
 */
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    //каждой роли может принадлежать множество пользователей
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    protected Role(){}
    public Role(String name)
    {
        role = name;
    }

    @Override
    public String getAuthority() {
        return getRole();
    }
}
