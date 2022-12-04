package com.nitetrain.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitetrain.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntermediateWorkoutDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntermediateWorkoutDTO.class);
        IntermediateWorkoutDTO intermediateWorkoutDTO1 = new IntermediateWorkoutDTO();
        intermediateWorkoutDTO1.setId(1L);
        IntermediateWorkoutDTO intermediateWorkoutDTO2 = new IntermediateWorkoutDTO();
        assertThat(intermediateWorkoutDTO1).isNotEqualTo(intermediateWorkoutDTO2);
        intermediateWorkoutDTO2.setId(intermediateWorkoutDTO1.getId());
        assertThat(intermediateWorkoutDTO1).isEqualTo(intermediateWorkoutDTO2);
        intermediateWorkoutDTO2.setId(2L);
        assertThat(intermediateWorkoutDTO1).isNotEqualTo(intermediateWorkoutDTO2);
        intermediateWorkoutDTO1.setId(null);
        assertThat(intermediateWorkoutDTO1).isNotEqualTo(intermediateWorkoutDTO2);
    }
}
