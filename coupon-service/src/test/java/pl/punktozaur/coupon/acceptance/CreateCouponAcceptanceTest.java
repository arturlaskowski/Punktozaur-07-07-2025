package pl.punktozaur.coupon.acceptance;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.punktozaur.AcceptanceTest;
import pl.punktozaur.KafkaIntegrationTest;
import pl.punktozaur.coupon.application.dto.CouponDto;
import pl.punktozaur.coupon.domain.CouponStatus;
import pl.punktozaur.coupon.web.dto.CreateCouponRequest;
import pl.punktozaur.coupon.web.dto.NominalValueApi;
import pl.punktozaur.domain.LoyaltyAccountId;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest(topics = CreateCouponAcceptanceTest.LOYALTY_COMMAND_TOPIC)
class CreateCouponAcceptanceTest extends KafkaIntegrationTest {

    static final String LOYALTY_COMMAND_TOPIC = "loyalty-commands";

    @BeforeEach
    void setUp() {
        setupKafkaConsumer(LOYALTY_COMMAND_TOPIC);
    }

    @Test
    @DisplayName("""
            given valid coupon creation request,
            when request is sent,
            then coupon is created and HTTP 201 status returned with location header""")
    void givenValidCouponCreationRequest_whenRequestIsSent_thenCouponCreatedAndHttp201Returned() throws InterruptedException {
        // given
        var loyaltyAccountId = LoyaltyAccountId.newOne().id();
        var createCouponRequest = new CreateCouponRequest(loyaltyAccountId, NominalValueApi.TWENTY);

        //when
        var postResponse = testRestTemplate.postForEntity(getBaseCouponsUrl(), createCouponRequest, Void.class);

        //then
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();
        var couponId = postResponse.getHeaders().getLocation().getPath().split("/")[2];

        var getResponse = testRestTemplate.getForEntity(postResponse.getHeaders().getLocation(), CouponDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody())
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", UUID.fromString(couponId))
                .hasFieldOrPropertyWithValue("loyaltyAccountId", createCouponRequest.loyaltyAccountId())
                .hasFieldOrPropertyWithValue("status", CouponStatus.PENDING)
                .extracting(CouponDto::nominalValue)
                .extracting(Enum::name)
                .isEqualTo(createCouponRequest.nominalValue().name());

        // Verify that command was sent to Kafka
        ConsumerRecord<String, String> customerEvent = records.poll(5, TimeUnit.SECONDS);
        assertThat(customerEvent).isNotNull();
        assertThat(customerEvent.topic()).isEqualTo(LOYALTY_COMMAND_TOPIC);
        assertThat(customerEvent.key()).isEqualTo((createCouponRequest.loyaltyAccountId().toString()));

    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }
}