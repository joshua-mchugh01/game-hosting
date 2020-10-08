package com.example.demo.awx.project.entity.service;

import com.example.demo.awx.project.aggregate.event.AwxProjectCreatedEvent;
import com.example.demo.awx.project.entity.model.AwxProject;

public interface IAwxProjectService {

    AwxProject handleCreated(AwxProjectCreatedEvent event);
}
