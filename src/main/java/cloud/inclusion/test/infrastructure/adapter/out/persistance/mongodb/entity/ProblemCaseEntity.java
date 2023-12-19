package cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCaseEntity {

  private Integer x;
  private Integer y;
  private Integer n;
  private Integer result;

}
