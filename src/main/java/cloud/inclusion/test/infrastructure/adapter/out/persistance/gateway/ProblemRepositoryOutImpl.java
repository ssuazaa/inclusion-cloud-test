package cloud.inclusion.test.infrastructure.adapter.out.persistance.gateway;

import cloud.inclusion.test.domain.model.Problem;
import cloud.inclusion.test.domain.port.out.ProblemRepositoryOut;
import cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.adapter.ProblemRepositoryMongoDB;
import java.util.UUID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProblemRepositoryOutImpl implements ProblemRepositoryOut {

  private final ProblemRepositoryMongoDB repository;

  public ProblemRepositoryOutImpl(ProblemRepositoryMongoDB repository) {
    this.repository = repository;
  }

  @Override
  public Flux<Problem> findAll() {
    return this.repository.findAll();
  }

  @Override
  public Mono<Problem> findById(UUID id) {
    return this.repository.findById(id);
  }

  @Override
  public Mono<Problem> save(Problem problem) {
    return this.repository.save(problem);
  }

}
