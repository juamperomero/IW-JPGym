package com.example.application.user.unit;

import com.example.application.data.User;
import com.example.application.user.ObjectMother;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void shouldProvideUsername() {

        // Given
        // a certain user (not stored on the database)
        User testUser = ObjectMother.createTestUser("john");

        // When
        // I invoke getUsername method
        String username = testUser.getUsername();

        // Then the result is equals to the provided username
        assertThat(username.equals("john")).isTrue();

    }

}
