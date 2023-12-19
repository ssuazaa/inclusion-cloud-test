package cloud.inclusion.test.infrastructure.adapter.in.rest.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponseDto {

  private UUID id;
  private Integer amount;
  private List<ProblemCaseResponseDto> cases;

}
