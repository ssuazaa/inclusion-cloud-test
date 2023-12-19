package cloud.inclusion.test.domain.port.out;

import cloud.inclusion.test.domain.model.Problem;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProblemRepositoryOut {

  Flux<Problem> findAll();

  Mono<Problem> findById(UUID id);

  Mono<Problem> save(Problem problem);

}
