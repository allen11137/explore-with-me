package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
@Validated
public class Controller {
    private final Client client;

    @GetMapping("/stats")
    public ResponseEntity<Object> getAllStats(HttpServletRequest request,
                                              @RequestParam(name = "start") String start,
                                              @RequestParam(name = "end") String end,
                                              @RequestParam(required = false, name = "uris") String[] uris,
                                              @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        String ipResource = request.getRemoteAddr();
        return client.getStats(ipResource, start, end, uris, unique);
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> addUser(HttpServletRequest request,
                                          @RequestBody EndpointHitDto endpointHitDto) {
        return client.addRequest(request.getRemoteAddr(), endpointHitDto);
    }

}
