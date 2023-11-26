package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;



@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class EndpointHitDto {
    public static final String DATA_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    @NotNull
    private String app;
    @NotBlank
    private String uri;
    @NotNull
    private String ip;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATA_TIME_PATTERN)
    private LocalDateTime timestamp;
}