package com.nitetrain.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BeginnerWorkoutMapperTest {

    private BeginnerWorkoutMapper beginnerWorkoutMapper;

    @BeforeEach
    public void setUp() {
        beginnerWorkoutMapper = new BeginnerWorkoutMapperImpl();
    }
}
