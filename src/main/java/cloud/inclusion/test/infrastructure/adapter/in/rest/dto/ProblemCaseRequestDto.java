package cloud.inclusion.test.infrastructure.adapter.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCaseRequestDto {

  private Integer x;
  private Integer y;
  private Integer n;

}
