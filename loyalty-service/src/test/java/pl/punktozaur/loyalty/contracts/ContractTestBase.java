package pl.punktozaur.loyalty.contracts;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.LoyaltyPoints;
import pl.punktozaur.loyalty.LoyaltyApp;
import pl.punktozaur.loyalty.application.LoyaltyAccountService;
import pl.punktozaur.loyalty.application.dto.CreateLoyaltyAccountDto;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = LoyaltyApp.class)
class ContractTestBase {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private LoyaltyAccountService accountService;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        // Mock subtractPoints to do nothing
        doNothing().when(accountService).subtractPoints(any(LoyaltyAccountId.class), any(LoyaltyPoints.class));

        // Mock addAccount to return a predictable LoyaltyAccountId
        when(accountService.addAccount(any(CreateLoyaltyAccountDto.class)))
                .thenAnswer(invocation -> new LoyaltyAccountId(UUID.randomUUID()));
    }
}