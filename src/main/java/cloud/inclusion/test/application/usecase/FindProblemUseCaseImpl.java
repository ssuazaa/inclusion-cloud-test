package cloud.inclusion.test.application.usecase;

import cloud.inclusion.test.domain.model.Problem;
import cloud.inclusion.test.domain.port.in.FindProblemUseCase;
import cloud.inclusion.test.domain.port.out.ProblemRepositoryOut;
import cloud.inclusion.test.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FindProblemUseCaseImpl implements FindProblemUseCase {

  private final ProblemRepositoryOut problemRepositoryOut;

  public FindProblemUseCaseImpl(ProblemRepositoryOut problemRepositoryOut) {
    this.problemRepositoryOut = problemRepositoryOut;
  }

  @Override
  public Flux<Problem> findAll() {
    return problemRepositoryOut.findAll();
  }

  @Override
  public Mono<Problem> findById(UUID id) {
    return problemRepositoryOut.findById(id)
        .switchIfEmpty(Mono.error(() -> new ObjectNotFoundException("PROBLEM_NOT_FOUND",
            String.format("Problem with id '%s' was not found", id))));
  }

}
