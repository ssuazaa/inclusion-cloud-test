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
import java.util.LinkedList;
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
    var problemRequest1 = Problem.builder()
        .amount(1)
        .cases(List.of(ProblemCase.builder()
            .x(7)
            .y(5)
            .n(12345)
            .build()))
        .build();
    var problemRequest2 = Problem.builder()
        .amount(1)
        .cases(List.of(ProblemCase.builder()
            .x(5)
            .y(0)
            .n(4)
            .build()))
        .build();
    var problemSaved1 = Problem.builder()
        .id(UUID.randomUUID())
        .amount(1)
        .cases(List.of(ProblemCase.builder()
            .x(7)
            .y(5)
            .n(12345)
            .build()))
        .build();
    var problemSaved2 = Problem.builder()
        .id(UUID.randomUUID())
        .amount(1)
        .cases(List.of(ProblemCase.builder()
            .x(5)
            .y(0)
            .n(4)
            .build()))
        .build();
    var problemSavedIterations = new LinkedList<>(
        List.of(Mono.just(problemSaved1), Mono.just(problemSaved2)));

    when(this.problemRepositoryOut.save(any(Problem.class)))
        .thenAnswer(invocationOnMock -> problemSavedIterations.poll());

    // Act
    var result1 = this.createProblemUseCase.create(problemRequest1);
    var result2 = this.createProblemUseCase.create(problemRequest2);

    // Assert
    StepVerifier.create(result1)
        .expectNextMatches(Objects::nonNull)
        .verifyComplete();
    StepVerifier.create(result2)
        .expectNextMatches(Objects::nonNull)
        .verifyComplete();

    verify(this.problemRepositoryOut, times(2)).save(any(Problem.class));
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
        .expectErrorMatches((Throwable throwable) -> {
          if (throwable instanceof ConstraintViolationException) {
            var exception = (ConstraintViolationException) throwable;
            return exception.getKey().equals("CASES_AMOUNT_INVALID");
          }
          return false;
        })
        .verify();

    verify(this.problemRepositoryOut, times(0)).save(any(Problem.class));
  }

}
