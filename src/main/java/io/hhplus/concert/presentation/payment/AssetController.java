package io.hhplus.concert.presentation.payment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.presentation.payment.dto.AssetChargeDto;
import io.hhplus.concert.presentation.payment.dto.AssetGetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "사용자 자산")
@RestController
public class AssetController {

    @Operation(summary = "잔액 조회 API")
    @Parameters(value = {
        @Parameter(name = "userId", required = true, description = "잔액을 충전할 사용자 ID")
    })
    @GetMapping("asset")
    public AssetGetDto.Response getAsset(
        @RequestParam("userId") long userId
    ) {
        return new AssetGetDto.Response(300000);
    }

    @Operation(summary = "잔액 충전 API")
    @Parameters(value = {
        @Parameter(name = "userId", required = true, description = "잔액을 충전할 사용자 ID")
    })
    @PatchMapping("asset/charge")
    public AssetChargeDto.Response chargeAsset(
        @RequestParam("userId") long userId,
        @RequestBody AssetChargeDto.Request assetChargeRequest
    ) {
        return new AssetChargeDto.Response(600000);
    }
}
