package payment.repositories;

import org.springframework.data.repository.CrudRepository;
import payment.entities.Payment;

/**
 * Created by Dark on 27.03.2016.
 */
public interface PaymentsRepository extends CrudRepository<Payment, Long> {
    Iterable<Payment> findByUserId(long user_id);
    Iterable<Payment> findByStatus(String status);
    Iterable<Payment> findByStatusAndManager(String status, long manager);
}
