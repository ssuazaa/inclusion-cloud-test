package cloud.inclusion.test.infrastructure.adapter.in.rest.dto;

import lombok.Builder;

@Builder
public record ProblemCaseRequestDto(Integer x, Integer y, Integer n) {

}
