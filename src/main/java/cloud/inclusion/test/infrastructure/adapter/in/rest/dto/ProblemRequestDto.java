package cloud.inclusion.test.infrastructure.adapter.in.rest.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ProblemRequestDto(Integer amount, List<ProblemCaseRequestDto> cases) {

}
