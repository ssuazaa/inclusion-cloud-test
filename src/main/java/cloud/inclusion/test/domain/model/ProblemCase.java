package cloud.inclusion.test.domain.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record ProblemCase(Integer x, Integer y, Integer n, Integer result) {

}
