package cloud.inclusion.test.infrastructure.adapter.in.rest.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import cloud.inclusion.test.domain.model.Problem;
import cloud.inclusion.test.infrastructure.adapter.in.rest.dto.ProblemRequestDto;
import cloud.inclusion.test.infrastructure.adapter.in.rest.dto.ProblemResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface RestProblemMapper {

  ProblemResponseDto toResponse(Problem problem);

  @Mapping(target = "id", ignore = true)
  Problem toDomain(ProblemRequestDto problemRequestDto);

}

