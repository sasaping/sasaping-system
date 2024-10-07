package com.sparta.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sparta.user.application.dto.TierResponse;
import com.sparta.user.application.service.TierService;
import com.sparta.user.domain.model.Tier;
import com.sparta.user.domain.repository.TierRepository;
import com.sparta.user.exception.UserErrorCode;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.TierRequest;
import com.sparta.user.presentation.request.TierRequest.Create;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class TierApplicationTests {

  @MockBean
  private TierRepository tierRepository;

  @Autowired
  private TierService tierService;

  @Test
  void 티어_생성시_이미_존재하는_티어면_예외발생() {
    // given
    TierRequest.Create request = new Create("골드핑", 150000L);
    given(tierRepository.findByName(request.getName()))
        .willReturn(Optional.of(new Tier()));

    // when & then
    UserException exception = assertThrows(UserException.class,
        () -> tierService.createTier(request));
    assertEquals("이미 존재하는 등급입니다.", exception.getMessage());
    then(tierRepository).should().findByName(request.getName());
    then(tierRepository).should(never()).save(any(Tier.class));
  }

  @Test
  void 티어_생성시_존재하지_않는_티어면_저장() {
    // given
    TierRequest.Create request = new Create("골드핑", 150000L);
    given(tierRepository.findByName(request.getName()))
        .willReturn(Optional.empty());

    // when
    tierService.createTier(request);

    // then
    then(tierRepository).should().findByName(request.getName());
    then(tierRepository).should().save(any(Tier.class));
  }

  @Test
  void 티어_목록_조회시_모든_티어가_반환됨() {
    // given
    List<Tier> tiers = Arrays.asList(
        new Tier(1L, "애기핑", 1000L),
        new Tier(2L, "실버핑", 5000L)
    );
    given(tierRepository.findAll()).willReturn(tiers);

    // when
    List<TierResponse.Get> tierResponseList = tierService.getTierList();

    // then
    assertEquals(2, tierResponseList.size());
    assertEquals("애기핑", tierResponseList.get(0).getName());
    assertEquals("실버핑", tierResponseList.get(1).getName());
    then(tierRepository).should().findAll();
  }

  @Test
  void 티어_목록_조회시_빈_목록이_반환됨() {
    // given
    given(tierRepository.findAll()).willReturn(Collections.emptyList());

    // when
    List<TierResponse.Get> tierResponseList = tierService.getTierList();

    // then
    assertTrue(tierResponseList.isEmpty());
    then(tierRepository).should().findAll();
  }

  @Test
  void 티어_수정시_존재하는_티어면_수정성공() {
    // given
    Long tierId = 1L;
    TierRequest.Update request = new TierRequest.Update("골드핑", 200000L);
    Tier existingTier = new Tier(tierId, "실버핑", 100000L);

    given(tierRepository.findById(tierId)).willReturn(Optional.of(existingTier));

    // when
    tierService.updateTier(tierId, request);

    // then
    assertEquals("골드핑", existingTier.getName()); // 업데이트된 이름 확인
    assertEquals(200000L, existingTier.getThresholdPrice()); // 업데이트된 포인트 확인
    then(tierRepository).should().findById(tierId);
    then(tierRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  void 티어_수정시_존재하지_않는_티어면_예외발생() {
    // given
    Long tierId = 1L;
    TierRequest.Update request = new TierRequest.Update("골드핑", 200000L);

    given(tierRepository.findById(tierId)).willReturn(Optional.empty());

    // when & then
    UserException exception = assertThrows(UserException.class,
        () -> tierService.updateTier(tierId, request));

    assertEquals(UserErrorCode.TIER_NOT_FOUND.getMessage(), exception.getMessage());
    then(tierRepository).should().findById(tierId);
    then(tierRepository).should(never()).save(any(Tier.class));
  }

  @Test
  void 티어_삭제시_존재하는_티어면_삭제성공() {
    // given
    Long tierId = 1L;
    Tier existingTier = new Tier(tierId, "골드핑", 150000L);

    given(tierRepository.findById(tierId)).willReturn(Optional.of(existingTier));

    // when
    tierService.deleteTier(tierId);

    // then
    then(tierRepository).should().findById(tierId);
    then(tierRepository).should().delete(existingTier);
  }

  @Test
  void 티어_삭제시_존재하지_않는_티어면_예외발생() {
    // given
    Long tierId = 1L;

    given(tierRepository.findById(tierId)).willReturn(Optional.empty());

    // when & then
    UserException exception = assertThrows(UserException.class,
        () -> tierService.deleteTier(tierId));

    assertEquals(UserErrorCode.TIER_NOT_FOUND.getMessage(), exception.getMessage());
    then(tierRepository).should().findById(tierId);
    then(tierRepository).should(never()).delete(any(Tier.class)); // 삭제 호출이 없어야 함
  }

}
