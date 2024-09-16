package com.first.testingProject.payment;

import com.first.testingProject.customer.Customer;
import com.first.testingProject.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

//import static org.awaitility.Awaitility.given;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

class PaymentServiceTest {

    @Mock
    private  CustomerRepository customerRepository;

    @Mock
    private  PaymentRepository paymentRepository ;

    @Mock
    private  CardPaymentCharger cardPaymentCharger ;

    private  PaymentService underTest ;

    @BeforeEach
    void setUp(){

        MockitoAnnotations.initMocks(this);
      underTest = new PaymentService( customerRepository, paymentRepository, cardPaymentCharger) ;
    }

    @Test
    void itShouldChargeCardSuccessfully() {
        //Given
        UUID customerId = UUID.randomUUID() ;

        //... Customer exists
        given(customerRepository.findById(customerId)).willReturn( Optional.of(mock(Customer.class)));


        //...Payment Request
        Currency currency = Currency.USD;
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        currency,
                        "card123xx",
                        "Donation"
                )

        );


        //...Card is charged successfully
        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge( true)) ;

        //When
        underTest.chargeCard(customerId, paymentRequest);

        //Then
        ArgumentCaptor<Payment> paymentArgumentCaptor =
                ArgumentCaptor.forClass(Payment.class) ;

        then(paymentRepository).should().save( paymentArgumentCaptor.capture()) ;

        Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue() ;
        assertThat(paymentArgumentCaptorValue)
                .isEqualToIgnoringGivenFields(paymentRequest.getPayment(),
                        "customerId");

        assertThat(paymentArgumentCaptorValue.getPaymentId()).isEqualTo(customerId) ;

    }

    @Test
    void itShouldThrowWhenCardIsNotCharged() {
        //Given
        UUID customerId = UUID.randomUUID() ;

        //... Customer exists
        given(customerRepository.findById(customerId)).willReturn( Optional.of(mock(Customer.class)));


        //...Payment Request
        Currency currency = Currency.USD;
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        currency,
                        "card123xx",
                        "Donation"
                )

        );


        //...Card is charged successfully
        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge( false)) ;

        //When
        //Then
       assertThatThrownBy(() ->underTest.chargeCard(customerId, paymentRequest))
               .isInstanceOf(IllegalStateException.class)
                       .hasMessageContaining("Card not debited for customer " + customerId);

        then(paymentRepository).should(never()).save(any(Payment.class));
    }
}