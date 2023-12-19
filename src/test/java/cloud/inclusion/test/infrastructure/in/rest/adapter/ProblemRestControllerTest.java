package cloud.inclusion.test.infrastructure.in.rest.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.LOCATION;

import cloud.inclusion.test.domain.model.Problem;
import cloud.inclusion.test.domain.model.ProblemCase;
import cloud.inclusion.test.domain.port.in.CreateProblemUseCase;
import cloud.inclusion.test.domain.port.in.FindProblemUseCase;
import cloud.inclusion.test.infrastructure.adapter.in.rest.adapter.ProblemRestController;
import cloud.inclusion.test.infrastructure.adapter.in.rest.dto.ProblemCaseRequestDto;
import cloud.inclusion.test.infrastructure.adapter.in.rest.dto.ProblemCaseResponseDto;
import cloud.inclusion.test.infrastructure.adapter.in.rest.dto.ProblemRequestDto;
import cloud.inclusion.test.infrastructure.adapter.in.rest.dto.ProblemResponseDto;
import cloud.inclusion.test.infrastructure.adapter.in.rest.mapper.RestProblemMapper;
import cloud.inclusion.test.infrastructure.config.errorhandler.ErrorResponseDto;
import cloud.inclusion.test.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {ProblemRestController.class})
class ProblemRestControllerTest {

  @MockBean
  private RestProblemMapper restProblemMapper;

  @MockBean
  private FindProblemUseCase findProblemUseCase;

  @MockBean
  private CreateProblemUseCase createProblemUseCase;

  @Autowired
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    when(this.restProblemMapper.toResponse(any(Problem.class)))
        .thenAnswer(invocation -> {
          Problem problem = invocation.getArgument(0);
          return ProblemResponseDto.builder()
              .id(problem.getId())
              .amount(problem.getAmount())
              .cases(problem.getCases().stream()
                  .map((ProblemCase problemCase) -> ProblemCaseResponseDto.builder()
                      .x(problemCase.getX())
                      .y(problemCase.getY())
                      .n(problemCase.getN())
                      .result(problemCase.getResult())
                      .build())
                  .collect(Collectors.toList()))
              .build();
        });
    when(this.restProblemMapper.toDomain(any(ProblemRequestDto.class)))
        .thenAnswer(invocation -> {
          ProblemRequestDto problemRequestDto = invocation.getArgument(0);
          return Problem.builder()
              .amount(problemRequestDto.getAmount())
              .cases(problemRequestDto.getCases().stream()
                  .map((ProblemCaseRequestDto problemCaseRequestDto) -> ProblemCase.builder()
                      .x(problemCaseRequestDto.getX())
                      .y(problemCaseRequestDto.getY())
                      .n(problemCaseRequestDto.getN())
                      .result(0)
                      .build())
                  .collect(Collectors.toList()))
              .build();
        });
  }

  @Test
  @DisplayName("testFindAll() -> Good case [not empty]")
  void testFindAll() {
    // Arrange
    var listOfProblems = List.of(Problem.builder()
        .id(UUID.randomUUID())
        .amount(1)
        .cases(List.of(ProblemCase.builder()
            .y(7)
            .y(5)
            .n(12345)
            .result(12339)
            .build()))
        .build());

    when(this.findProblemUseCase.findAll()).thenReturn(Flux.fromIterable(listOfProblems));

    // Act
    var response = this.webTestClient.get().uri("/api/v1/problems")
        .header(ACCEPT, "application/json")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(ProblemResponseDto.class)
        .returnResult()
        .getResponseBody();

    // Assert
    assertNotNull(response);
    assertFalse(response.isEmpty());
    assertAll("Validate each element", () -> {
      for (var i = 0; i < listOfProblems.size(); i++) {
        assertEquals(response.get(i).getId(), listOfProblems.get(i).getId());
        assertEquals(response.get(i).getAmount(), listOfProblems.get(i).getAmount());
        assertEquals(response.get(i).getCases().size(), listOfProblems.get(i).getCases().size());
      }
    });

    verify(this.findProblemUseCase, times(1)).findAll();
    verify(this.restProblemMapper, times(listOfProblems.size())).toResponse(any(Problem.class));
  }

  @Test
  @DisplayName("testFindAll() -> Good case [empty]")
  void testFindAllEmpty() {
    // Arrange
    when(this.findProblemUseCase.findAll()).thenReturn(Flux.empty());

    // Act
    var response = this.webTestClient.get().uri("/api/v1/problems")
        .header(ACCEPT, "application/json")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(ProblemResponseDto.class)
        .returnResult()
        .getResponseBody();

    // Assert
    assertNotNull(response);
    assertTrue(response.isEmpty());

    verify(this.findProblemUseCase, times(1)).findAll();
    verify(this.restProblemMapper, times(0)).toResponse(any(Problem.class));
  }

  @Test
  @DisplayName("testFindById() -> Good case [found]")
  void testFindById() {
    // Arrange
    var id = UUID.randomUUID();
    var problemSaved = Problem.builder()
        .id(UUID.randomUUID())
        .amount(1)
        .cases(List.of(ProblemCase.builder()
            .y(7)
            .y(5)
            .n(12345)
            .result(12339)
            .build()))
        .build();

    when(this.findProblemUseCase.findById(any(UUID.class))).thenReturn(Mono.just(problemSaved));

    // Act
    var response = this.webTestClient.get().uri("/api/v1/problems/{id}", id)
        .header(ACCEPT, "application/json")
        .exchange()
        .expectStatus().isOk()
        .expectBody(ProblemResponseDto.class)
        .returnResult()
        .getResponseBody();

    // Assert
    assertNotNull(response);
    assertThat(problemSaved.getId()).isEqualTo(response.getId());
    assertThat(problemSaved.getAmount()).isEqualTo(response.getAmount());

    verify(this.findProblemUseCase, times(1)).findById(any(UUID.class));
    verify(this.restProblemMapper, times(1)).toResponse(any(Problem.class));
  }

  @Test
  @DisplayName("testFindById() -> Error case [not found]")
  void testFindByIdException() {
    // Arrange
    var id = UUID.randomUUID();

    when(this.findProblemUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.error(() -> new ObjectNotFoundException("PROBLEM_NOT_FOUND", "")));

    // Act
    var response = this.webTestClient.get().uri("/api/v1/problems/{id}", id)
        .header(ACCEPT, "application/json")
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ErrorResponseDto.class)
        .returnResult()
        .getResponseBody();

    // Assert
    assertNotNull(response);
    assertThat(response.getKey()).isEqualTo("PROBLEM_NOT_FOUND");

    verify(this.findProblemUseCase, times(1)).findById(any(UUID.class));
    verify(this.restProblemMapper, times(0)).toResponse(any(Problem.class));
  }

  @Test
  @DisplayName("testCreate() -> Good case [created]")
  void testCreate() {
    // Arrange
    var idSaved = UUID.randomUUID();
    var problemRequest = ProblemRequestDto.builder()
        .amount(1)
        .cases(List.of(ProblemCaseRequestDto.builder()
            .x(7)
            .y(5)
            .n(12345)
            .build()))
        .build();

    when(this.createProblemUseCase.create(any(Problem.class))).thenReturn(Mono.just(idSaved));

    // Act
    var response = this.webTestClient.post().uri("/api/v1/problems")
        .bodyValue(problemRequest)
        .header(ACCEPT, "application/json")
        .exchange()
        .expectStatus().isCreated()
        .returnResult(String.class)
        .getResponseHeaders().getFirst(LOCATION);

    // Assert
    assertNotNull(response);
    assertThat(response).isEqualTo("/api/v1/problems/" + idSaved);

    verify(this.createProblemUseCase, times(1)).create(any(Problem.class));
  }

}
