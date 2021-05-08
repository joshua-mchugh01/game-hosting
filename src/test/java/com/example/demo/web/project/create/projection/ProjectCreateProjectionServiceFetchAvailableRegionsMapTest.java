package com.example.demo.web.project.create.projection;

import com.example.demo.project.entity.model.Project;
import com.example.demo.sample.SampleBuilder;
import com.example.demo.sample.SampleData;
import com.example.demo.web.project.create.projection.model.FetchProjectAvailableRegionsMapQuery;
import com.example.demo.web.project.create.projection.model.FetchProjectAvailableRegionsMapResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.lang.reflect.UndeclaredThrowableException;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class ProjectCreateProjectionServiceFetchAvailableRegionsMapTest {

    @Autowired
    private IProjectCreateProjectionService service;

    @Autowired
    private SampleBuilder sampleBuilder;

    @Test
    public void whenParamIsNullExpectException() {

        Assertions.assertThrows(UndeclaredThrowableException.class, () -> service.fetchAvailableRegionsMap(null));
    }

    @Test
    public void whenParamHasNullIdThenExpectException() {

        FetchProjectAvailableRegionsMapQuery query = new FetchProjectAvailableRegionsMapQuery(null);

        Assertions.assertThrows(UndeclaredThrowableException.class, () -> service.fetchAvailableRegionsMap(query));
    }

    @Test
    public void whenRegionIsAvailableThenExpectResults() {

        SampleData sampleData = sampleBuilder.builder()
                .region()
                .flavor()
                .image()
                .game()
                .gameServer()
                .user()
                .project()
                .build();

        FetchProjectAvailableRegionsMapQuery query = new FetchProjectAvailableRegionsMapQuery(sampleData.getProject().getId());
        FetchProjectAvailableRegionsMapResponse response = service.fetchAvailableRegionsMap(query);

        Assertions.assertEquals(1, response.getAvailableRegions().size());
    }

    @Test
    public void whenRegionIsNotAvailableThenExpectEmptyResults() {

        Project project = sampleBuilder.builder()
                .user()
                .project()
                .build()
                .getProject();

        FetchProjectAvailableRegionsMapQuery query = new FetchProjectAvailableRegionsMapQuery(project.getId());
        FetchProjectAvailableRegionsMapResponse response = service.fetchAvailableRegionsMap(query);

        Assertions.assertEquals(0, response.getAvailableRegions().size());
    }
}
