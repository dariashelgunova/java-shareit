package ru.practicum.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.baseclient.BaseClient;
import ru.practicum.user.dto.UserRequestDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    public ResponseEntity<Object> create(UserRequestDto requestDto) {
        return post("", requestDto);
    }

    public ResponseEntity<Object> findById(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> update(Long userId, UserRequestDto user) {
        return patch("/" + userId, user);
    }

    public void deleteById(Long userId) {
        delete("/" + userId);
    }

    public void deleteAll() {
        delete("");
    }
}
