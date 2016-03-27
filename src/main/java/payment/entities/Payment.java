package payment.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Dark on 27.03.2016.
 * Модель "Платеж"
 */
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //создатель платежа
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String purpose;
    private String status;
    private Date date;
    private long manager;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getManager() {
        return manager;
    }

    public void setManager(long manager) {
        this.manager = manager;
    }

    protected Payment(){}
    public Payment(String purpose, User user) {
        this.purpose = purpose;
        this.user = user;
        this.status = "new";
        this.date = new Date();
        this.manager = 0;
    }
}
