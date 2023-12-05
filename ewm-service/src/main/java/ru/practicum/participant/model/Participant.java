package ru.practicum.participant.model;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.event.model.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "participant")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime created;

    @Column
    private Long event;

    @Column
    private Long requester;

    @Enumerated(EnumType.STRING)
    private Status status;
}