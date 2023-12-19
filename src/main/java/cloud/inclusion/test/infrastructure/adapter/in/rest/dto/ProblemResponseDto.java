package cloud.inclusion.test.infrastructure.adapter.in.rest.dto;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProblemResponseDto(UUID id, Integer amount, List<ProblemCaseResponseDto> cases) {

}
