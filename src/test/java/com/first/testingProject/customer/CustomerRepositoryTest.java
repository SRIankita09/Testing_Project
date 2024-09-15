package com.first.testingProject.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
//import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import java.util.Currency;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)

class CustomerRepositoryTest {


    @Autowired
    private CustomerRepository repo ;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        //Given
        //When
        //Then

    }

    @Test
    void itShouldSaveCustomer() {
        //Given
        UUID id = UUID.randomUUID() ;
        Customer customer = new Customer(id, "Abel", "0000");

        //When
        repo.save(customer);

        //Then
        Optional<Customer> optionalCustomer = repo.findById(id);
        assertThat(optionalCustomer).isPresent();

    }

//    @Test
//    void itShouldNotSaveCustomerWhenNameIsNull() {
//        //Given
//        UUID id = UUID.randomUUID() ;
//        Customer customer = new Customer(id, null, "0000");
//
//        //When
//
//
//       //Then
//        assertThatThrownBy( () -> repo.save(customer))
//                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.name ")
//                .isInstanceOf(DataIntegrityViolationException.class);
//
//   }

}