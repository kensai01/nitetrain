package com.nitetrain.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitetrain.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntermediateWorkoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntermediateWorkout.class);
        IntermediateWorkout intermediateWorkout1 = new IntermediateWorkout();
        intermediateWorkout1.setId(1L);
        IntermediateWorkout intermediateWorkout2 = new IntermediateWorkout();
        intermediateWorkout2.setId(intermediateWorkout1.getId());
        assertThat(intermediateWorkout1).isEqualTo(intermediateWorkout2);
        intermediateWorkout2.setId(2L);
        assertThat(intermediateWorkout1).isNotEqualTo(intermediateWorkout2);
        intermediateWorkout1.setId(null);
        assertThat(intermediateWorkout1).isNotEqualTo(intermediateWorkout2);
    }
}
