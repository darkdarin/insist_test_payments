package payment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import payment.entities.CurrentUser;
import payment.entities.Payment;
import payment.entities.Role;
import payment.entities.User;
import payment.repositories.PaymentsRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Dark on 27.03.2016.
 */
@RestController
@RequestMapping("/payments")
public class PaymentsController {
    @Autowired
    PaymentsRepository payments;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('user')")
    public List<Payment> getPayments()
    {
        CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Payment> result = new ArrayList<>();
        payments.findByUserId(user.getId()).forEach(result::add);
        return result;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @PreAuthorize("hasRole('manager')")
    public List<Payment> getNewPayments()
    {
        List<Payment> result = new ArrayList<>();
        payments.findByStatus("new").forEach(result::add);
        return result;
    }

    @RequestMapping(value = "/preapproved", method = RequestMethod.GET)
    @PreAuthorize("hasRole('topmanager')")
    public List<Payment> getPreApprovedPayments()
    {
        CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Payment> result = new ArrayList<>();
        payments.findByStatusAndManager("preapproved", user.getId()).forEach(result::add);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('user')")
    public Payment addPayment(String purpose)
    {
        if (purpose.isEmpty()) return null;

        CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return payments.save(new Payment(purpose, user.getUser()));
    }

    @RequestMapping(value="/approved", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('topmanager','manager')")
    public Payment approvedPayment(long id, HttpServletRequest request)
    {
        CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Payment payment = payments.findOne(id);

        if (request.isUserInRole("topmanager")) {
            payment.setStatus("approved");
        } else {
            payment.setStatus("preapproved");
            payment.setManager(user.getUser().getManager());
        }

        return payments.save(payment);
    }

    @RequestMapping(value="/cancel", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('topmanager','manager')")
    public Payment cancelPayment(long id)
    {
        Payment payment = payments.findOne(id);

        payment.setStatus("cancel");
        return payments.save(payment);
    }
}
