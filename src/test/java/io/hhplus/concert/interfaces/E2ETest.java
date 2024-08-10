package io.hhplus.concert.interfaces;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import io.hhplus.concert.interfaces.controlleradvice.ErrorResponse;
import io.hhplus.concert.interfaces.presentation.concert.dto.ConcertResponse;
import io.hhplus.concert.interfaces.presentation.concert.dto.ConcertScheduleResponse;
import io.hhplus.concert.interfaces.presentation.concert.dto.ConcertSeatResponse;
import io.hhplus.concert.interfaces.presentation.payment.dto.PaymentRequest;
import io.hhplus.concert.interfaces.presentation.payment.dto.PaymentResponse;
import io.hhplus.concert.interfaces.presentation.reservation.dto.ReservationRequest;
import io.hhplus.concert.interfaces.presentation.reservation.dto.ReservationResponse;
import io.hhplus.concert.interfaces.presentation.user.dto.AssetChargeRequest;
import io.hhplus.concert.interfaces.presentation.user.dto.AssetChargeResponse;
import io.hhplus.concert.interfaces.presentation.user.dto.AssetGetResponse;
import io.hhplus.concert.interfaces.presentation.queue.dto.WaitingQueueResponse;
import io.hhplus.concert.support.exception.ExceptionCode;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class E2ETest {
    @LocalServerPort
    int port;

    @Test
    @DisplayName("전체 시나리오 E2E 테스트(토큰 발급 -> 콘서트/스케줄/좌석 조회 -> 예약 -> 결제)")
    void testAllE2E() throws InterruptedException {
        //given
        long userId = 0;

        //when
        WaitingQueueResponse queueRes = RestAssured
            .given()
                .accept("application/json")
                .port(port)
            .when()
                .get("/queue/token")
            .then()
                .statusCode(200)
                .extract().as(WaitingQueueResponse.class);

        //then
        assertThat(queueRes).isNotNull();
        assertThat(queueRes.token()).isNotNull();

        //when:15초 대기 후 조회 시 Token 대기 상태가 아닙니다 Error 발생
        Thread.sleep(15 * 1000L);
        var queueErrorRes = RestAssured
            .given()
                .accept("application/json")
                .port(port)
                .header("TOKEN", queueRes.token())
            .when()
                .get("/queue/token")
            .then()
                .statusCode(400)
                .extract().as(ErrorResponse.class);
        
        //then
        assertThat(queueErrorRes).isNotNull();
        assertThat(queueErrorRes.message()).isEqualTo(ExceptionCode.TOKEN_IS_ACTIVATED.getMessage());

        //when
        ConcertResponse[] concertsRes = RestAssured
            .given()
                .accept("application/json")
                .port(port)
                .header("TOKEN", queueRes.token())
                .param("page", 0L)
            .when()
                .get("/concerts")
            .then()
                .statusCode(200)
                .extract().as(ConcertResponse[].class);

        //then
        assertThat(concertsRes).isNotEmpty();

        //when
        ConcertScheduleResponse[] concertSchedulesRes = RestAssured
            .given()
                .accept("application/json")
                .port(port)
                .header("TOKEN", queueRes.token())
                .param("concertId", concertsRes[0].id())
            .when()
                .get("/concerts/schedules")
            .then()
                .statusCode(200)
                .extract().as(ConcertScheduleResponse[].class);

        //then
        assertThat(concertSchedulesRes).isNotEmpty();

        //when
        ConcertSeatResponse[] concertSeatsRes = RestAssured
            .given()
                .accept("application/json")
                .port(port)
                .header("TOKEN", queueRes.token())
                .param("concertScheduleId", concertSchedulesRes[0].id())
            .when()
                .get("/concerts/seats")
            .then()
                .statusCode(200)
                .extract().as(ConcertSeatResponse[].class);

        //then
        assertThat(concertSeatsRes).isNotEmpty();

        //given
        ReservationRequest reservationRequest = new ReservationRequest(
            concertSchedulesRes[0].id(), 
            List.of(concertSeatsRes[0].id(), concertSeatsRes[1].id())
        );

        //when
        ReservationResponse reservationRes = RestAssured
            .given()
                .contentType("application/json")
                .accept("application/json")
                .port(port)
                .header("TOKEN", queueRes.token())
                .queryParam("userId", userId)
                .body(reservationRequest)
            .when()
                .post("reservations")
            .then()
                .statusCode(200)
                .extract().as(ReservationResponse.class);

        //then
        assertThat(reservationRes).isNotNull();
        assertThat(reservationRes.totalPrice()).isEqualTo(concertSeatsRes[0].price() + concertSeatsRes[1].price());

        //given
        AssetChargeRequest chargeRequest = new AssetChargeRequest(concertSeatsRes[0].price() + concertSeatsRes[1].price());

        //when
        AssetChargeResponse chargeRes = RestAssured
            .given()
                .contentType("application/json")
                .accept("application/json")
                .port(port)
                .param("userId", userId)
                .body(chargeRequest)
            .when()
                .patch("/asset/charge")
            .then()
                .statusCode(200)
                .extract().as(AssetChargeResponse.class);

        //then
        assertThat(chargeRes).isNotNull();
        assertThat(chargeRes.balance()).isEqualTo(chargeRequest.amount());

        //when
        AssetGetResponse assetRes = RestAssured
            .given()
                .accept("application/json")
                .port(port)
                .param("userId", userId)
            .when()
                .get("/asset")
            .then()
                .statusCode(200)
                .extract().as(AssetGetResponse.class);

        //then
        assertThat(assetRes).isNotNull();
        assertThat(assetRes.balance()).isEqualTo(chargeRequest.amount());

        //given
        PaymentRequest paymentRequest = new PaymentRequest(reservationRes.reservationId());
        //when
        PaymentResponse paymentRes = RestAssured
            .given()
                .contentType("application/json")
                .accept("application/json")
                .port(port)
                .header("TOKEN", queueRes.token())
                .queryParam("userId", userId)
                .body(paymentRequest)
            .when()
                .post("/payment")
            .then()
                .statusCode(200)
                .extract().as(PaymentResponse.class);

        //then
        assertThat(paymentRes).isNotNull();
        assertThat(paymentRes.paidAmount()).isEqualTo(concertSeatsRes[0].price() + concertSeatsRes[1].price());

        //when
        assetRes = RestAssured
            .given()
                .accept("application/json")
                .port(port)
                .param("userId", userId)
            .when()
                .get("/asset")
            .then()
                .statusCode(200)
                .extract().as(AssetGetResponse.class);

        //then
        assertThat(assetRes).isNotNull();
        assertThat(assetRes.balance()).isEqualTo(0);

    }

    @Test
    @DisplayName("오류 발생시 Custom Error Response Return")
    void testErrorResponse() {
        //when
        var errorRes = RestAssured
            .given()
                .accept("application/json")
                .port(port)
                .header("TOKEN", "")
            .when()
                .get("/concerts")
            .then()
                .statusCode(404)
                .extract().as(ErrorResponse.class);

        //then
        assertThat(errorRes).isInstanceOf(ErrorResponse.class);
        assertThat(errorRes.message()).isEqualTo(ExceptionCode.ACTIVE_TOKEN_NOT_FOUND.getMessage());
    }
}
