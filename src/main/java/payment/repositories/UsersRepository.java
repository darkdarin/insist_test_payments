package payment.repositories;

import org.springframework.data.repository.CrudRepository;
import payment.entities.User;

/**
 * Created by Dark on 27.03.2016.
 * Репозиторий работы с пользователями
 */
public interface UsersRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
