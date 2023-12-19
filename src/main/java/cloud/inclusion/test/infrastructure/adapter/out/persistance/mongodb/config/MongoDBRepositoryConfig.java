package cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.config;

import cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.entity.ProblemEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoDBRepositoryConfig extends ReactiveMongoRepository<ProblemEntity, String> {

}
