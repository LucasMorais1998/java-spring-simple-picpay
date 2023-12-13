package com.youtube.javaspringsimplepicpay.repositories;

import com.youtube.javaspringsimplepicpay.domain.user.User;
import com.youtube.javaspringsimplepicpay.domain.user.UserType;
import com.youtube.javaspringsimplepicpay.dtos.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get User successfully from DB")
    void findUserByDocumentSuccess() {
        String document = "99999999901";

        UserDTO data = new UserDTO("John",
                "Doe",
                document,
                new BigDecimal(10),
                "john_doe@email.com",
                "password",
                UserType.COMMON);

        this.createUser(data);

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    private User createUser(UserDTO data) {
        User newUser = new User(data);
        this.entityManager.persist(newUser);

        return newUser;
    }
}