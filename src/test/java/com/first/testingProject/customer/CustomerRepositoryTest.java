package com.first.testingProject.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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
        Customer customer = new Customer(id, "Abel", "id");

        //When
        repo.save(customer);

        //Then
        Optional<Customer> optionalCustomer = repo.findById(id);
        assertThat(optionalCustomer).isPresent();

    }





}