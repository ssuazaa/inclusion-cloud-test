package cloud.inclusion.test.infrastructure.config.errorhandler;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ErrorResponseDto(String key,
                               String message,
                               LocalDateTime dateTime) {

}
