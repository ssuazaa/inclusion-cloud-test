package cloud.inclusion.test.application.usecase;

import cloud.inclusion.test.domain.model.Problem;
import cloud.inclusion.test.domain.model.ProblemCase;
import cloud.inclusion.test.domain.port.in.CreateProblemUseCase;
import cloud.inclusion.test.domain.port.out.ProblemRepositoryOut;
import cloud.inclusion.test.infrastructure.config.exceptions.ConstraintViolationException;
import java.util.UUID;
import java.util.stream.Collectors;
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
        .map(Problem::getId);
  }

  private Mono<Void> validateCases(Problem problem) {
    if (problem.getAmount() != problem.getCases().size()) {
      return Mono.error(() -> new ConstraintViolationException("CASES_AMOUNT_INVALID", ""));
    }
    return Mono.empty();
  }

  private Mono<Problem> operateCases(Problem problem) {
    var casesSolved = problem.getCases().stream()
        .map(this::operateCase)
        .collect(Collectors.toList());
    var problemSolved = problem.toBuilder()
        .id(UUID.randomUUID())
        .cases(casesSolved)
        .build();
    return Mono.just(problemSolved);
  }

  private ProblemCase operateCase(ProblemCase problemCase) {
    var result =
        (problemCase.getN() / problemCase.getX()) * problemCase.getX() + problemCase.getY();
    if (result > problemCase.getN()) {
      result -= problemCase.getX();
    }
    return problemCase.toBuilder()
        .result(result)
        .build();
  }

}
