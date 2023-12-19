package cloud.inclusion.test.application.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cloud.inclusion.test.domain.model.Problem;
import cloud.inclusion.test.domain.model.ProblemCase;
import cloud.inclusion.test.domain.port.in.CreateProblemUseCase;
import cloud.inclusion.test.domain.port.out.ProblemRepositoryOut;
import cloud.inclusion.test.infrastructure.adapter.out.persistance.gateway.ProblemRepositoryOutImpl;
import cloud.inclusion.test.infrastructure.config.exceptions.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreateProblemUseCaseImplTest {

  @MockBean
  ProblemRepositoryOut problemRepositoryOut;

  CreateProblemUseCase createProblemUseCase;

  @BeforeEach
  public void setUp() {
    this.problemRepositoryOut = mock(ProblemRepositoryOutImpl.class);
    this.createProblemUseCase = new CreateProblemUseCaseImpl(this.problemRepositoryOut);
  }

  @Test
  @DisplayName("testCreate() -> Good case")
  void testCreate() {
    // Arrange
    var problemRequest = Problem.builder()
        .amount(1)
        .cases(List.of(ProblemCase.builder()
            .x(7)
            .y(5)
            .n(12345)
            .build()))
        .build();
    var problemSaved = Problem.builder()
        .id(UUID.randomUUID())
        .amount(1)
        .cases(List.of(ProblemCase.builder()
            .x(7)
            .y(5)
            .n(12345)
            .build()))
        .build();

    when(this.problemRepositoryOut.save(any(Problem.class)))
        .thenReturn(Mono.just(problemSaved));

    // Act
    var result = this.createProblemUseCase.create(problemRequest);

    // Assert
    StepVerifier.create(result)
        .expectNextMatches(Objects::nonNull)
        .verifyComplete();

    verify(this.problemRepositoryOut, times(1)).save(any(Problem.class));
  }

  @Test
  @DisplayName("testCreate() -> Error case")
  void testCreateError() {
    // Arrange
    var problemRequest = Problem.builder()
        .amount(2)
        .cases(List.of(ProblemCase.builder()
            .x(7)
            .y(5)
            .n(12345)
            .build()))
        .build();

    // Act
    var result = this.createProblemUseCase.create(problemRequest);

    // Assert
    StepVerifier.create(result)
        .expectErrorMatches((Throwable throwable) ->
            throwable instanceof ConstraintViolationException exception
                && exception.getKey().equals("CASES_AMOUNT_INVALID"))
        .verify();

    verify(this.problemRepositoryOut, times(0)).save(any(Problem.class));
  }

}
