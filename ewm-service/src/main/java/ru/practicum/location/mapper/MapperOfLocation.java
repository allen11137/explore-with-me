package ru.practicum.location.mapper;

import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;

public class MapperOfLocation {

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto()
                .setLat(location.getLat())
                .setLon(location.getLon());
    }


    public static Location toLocation(LocationDto locationDto) {
        return new Location()
                .setLat(locationDto.getLat())
                .setLon(locationDto.getLon());
    }
}