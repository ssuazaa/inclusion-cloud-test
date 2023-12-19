package cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import cloud.inclusion.test.domain.model.Problem;
import cloud.inclusion.test.infrastructure.adapter.out.persistance.mongodb.entity.ProblemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface MongoDBProblemMapper {

  ProblemEntity toEntity(Problem problem);

  Problem toDomain(ProblemEntity problemEntity);

}

