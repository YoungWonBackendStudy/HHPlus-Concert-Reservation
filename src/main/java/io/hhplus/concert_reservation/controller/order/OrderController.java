package io.hhplus.concert_reservation.controller.order;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert_reservation.controller.order.dto.OrderDto;
import io.hhplus.concert_reservation.controller.order.dto.OrderPayDto;

@RestController
public class OrderController {
    @PostMapping("order")
    public OrderDto.Response order(
        @RequestParam("passToken") String passToken,
        @RequestBody OrderDto.Request orderRequest
    ) {
        List<OrderDto.OrderedSeatInfo> orderedSeats = List.of(new OrderDto.OrderedSeatInfo(0, "R1", 120000));
        return new OrderDto.Response(0, 120000, new Date(), new Date(System.currentTimeMillis() + 1000l * 60 * 5), orderedSeats);
    }

    @PatchMapping("order/pay")
    public OrderPayDto.Response payOrder(
        @RequestBody OrderPayDto.Request orderPayRequest
    ) {
        return new OrderPayDto.Response(120000, 480000);
    }
}
