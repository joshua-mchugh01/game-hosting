package com.example.demo.ovh.region.aggregate.event;

import com.example.demo.ovh.region.entity.RegionStatus;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(builderClassName = "Builder")
public class RegionCreatedEvent {

    UUID id;
    String name;
    String continentCode;
    String countryCodes;
    String dataCenterLocation;
    RegionStatus status;
}
