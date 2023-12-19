package cloud.inclusion.test.domain.model;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record Problem(UUID id, Integer amount, List<ProblemCase> cases) {

}
