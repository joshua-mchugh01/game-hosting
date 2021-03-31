package com.example.demo.framework.seed.awx.project;

import com.example.demo.awx.feign.ListResponse;
import com.example.demo.awx.notification.feign.INotificationFeignService;
import com.example.demo.awx.notification.feign.model.NotificationApi;
import com.example.demo.awx.notification.feign.model.NotificationConfiguration;
import com.example.demo.awx.project.feign.IProjectFeignService;
import com.example.demo.awx.project.feign.model.ProjectApi;
import com.example.demo.sample.SampleBuilder;
import com.example.demo.sample.SampleData;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class AwxProjectSeedServiceTest {

    @Autowired
    private AwxProjectSeedService awxProjectSeedService;

    @Autowired
    private SampleBuilder sampleBuilder;

    @MockBean
    private IProjectFeignService projectFeignService;

    @MockBean
    private INotificationFeignService notificationFeignService;

    @Test
    public void whenAwxProjectExistsThenDataDoesNotExistsReturnFalse() throws ExecutionException, InterruptedException {

        sampleBuilder.builder()
                .awxOrganization()
                .awxCredential()
                .awxProject()
                .build();

        Assertions.assertFalse(awxProjectSeedService.dataNotExists());
    }

    @Test
    public void whenAwxProjectDoesNotExistsThenDataDoesNotExistsReturnTrue() throws ExecutionException, InterruptedException {

        Assertions.assertTrue(awxProjectSeedService.dataNotExists());
    }

    @Test
    public void whenAwxProjectClientReturnsMatchingNameThenReturnList() throws ExecutionException, InterruptedException {

        SampleData sampleData = sampleBuilder.builder()
                .awxOrganization()
                .awxGitlabCredential()
                .build();

        ProjectApi projectApi = new ProjectApi();
        projectApi.setId(1L);
        projectApi.setName("Game Hosting Project");
        projectApi.setOrganizationId(sampleData.getAwxOrganization().getAwxId());
        projectApi.setCredentialId(sampleData.getAwxCredential().getAwxId());
        projectApi.setScmType("git");
        projectApi.setScmBranch("master");
        projectApi.setScmUrl("url");

        ListResponse<ProjectApi> clientResponse = new ListResponse<>();
        clientResponse.setResults(Collections.singletonList(projectApi));

        Mockito.when(projectFeignService.getProjects()).thenReturn(clientResponse);

        ImmutableList<Object> awxProjects = awxProjectSeedService.initializeData();

        Assertions.assertEquals(1, awxProjects.size());
    }

    @Test
    public void whenAwxProjectClientDoesNotReturnMatchingNameThenReturnList() throws ExecutionException, InterruptedException {

        SampleData sampleData = sampleBuilder.builder()
                .awxOrganization()
                .awxGitlabCredential()
                .build();

        ProjectApi wrongProjectApi = new ProjectApi();
        wrongProjectApi.setId(2L);
        wrongProjectApi.setName("Wrong Project");
        wrongProjectApi.setOrganizationId(sampleData.getAwxOrganization().getAwxId());
        wrongProjectApi.setCredentialId(sampleData.getAwxCredential().getAwxId());
        wrongProjectApi.setScmType("git");
        wrongProjectApi.setScmBranch("master");
        wrongProjectApi.setScmUrl("url");

        ListResponse<ProjectApi> clientResponse = new ListResponse<>();
        clientResponse.setResults(Collections.singletonList(wrongProjectApi));

        Mockito.when(projectFeignService.getProjects()).thenReturn(clientResponse);

        ProjectApi projectApi = new ProjectApi();
        projectApi.setId(2L);
        projectApi.setName("Game Hosting Project");
        projectApi.setOrganizationId(sampleData.getAwxOrganization().getAwxId());
        projectApi.setCredentialId(sampleData.getAwxCredential().getAwxId());
        projectApi.setScmType("git");
        projectApi.setScmBranch("master");
        projectApi.setScmUrl("url");

        Mockito.when(projectFeignService.createProject(Mockito.any())).thenReturn(projectApi);

        NotificationConfiguration configuration = NotificationConfiguration.builder()
                .url("url")
                .build();

        NotificationApi notificationApi = new NotificationApi();
        notificationApi.setId(1L);
        notificationApi.setName("Notification Name");
        notificationApi.setDescription("Notification Description");
        notificationApi.setOrganizationId(sampleData.getAwxOrganization().getAwxId());
        notificationApi.setType("webhook");
        notificationApi.setNotificationConfiguration(configuration);

        Mockito.when(notificationFeignService.createSuccessNotificationForProject(Mockito.anyLong(), Mockito.any())).thenReturn(notificationApi);

        ImmutableList<Object> awxProjects = awxProjectSeedService.initializeData();

        Assertions.assertEquals(1, awxProjects.size());
    }

    @Test
    public void whenTypeHasValueThenReturnValue() {

        Assertions.assertEquals("Awx Project", awxProjectSeedService.type());
    }

    @Test
    public void typeShouldNotBeNull() {

        Assertions.assertNotNull(awxProjectSeedService.type());
    }

    @Test
    public void whenOrderHasValueReturnValue() {

        Assertions.assertEquals(9, awxProjectSeedService.order());
    }

    @Test
    public void orderShouldNotBeNull() {

        Assertions.assertNotNull(awxProjectSeedService.order());
    }
}
