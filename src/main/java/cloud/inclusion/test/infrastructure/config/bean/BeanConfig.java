package cloud.inclusion.test.infrastructure.config.bean;

import cloud.inclusion.test.application.usecase.CreateProblemUseCaseImpl;
import cloud.inclusion.test.application.usecase.FindProblemUseCaseImpl;
import cloud.inclusion.test.domain.port.in.CreateProblemUseCase;
import cloud.inclusion.test.domain.port.in.FindProblemUseCase;
import cloud.inclusion.test.domain.port.out.ProblemRepositoryOut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  private final ProblemRepositoryOut problemRepositoryOut;

  public BeanConfig(ProblemRepositoryOut problemRepositoryOut) {
    this.problemRepositoryOut = problemRepositoryOut;
  }

  @Bean
  public FindProblemUseCase findProblemUseCase() {
    return new FindProblemUseCaseImpl(this.problemRepositoryOut);
  }

  @Bean
  public CreateProblemUseCase createProblemUseCase() {
    return new CreateProblemUseCaseImpl(this.problemRepositoryOut);
  }

}
