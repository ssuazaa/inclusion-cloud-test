package cloud.inclusion.test.domain.port.in;

import cloud.inclusion.test.domain.model.Problem;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindProblemUseCase {

  Flux<Problem> findAll();

  Mono<Problem> findById(UUID id);

}
