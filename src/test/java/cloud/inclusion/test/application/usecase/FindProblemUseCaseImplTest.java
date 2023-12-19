package cloud.inclusion.test.application.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cloud.inclusion.test.domain.model.Problem;
import cloud.inclusion.test.domain.model.ProblemCase;
import cloud.inclusion.test.domain.port.in.FindProblemUseCase;
import cloud.inclusion.test.domain.port.out.ProblemRepositoryOut;
import cloud.inclusion.test.infrastructure.adapter.out.persistance.gateway.ProblemRepositoryOutImpl;
import cloud.inclusion.test.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FindProblemUseCaseImplTest {

  @MockBean
  ProblemRepositoryOut problemRepositoryOut;

  FindProblemUseCase findProblemUseCase;

  @BeforeEach
  public void setUp() {
    this.problemRepositoryOut = mock(ProblemRepositoryOutImpl.class);
    this.findProblemUseCase = new FindProblemUseCaseImpl(this.problemRepositoryOut);
  }

  @Test
  @DisplayName("testFindAll() -> Good case [not empty and empty]")
  void testFindAll() {
    // Assert
    var elements = List.of(Problem.builder()
        .id(UUID.randomUUID())
        .amount(1)
        .cases(List.of(ProblemCase.builder()
            .y(7)
            .y(5)
            .n(12345)
            .result(12339)
            .build()))
        .build());
    var findAllIterations = new LinkedList<>(List.of(Flux.fromIterable(elements),
        Flux.empty()));

    when(this.findProblemUseCase.findAll())
        .thenAnswer(invocationOnMock -> findAllIterations.poll());

    // Act
    var result1 = this.findProblemUseCase.findAll();
    var result2 = this.findProblemUseCase.findAll();

    // Arrange
    StepVerifier.create(result1)
        .expectNextCount(1)
        .verifyComplete();

    StepVerifier.create(result2)
        .expectNextCount(0)
        .verifyComplete();

    verify(this.problemRepositoryOut, times(2)).findAll();
  }

  @Test
  @DisplayName("testFindById() -> Good case [exists]")
  void testFindById() {
    // Arrange
    var id = UUID.randomUUID();
    var problemSaved = Problem.builder()
        .id(UUID.randomUUID())
        .amount(1)
        .cases(List.of(ProblemCase.builder()
            .x(7)
            .y(5)
            .n(12345)
            .build()))
        .build();

    when(this.problemRepositoryOut.findById(any(UUID.class)))
        .thenReturn(Mono.just(problemSaved));

    // Act
    var result = this.findProblemUseCase.findById(id);

    // Assert
    StepVerifier.create(result)
        .expectNextMatches(Objects::nonNull)
        .verifyComplete();

    verify(this.problemRepositoryOut, times(1)).findById(any(UUID.class));
  }

  @Test
  @DisplayName("testFindById() -> Error case [not exists]")
  void testFindByIdNotFound() {
    // Arrange
    var id = UUID.randomUUID();

    when(this.problemRepositoryOut.findById(any(UUID.class)))
        .thenReturn(Mono.empty());

    // Act
    var result = this.findProblemUseCase.findById(id);

    // Assert
    StepVerifier.create(result)
        .expectErrorMatches((Throwable throwable) -> {
          if (throwable instanceof ObjectNotFoundException) {
            var exception = (ObjectNotFoundException) throwable;
            return exception.getKey().equals("PROBLEM_NOT_FOUND");
          }
          return false;
        })
        .verify();

    verify(this.problemRepositoryOut, times(1)).findById(any(UUID.class));
  }

}
