package cloud.inclusion.test.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCase {

  private Integer x;
  private Integer y;
  private Integer n;
  private Integer result;

}
