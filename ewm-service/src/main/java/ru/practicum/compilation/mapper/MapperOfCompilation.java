package ru.practicum.compilation.mapper;


import ru.practicum.compilation.dto.AddCompilationDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.RequestUpdateCompilation;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.MapperOfEvent;
import ru.practicum.event.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

public class MapperOfCompilation {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto()
                .setId(compilation.getId())
                .setEvents(compilation.getEvents().stream().map(MapperOfEvent::toEventShortDto).collect(Collectors.toList()))
                .setPinned(compilation.isPinned())
                .setTitle(compilation.getTitle());
    }

    public static Compilation toCompilation(AddCompilationDto addCompilationDto, Set<Event> events) {
        return new Compilation()
                .setEvents(events)
                .setPinned(addCompilationDto.isPinned())
                .setTitle(addCompilationDto.getTitle());
    }

    public static Compilation toCompilation(RequestUpdateCompilation requestUpdateCompilation, Set<Event> events) {
        return new Compilation()
                .setEvents(events)
                .setPinned(requestUpdateCompilation.isPinned());
    }
}
