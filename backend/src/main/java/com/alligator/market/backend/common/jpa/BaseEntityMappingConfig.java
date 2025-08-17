package com.alligator.market.backend.common.jpa;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;


@MapperConfig(mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG)
public interface BaseEntityMappingConfig {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdVia", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedVia", ignore = true)
    void ignoreBaseFields(BaseEntity src, @MappingTarget BaseEntity dst);
}
