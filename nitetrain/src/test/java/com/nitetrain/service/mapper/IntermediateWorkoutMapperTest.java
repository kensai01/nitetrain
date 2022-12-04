package com.nitetrain.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntermediateWorkoutMapperTest {

    private IntermediateWorkoutMapper intermediateWorkoutMapper;

    @BeforeEach
    public void setUp() {
        intermediateWorkoutMapper = new IntermediateWorkoutMapperImpl();
    }
}
