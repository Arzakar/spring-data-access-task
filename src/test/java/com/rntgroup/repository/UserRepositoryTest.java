package com.rntgroup.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.TestUtil;
import com.rntgroup.db.UserDatabase;
import com.rntgroup.model.User;
import com.rntgroup.repository.util.Page;
import com.rntgroup.repository.util.SearchResult;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserDatabase userDatabase;

    @InjectMocks
    private UserRepository userRepository;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @ParameterizedTest(name = "[{index}] Page = {1}, expected search result = {2}")
    @MethodSource("getUsersByName")
    @DisplayName("Должен вернуть определённую страницу с результатом поиска по имени")
    void shouldReturnSearchResultByName(List<User> responseUsersByName, Page page, SearchResult<User> expectedResult) {
        when(userDatabase.selectByName(any(String.class))).thenReturn(responseUsersByName);

        SearchResult<User> actualResult = userRepository.findByName("Андрей", page);

        assertEquals(expectedResult, actualResult);
    }

    @SneakyThrows
    private static Stream<Arguments> getUsersByName() {
        List<User> responseEventsByTitle = OBJECT_MAPPER.readValue(
                TestUtil.readResourceAsString("repository/response/users-by-name.json"),
                new TypeReference<>() {
                }
        );

        return Stream.of(
                Arguments.of(responseEventsByTitle, Page.of(3, 1), SearchResult.pack(responseEventsByTitle, Page.of(3, 1))),
                Arguments.of(responseEventsByTitle, Page.of(2, 3), SearchResult.pack(responseEventsByTitle, Page.of(2, 3))),
                Arguments.of(responseEventsByTitle, Page.of(4, 2), SearchResult.pack(responseEventsByTitle, Page.of(4, 2)))
        );
    }

    @ParameterizedTest(name = "[{index}] User email = {0}, response by DB = {1}, expected result = {2}")
    @MethodSource("getUsersByEmail")
    @DisplayName("Должен вернуть Optional с найденным по email User или Optional.empty")
    void shouldReturnUserByEmail(String email, User responseUser, Optional<User> expectedResult) {
        when(userDatabase.selectByEmail(email)).thenReturn(responseUser);

        assertEquals(expectedResult, userRepository.findByEmail(email));
        verify(userDatabase).selectByEmail(email);
    }

    private static Stream<Arguments> getUsersByEmail() {
        return Stream.of(
                Arguments.of("test@mail.ru", new User(0L, "TestUser", "test@mail.ru"), Optional.of(new User(0L, "TestUser", "test@mail.ru"))),
                Arguments.of("incorrect.email", null, Optional.empty())
        );
    }

}