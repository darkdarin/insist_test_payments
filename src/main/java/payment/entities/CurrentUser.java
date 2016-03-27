package payment.entities;

import java.util.Set;

/**
 * Created by Dark on 27.03.2016.
 * "Оболочка" для модели User
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public CurrentUser(User user) {
        super(user.getUsername(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public Set<Role> getRoles() {
        return user.getRoles();
    }
}
