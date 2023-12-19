package cloud.inclusion.test.infrastructure.config.exceptions;

public class ConstraintViolationException extends BaseException {

  public ConstraintViolationException(String key, String message) {
    super(key, message, 400);
  }

}
