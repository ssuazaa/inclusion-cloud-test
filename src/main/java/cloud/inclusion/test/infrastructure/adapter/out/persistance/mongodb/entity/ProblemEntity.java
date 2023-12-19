package cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemEntity {

  @MongoId
  private String id;
  private Integer amount;
  private List<ProblemCaseEntity> cases;

}
