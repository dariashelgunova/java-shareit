package ru.practicum.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.baseclient.BaseClient;
import ru.practicum.item.comment.dto.CommentRequestDto;
import ru.practicum.item.dto.ItemRequestDto;

import java.util.Map;


@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(ItemRequestDto item, long userId) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> findById(Long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> update(Long itemId, ItemRequestDto item, Long userId) {
        return patch("/" + itemId, userId, item);
    }

    public void deleteById(Long itemId) {
        delete("/" + itemId);
    }

    public void deleteAll() {
        delete("");
    }

    public ResponseEntity<Object> findItemsByOwner(Integer from, Integer size, Long userId) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> findItemsBySearch(Integer from, Integer size, String text, Long userId) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> createComment(Long itemId, Long userId, CommentRequestDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
