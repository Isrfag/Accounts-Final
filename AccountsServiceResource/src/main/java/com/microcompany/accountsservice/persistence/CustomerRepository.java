package com.microcompany.accountsservice.persistence;
import com.microcompany.accountsservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
