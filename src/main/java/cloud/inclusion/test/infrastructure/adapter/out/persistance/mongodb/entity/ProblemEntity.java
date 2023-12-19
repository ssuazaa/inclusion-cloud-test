package cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.entity;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.MongoId;

public record ProblemEntity(@MongoId String id, Integer amount, List<ProblemCaseEntity> cases) {

}
