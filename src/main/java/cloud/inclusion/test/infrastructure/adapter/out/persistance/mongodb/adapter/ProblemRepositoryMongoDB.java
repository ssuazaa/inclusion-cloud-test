package cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.adapter;

import cloud.inclusion.test.domain.model.Problem;
import cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.config.MongoDBRepositoryConfig;
import cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.mapper.MongoDBProblemMapper;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProblemRepositoryMongoDB {

  private final MongoDBProblemMapper mapper;
  private final MongoDBRepositoryConfig repository;

  public ProblemRepositoryMongoDB(MongoDBProblemMapper mapper,
      MongoDBRepositoryConfig repository) {
    this.mapper = mapper;
    this.repository = repository;
  }

  public Flux<Problem> findAll() {
    return this.repository.findAll()
        .map(this.mapper::toDomain);
  }

  public Mono<Problem> findById(UUID id) {
    return this.repository.findById(id.toString())
        .map(this.mapper::toDomain);
  }

  public Mono<Problem> save(Problem problem) {
    return this.repository.save(this.mapper.toEntity(problem))
        .map(this.mapper::toDomain);
  }

}
