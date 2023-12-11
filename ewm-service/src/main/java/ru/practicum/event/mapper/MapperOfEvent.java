package ru.practicum.event.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.category.mapper.MapperOfCategory;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.location.mapper.MapperOfLocation;
import ru.practicum.user.mapper.MapperOfUser;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.event.Constant.DATA_TIME_PATTERN;

@UtilityClass
public class MapperOfEvent {
    public EventCompleteDto toEventFullDto(Event event) {
        return new EventCompleteDto()
                .setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(MapperOfCategory.toCategoryDto(event.getCategory()))
                .setConfirmedRequests(event.getConfirmedRequests())
                .setCreatedOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .setDescription(event.getDescription())
                .setEventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .setInitiator(MapperOfUser.toUserBriefDto(event.getInitiator()))
                .setLocation(MapperOfLocation.toLocationDto(event.getLocation()))
                .setPaid(event.getPaid())
                .setParticipantLimit(event.getParticipantLimit())
                .setPublishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn().format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .setRequestModeration(event.getRequestModeration())
                .setState(event.getState() == null ? null : event.getState().toString())
                .setTitle(event.getTitle())
                .setViews(event.getViews());
    }

    public EventBriefDto toEventShortDto(Event event) {
        return new EventBriefDto()
                .setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(MapperOfCategory.toCategoryDto(event.getCategory()))
                .setConfirmedRequests(event.getConfirmedRequests())
                .setEventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .setInitiator(MapperOfUser.toUserBriefDto(event.getInitiator()))
                .setPaid(event.getPaid())
                .setTitle(event.getTitle())
                .setViews(event.getViews());
    }

    public Event toEvent(AddEventDto newEventDto, User initiator, Category category) {
        return new Event()
                .setAnnotation(newEventDto.getAnnotation())
                .setCategory(category)
                .setCreatedOn(LocalDateTime.now())
                .setDescription(newEventDto.getDescription())
                .setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .setInitiator(initiator)
                .setLocation(newEventDto.getLocation())
                .setConfirmedRequests(0L)
                .setPaid(newEventDto.getPaid() != null && newEventDto.getPaid())
                .setParticipantLimit(newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit())
                .setRequestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration())
                .setState(State.PENDING)
                .setTitle(newEventDto.getTitle())
                .setViews(0L);
    }

    public Event toEvent(AdminUpdateEventRequest updateEventAdminRequest, Event oldEvent, Category category) {
        return new Event()
                .setId(oldEvent.getId())
                .setAnnotation(updateEventAdminRequest.getAnnotation() == null ? oldEvent.getAnnotation() : updateEventAdminRequest.getAnnotation())
                .setCategory(updateEventAdminRequest.getCategory() == null ? oldEvent.getCategory() : category)
                .setConfirmedRequests(oldEvent.getConfirmedRequests())
                .setCreatedOn(oldEvent.getCreatedOn())
                .setDescription(updateEventAdminRequest.getDescription() == null ? oldEvent.getDescription() : updateEventAdminRequest.getDescription())
                .setEventDate(updateEventAdminRequest.getEventDate() == null ? oldEvent.getEventDate() : LocalDateTime.parse(updateEventAdminRequest.getEventDate(), DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .setInitiator(oldEvent.getInitiator())
                .setLocation(updateEventAdminRequest.getLocation() == null ? oldEvent.getLocation() : updateEventAdminRequest.getLocation())
                .setPaid(updateEventAdminRequest.getPaid() == null ? oldEvent.getPaid() : updateEventAdminRequest.getPaid())
                .setParticipantLimit(updateEventAdminRequest.getParticipantLimit() == null ? oldEvent.getParticipantLimit() : updateEventAdminRequest.getParticipantLimit())
                .setRequestModeration(updateEventAdminRequest.getRequestModeration() == null ? oldEvent.getRequestModeration() : updateEventAdminRequest.getRequestModeration())
                .setState(oldEvent.getState())
                .setTitle(updateEventAdminRequest.getTitle() == null ? oldEvent.getTitle() : updateEventAdminRequest.getTitle())
                .setViews(oldEvent.getViews());
    }

    public Event toEvent(UserUpdateEventRequest updateEventUserRequest, Event oldEvent, Category category) {
        return new Event()
                .setId(oldEvent.getId())
                .setAnnotation(updateEventUserRequest.getAnnotation() == null ? oldEvent.getAnnotation() : updateEventUserRequest.getAnnotation())
                .setCategory(updateEventUserRequest.getCategory() == null ? oldEvent.getCategory() : category)
                .setConfirmedRequests(oldEvent.getConfirmedRequests())
                .setCreatedOn(oldEvent.getCreatedOn())
                .setDescription(updateEventUserRequest.getDescription() == null ? oldEvent.getDescription() : updateEventUserRequest.getDescription())
                .setEventDate(updateEventUserRequest.getEventDate() == null ? oldEvent.getEventDate() : LocalDateTime.parse(updateEventUserRequest.getEventDate(), DateTimeFormatter.ofPattern(DATA_TIME_PATTERN)))
                .setInitiator(oldEvent.getInitiator())
                .setLocation(updateEventUserRequest.getLocation() == null ? oldEvent.getLocation() : updateEventUserRequest.getLocation())
                .setPaid(updateEventUserRequest.getPaid() == null ? oldEvent.getPaid() : updateEventUserRequest.getPaid())
                .setParticipantLimit(updateEventUserRequest.getParticipantLimit() == null ? oldEvent.getParticipantLimit() : updateEventUserRequest.getParticipantLimit())
                .setRequestModeration(updateEventUserRequest.getRequestModeration() == null ? oldEvent.getRequestModeration() : updateEventUserRequest.getRequestModeration())
                .setTitle(updateEventUserRequest.getTitle() == null ? oldEvent.getTitle() : updateEventUserRequest.getTitle())
                .setViews(oldEvent.getViews());
    }
}