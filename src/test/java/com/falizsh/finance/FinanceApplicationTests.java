package com.falizsh.finance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"cloudconvert.token=test-token",
		"brapi.token=test-token"
})
class FinanceApplicationTests {

	@Test
	void contextLoads() {
	}

}
