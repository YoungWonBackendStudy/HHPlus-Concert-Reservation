package io.hhplus.concert.controller.payment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.controller.payment.dto.AssetChargeDto;
import io.hhplus.concert.controller.payment.dto.AssetGetDto;

@RestController
public class AssetController {
    @GetMapping("asset")
    public AssetGetDto.Response getAsset(
        @RequestParam("userId") long userId
    ) {
        return new AssetGetDto.Response(300000);
    }

    @PatchMapping("asset/charge")
    public AssetChargeDto.Response chargeAsset(
        @RequestParam("userId") long userId,
        @RequestBody AssetChargeDto.Request assetChargeRequest
    ) {
        return new AssetChargeDto.Response(600000);
    }
}
