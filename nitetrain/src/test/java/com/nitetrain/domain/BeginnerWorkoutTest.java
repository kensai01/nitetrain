package com.nitetrain.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.nitetrain.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BeginnerWorkoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BeginnerWorkout.class);
        BeginnerWorkout beginnerWorkout1 = new BeginnerWorkout();
        beginnerWorkout1.setId(1L);
        BeginnerWorkout beginnerWorkout2 = new BeginnerWorkout();
        beginnerWorkout2.setId(beginnerWorkout1.getId());
        assertThat(beginnerWorkout1).isEqualTo(beginnerWorkout2);
        beginnerWorkout2.setId(2L);
        assertThat(beginnerWorkout1).isNotEqualTo(beginnerWorkout2);
        beginnerWorkout1.setId(null);
        assertThat(beginnerWorkout1).isNotEqualTo(beginnerWorkout2);
    }
}
