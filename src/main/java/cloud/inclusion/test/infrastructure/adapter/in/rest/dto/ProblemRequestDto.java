package cloud.inclusion.test.infrastructure.adapter.in.rest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemRequestDto {

  private Integer amount;
  private List<ProblemCaseRequestDto> cases;

}
