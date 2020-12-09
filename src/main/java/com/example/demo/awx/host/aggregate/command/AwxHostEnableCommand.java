package com.example.demo.awx.host.aggregate.command;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@AllArgsConstructor
public class AwxHostEnableCommand {

    @NotNull
    @TargetAggregateIdentifier
    UUID id;
}
