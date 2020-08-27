package com.example.demo.awx.feign.host.model;

import com.example.demo.awx.feign.common.AbstractBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HostApi extends AbstractBase {

    String name;
    String description;
    Boolean enabled;

    @JsonProperty("instance_id")
    String instanceId;

    @JsonProperty("inventory")
    Long inventoryId;

    @JsonProperty("last_job")
    Long lastJob;
}
