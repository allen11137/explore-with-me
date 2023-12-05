package ru.practicum.compilation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.AddCompilationDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.RequestUpdateCompilation;
import ru.practicum.compilation.mapper.MapperOfCompilation;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.RepositoryOfCompilation;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.RepositoryOfEvent;
import ru.practicum.exception.DoubleNameException;
import ru.practicum.exception.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class CompilationService {
    private final RepositoryOfCompilation repositoryOfCompilation;
    private final RepositoryOfEvent eventRepository;


    @Transactional
    public CompilationDto addAdminCompilation(AddCompilationDto addCompilationDto) {
        Set<Event> setEvent = new HashSet<>();
        if (addCompilationDto.getEvents() != null && !addCompilationDto.getEvents().isEmpty()) {
            setEvent = eventRepository.getEventsByIdIn(addCompilationDto.getEvents());
        }
        Compilation compilation = MapperOfCompilation.toCompilation(addCompilationDto, setEvent);
        CompilationDto compilationDto;
        try {
            compilationDto = MapperOfCompilation.toCompilationDto(repositoryOfCompilation.save(compilation));
        } catch (DataIntegrityViolationException e) {
            throw new DoubleNameException("Название уже существует");
        }
        return compilationDto;
    }


    @Transactional
    public CompilationDto updateAdminCompilationById(Long compId, RequestUpdateCompilation requestUpdateCompilation) {
        Compilation oldCompilation = getCompilation(compId);
        Set<Event> listEvent = new HashSet<>();
        if (requestUpdateCompilation.getEvents() != null) {
            listEvent = eventRepository.getEventsByIdIn(requestUpdateCompilation.getEvents());
        }
        Compilation compilation = MapperOfCompilation.toCompilation(requestUpdateCompilation, listEvent);
        compilation.setTitle(requestUpdateCompilation.getTitle() == null ? oldCompilation.getTitle() : requestUpdateCompilation.getTitle());
        compilation.setId(compId);
        return MapperOfCompilation.toCompilationDto(repositoryOfCompilation.save(compilation));
    }

    @Transactional
    public void deleteAdminCompilationById(Long compId) {
        getCompilation(compId);
        repositoryOfCompilation.removeCompilationById(compId);
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> getPublicCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned != null) {
            return repositoryOfCompilation.findCompilationByPinnedIs(pinned, pageable).stream()
                    .map(MapperOfCompilation::toCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return repositoryOfCompilation.findAll(pageable).stream()
                    .map(MapperOfCompilation::toCompilationDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public CompilationDto getPublicCompilationById(Long compId) {
        return MapperOfCompilation.toCompilationDto(getCompilation(compId));
    }


    private Compilation getCompilation(Long compId) {
        Compilation compilation = repositoryOfCompilation.findCompilationById(compId);
        if (compilation == null) {
            throw new NotFoundException("Подборка событий не найдена.");
        }
        return compilation;
    }
}