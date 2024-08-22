package io.hhplus.concert.interfaces.presentation.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.application.user.UserAssetFacade;
import io.hhplus.concert.interfaces.presentation.user.dto.AssetChargeRequest;
import io.hhplus.concert.interfaces.presentation.user.dto.AssetChargeResponse;
import io.hhplus.concert.interfaces.presentation.user.dto.AssetGetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "사용자 자산")
@RestController
public class AssetController {
    private final UserAssetFacade userAssetFacade;

    public AssetController(UserAssetFacade userAssetFacade) {
        this.userAssetFacade = userAssetFacade;
    }

    @Operation(summary = "잔액 조회 API")
    @Parameters(value = {
            @Parameter(name = "userId", required = true, description = "잔액을 충전할 사용자 ID")
    })
    @GetMapping("asset")
    public AssetGetResponse getAsset(
            @RequestParam("userId") long userId) {
        return new AssetGetResponse(userAssetFacade.getBalance(userId));
    }

    @Operation(summary = "잔액 충전 API")
    @Parameters(value = {
            @Parameter(name = "userId", required = true, description = "잔액을 충전할 사용자 ID")
    })
    @PatchMapping("asset/charge")
    public AssetChargeResponse chargeAsset(
            @RequestParam("userId") long userId,
            @RequestBody AssetChargeRequest assetChargeRequest) {
        return new AssetChargeResponse(userAssetFacade.chargeBalance(userId, assetChargeRequest.amount()));
    }
}
