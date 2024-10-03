package com.sparta.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sparta.user.application.service.TierService;
import com.sparta.user.domain.model.Tier;
import com.sparta.user.domain.repository.TierRepository;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.TierRequest;
import com.sparta.user.presentation.request.TierRequest.Create;
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

}
