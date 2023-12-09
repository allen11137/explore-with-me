package ru.practicum.location.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;

@UtilityClass
public class MapperOfLocation {
    public static LocationDto toLocationDto(Location location) {
        return new LocationDto()
                .setLat(location.getLat())
                .setLon(location.getLon());
    }
}