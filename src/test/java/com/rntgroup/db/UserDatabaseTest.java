package com.rntgroup.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestUtil;
import com.rntgroup.model.User;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class UserDatabaseTest {

    private final UserDatabase userDatabase = new UserDatabase();

    @SneakyThrows
    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<Long, User> testUsers = objectMapper.readValue(
                TestUtil.readResourceAsString("db/init-user-data.json"),
                new TypeReference<>() {}
        );
        userDatabase.setData(testUsers);
    }

    @ParameterizedTest(name = "[{index}] User name = {0}, users with this name = {1}")
    @MethodSource("getNames")
    @DisplayName("Должен вернуть все User из БД с конкретным именем")
    void shouldReturnUsersByName(String name, int expectedListSize) {
        List<User> actualResult = userDatabase.selectByName(name);

        assertEquals(expectedListSize, actualResult.size());
        assertTrue(actualResult.stream().allMatch(event -> event.getName().equals(name)));
    }

    private static Stream<Arguments> getNames() {
        return Stream.of(
                Arguments.of("Игорь", 2),
                Arguments.of("Виктория", 1),
                Arguments.of("Антон", 0)
        );
    }

    @ParameterizedTest(name = "[{index}] User email = {0}, user with this email = {1}")
    @MethodSource("getEmails")
    @DisplayName("Должен вернуть User из БД с конкретным email")
    void shouldReturnUserByEmail(String requestEmail, User expectedResult) {
        User actualResult = userDatabase.selectByEmail(requestEmail);

        assertEquals(expectedResult, actualResult);
    }

    private static Stream<Arguments> getEmails() {
        return Stream.of(
                Arguments.of("nusha.from.moscow@google.com", new User(2L, "Анастасия", "nusha.from.moscow@google.com")),
                Arguments.of("markdown@github.com", new User(7L, "Марк", "markdown@github.com")),
                Arguments.of("fishing.email@rambler.com", null)
        );
    }
}