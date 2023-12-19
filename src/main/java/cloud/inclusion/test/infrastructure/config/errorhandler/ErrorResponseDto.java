package cloud.inclusion.test.infrastructure.config.errorhandler;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

  private String key;
  private String message;
  private LocalDateTime dateTime;

}
