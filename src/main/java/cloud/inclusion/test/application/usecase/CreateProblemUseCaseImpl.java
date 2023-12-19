package cloud.inclusion.test.application.usecase;

import cloud.inclusion.test.domain.model.Problem;
import cloud.inclusion.test.domain.model.ProblemCase;
import cloud.inclusion.test.domain.port.in.CreateProblemUseCase;
import cloud.inclusion.test.domain.port.out.ProblemRepositoryOut;
import cloud.inclusion.test.infrastructure.config.exceptions.ConstraintViolationException;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class CreateProblemUseCaseImpl implements CreateProblemUseCase {

  private final ProblemRepositoryOut problemRepositoryOut;

  public CreateProblemUseCaseImpl(ProblemRepositoryOut problemRepositoryOut) {
    this.problemRepositoryOut = problemRepositoryOut;
  }

  @Override
  public Mono<UUID> create(Problem problem) {
    return validateCases(problem)
        .then(operateCases(problem))
        .flatMap(problemRepositoryOut::save)
        .map(Problem::id);
  }

  private Mono<Void> validateCases(Problem problem) {
    if (problem.amount() != problem.cases().size()) {
      return Mono.error(() -> new ConstraintViolationException("CASES_AMOUNT_INVALID", ""));
    }
    return Mono.empty();
  }

  private Mono<Problem> operateCases(Problem problem) {
    var casesSolved = problem.cases().stream()
        .map(this::operateCase)
        .toList();
    var problemSolved = problem.toBuilder()
        .id(UUID.randomUUID())
        .cases(casesSolved)
        .build();
    return Mono.just(problemSolved);
  }

  private ProblemCase operateCase(ProblemCase problemCase) {
    var result = (problemCase.n() / problemCase.x()) * problemCase.x() + problemCase.y();
    if (result > problemCase.n()) {
      result -= problemCase.x();
    }
    return problemCase.toBuilder()
        .result(result)
        .build();
  }

}
