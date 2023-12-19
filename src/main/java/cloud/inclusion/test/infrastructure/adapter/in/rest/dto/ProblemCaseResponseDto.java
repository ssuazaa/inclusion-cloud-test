package cloud.inclusion.test.infrastructure.adapter.in.rest.dto;

import lombok.Builder;

@Builder
public record ProblemCaseResponseDto(Integer x, Integer y, Integer n, Integer result) {

}
