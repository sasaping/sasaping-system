package com.sparta.product.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record PreOrderCreateRequest(
    @NotNull(message = "상품아이디는 필수입니다") UUID productId,
    @NotBlank(message = "사전예약주문타이틀은 필수입니다") String preOrderTitle,
    @NotNull(message = "사전예약시작일자는 필수입니다") LocalDateTime startDateTime,
    @NotNull(message = "사전예약종료일자는 필수입니다") LocalDateTime endDateTime,
    @NotNull(message = "사전예약발송일자는 필수입니다") LocalDateTime releaseDateTime,
    @NotNull(message = "사전예약수량은 필수입니다") Integer availableQuantity) {}
