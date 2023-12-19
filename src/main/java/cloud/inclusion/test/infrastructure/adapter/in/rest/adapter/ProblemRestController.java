package cloud.inclusion.test.infrastructure.adapter.in.rest.adapter;

import cloud.inclusion.test.domain.port.in.CreateProblemUseCase;
import cloud.inclusion.test.domain.port.in.FindProblemUseCase;
import cloud.inclusion.test.infrastructure.adapter.in.rest.dto.ProblemRequestDto;
import cloud.inclusion.test.infrastructure.adapter.in.rest.dto.ProblemResponseDto;
import cloud.inclusion.test.infrastructure.adapter.in.rest.mapper.RestProblemMapper;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/problems")
public class ProblemRestController {

  private final FindProblemUseCase findProblemUseCase;
  private final CreateProblemUseCase createProblemUseCase;
  private final RestProblemMapper restProblemMapper;

  public ProblemRestController(FindProblemUseCase findProblemUseCase,
      CreateProblemUseCase createProblemUseCase, RestProblemMapper restProblemMapper) {
    this.findProblemUseCase = findProblemUseCase;
    this.createProblemUseCase = createProblemUseCase;
    this.restProblemMapper = restProblemMapper;
  }

  @GetMapping
  public Flux<ProblemResponseDto> findAll() {
    return this.findProblemUseCase.findAll()
        .map(this.restProblemMapper::toResponse);
  }

  @GetMapping("/{id}")
  public Mono<ProblemResponseDto> findById(@PathVariable UUID id) {
    return this.findProblemUseCase.findById(id)
        .map(this.restProblemMapper::toResponse);
  }

  @PostMapping
  public Mono<ResponseEntity<Void>> create(@RequestBody ProblemRequestDto problemRequestDto,
      UriComponentsBuilder uriBuilder) {
    return this.createProblemUseCase.create(restProblemMapper.toDomain(problemRequestDto))
        .map((UUID id) -> ResponseEntity.created(uriBuilder
                .path("/api/v1/problems/{id}")
                .buildAndExpand(id)
                .toUri())
            .build());
  }

}
