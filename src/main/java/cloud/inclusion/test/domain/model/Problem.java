package cloud.inclusion.test.domain.model;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class Problem {

  private UUID id;
  private Integer amount;
  private List<ProblemCase> cases;

}
