package ru.practicum.request.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.baseclient.BaseClient;
import ru.practicum.request.dto.ItemRequestRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> findAll(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> create(ItemRequestRequestDto requestDto, long userId) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> findById(Long requestId, long userId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> findRequestsCreatedByUsers(Integer from, Integer size, Long userId) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }
}
