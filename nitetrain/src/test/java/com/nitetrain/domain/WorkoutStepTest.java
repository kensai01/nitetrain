package com.nitetrain.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitetrain.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkoutStepTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkoutStep.class);
        WorkoutStep workoutStep1 = new WorkoutStep();
        workoutStep1.setId(1L);
        WorkoutStep workoutStep2 = new WorkoutStep();
        workoutStep2.setId(workoutStep1.getId());
        assertThat(workoutStep1).isEqualTo(workoutStep2);
        workoutStep2.setId(2L);
        assertThat(workoutStep1).isNotEqualTo(workoutStep2);
        workoutStep1.setId(null);
        assertThat(workoutStep1).isNotEqualTo(workoutStep2);
    }
}
