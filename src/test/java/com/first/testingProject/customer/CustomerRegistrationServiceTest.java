package com.first.testingProject.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

//import static org.awaitility.Awaitility.given;
//import static org.hamcrest.MatcherAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@DataJpaTest(
        properties =   {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
}

)
class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository ;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    private CustomerRegistrationService repo;



    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        repo = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {
        //Given
        String phoneNumber = "00009";
        Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber) ;

        //... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //... No customer with phone number passed.
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //When
        repo.registerNewCustomer(request);

        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualToComparingFieldByField(customer);

    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        //Given
        String phoneNumber = "00009";
        Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber) ;

        //... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //... an existing customer is returned.
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        //When
        repo.registerNewCustomer(request);

        //Then
        then(customerRepository).should(never()).save(any()) ;
        //then(customerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void itShouldThrowWhenPhoneNumberIsTaken() {
        //Given
        String phoneNumber = "00009";
        Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber) ;
        Customer customerTwo = new Customer(UUID.randomUUID(), "John", phoneNumber) ;

        //... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //...a customer with same phone number is passed.
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customerTwo));

        //When
        //Then
        assertThatThrownBy( () -> repo.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("phone number [%s] is taken", phoneNumber)) ;


        //Finally
        then(customerRepository).should(never()).save(any(Customer.class));
    }

    @Test
    void itShouldSaveNewCustomerWhenIdIsNull() {

        //Given
        String phoneNumber = "00009";
        Customer customer = new Customer(null, "Maryam", phoneNumber) ;

        //... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        //...a customer with same phone number is passed.
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //When
        repo.registerNewCustomer(request);

        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptureValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptureValue)
                .isEqualToIgnoringGivenFields(customer, "id");

        assertThat(customerArgumentCaptureValue.getId()).isNotNull();



    }
}