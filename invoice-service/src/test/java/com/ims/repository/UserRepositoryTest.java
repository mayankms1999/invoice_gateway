package com.ims.repository;

import com.ims.model.UserDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest  // Loads only repository layer with H2 DB
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFetchUser() {
        // Arrange
        UserDetail user = UserDetail.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        // Act
        UserDetail savedUser = userRepository.save(user);
        Optional<UserDetail> retrievedUser = userRepository.findById(savedUser.getId());

        // Assert
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getName()).isEqualTo("John Doe");
        assertThat(retrievedUser.get().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(retrievedUser.get().getPhone()).isEqualTo("1234567890");
    }

    @Test
    void shouldDeleteUser() {
        // Arrange
        UserDetail user = UserDetail.builder()
                .name("Alice")
                .email("alice@example.com")
                .phone("9876543210")
                .build();

        UserDetail savedUser = userRepository.save(user);

        // Act
        userRepository.deleteById(savedUser.getId());
        Optional<UserDetail> deletedUser = userRepository.findById(savedUser.getId());

        // Assert
        assertThat(deletedUser).isEmpty();
    }
}
