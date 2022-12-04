package com.nitetrain.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitetrain.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BeginnerWorkoutDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BeginnerWorkoutDTO.class);
        BeginnerWorkoutDTO beginnerWorkoutDTO1 = new BeginnerWorkoutDTO();
        beginnerWorkoutDTO1.setId(1L);
        BeginnerWorkoutDTO beginnerWorkoutDTO2 = new BeginnerWorkoutDTO();
        assertThat(beginnerWorkoutDTO1).isNotEqualTo(beginnerWorkoutDTO2);
        beginnerWorkoutDTO2.setId(beginnerWorkoutDTO1.getId());
        assertThat(beginnerWorkoutDTO1).isEqualTo(beginnerWorkoutDTO2);
        beginnerWorkoutDTO2.setId(2L);
        assertThat(beginnerWorkoutDTO1).isNotEqualTo(beginnerWorkoutDTO2);
        beginnerWorkoutDTO1.setId(null);
        assertThat(beginnerWorkoutDTO1).isNotEqualTo(beginnerWorkoutDTO2);
    }
}
