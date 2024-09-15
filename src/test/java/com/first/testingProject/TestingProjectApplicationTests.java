package com.first.testingProject;

import com.first.testingProject.payment.Currency;
import com.first.testingProject.payment.Payment;
import com.first.testingProject.payment.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(
		properties = {
				"spring.jpa.properties.javax.persistence.validation.mode=none"
		}
)
class TestingProjectApplicationTests {

	@Autowired
	private PaymentRepository underTest ;


	@Test
	void itShouldInsertPayment() {

		//Given
		long paymentId = 1L;
		Payment payment = new Payment(paymentId, UUID.randomUUID(),
				new BigDecimal("10.00"),
				Currency.USD, "card123" ,
				"Donation" );

		//When
		underTest.save(payment);

		//Then
		Optional<Payment> paymentOptional = underTest.findById(paymentId);
		assertThat(paymentOptional)
				.isPresent()
				.hasValueSatisfying(p -> {
					assertThat(p).isEqualToComparingFieldByField(payment);
						}

				);

	}

}
