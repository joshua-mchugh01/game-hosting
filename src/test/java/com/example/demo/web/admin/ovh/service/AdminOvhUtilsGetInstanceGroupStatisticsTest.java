package com.example.demo.web.admin.ovh.service;

import com.example.demo.ovh.instance.feign.IInstanceGroupFeignService;
import com.example.demo.ovh.instance.feign.model.InstanceGroupApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;

public class AdminOvhUtilsGetInstanceGroupStatisticsTest {

    @Test
    public void whenInstanceGroupsExistsAndDoNotHaveInstanceIdsThenReturnTotal() {

        IInstanceGroupFeignService instanceGroupFeignService = Mockito.mock(IInstanceGroupFeignService.class);

        InstanceGroupApi instanceGroupApi = new InstanceGroupApi();

        Mockito.when(instanceGroupFeignService.getInstanceGroups()).thenReturn(Collections.singletonList(instanceGroupApi));

        AdminOvhUtilsServiceImpl service = new AdminOvhUtilsServiceImpl(instanceGroupFeignService);

        Assertions.assertEquals(1, service.getInstanceGroupStatistics().getTotal());
    }

    @Test
    public void whenInstanceGroupsDoNotExistsThenExpectZeroTotal() {

        IInstanceGroupFeignService instanceGroupFeignService = Mockito.mock(IInstanceGroupFeignService.class);

        Mockito.when(instanceGroupFeignService.getInstanceGroups()).thenReturn(new ArrayList<>());

        AdminOvhUtilsServiceImpl service = new AdminOvhUtilsServiceImpl(instanceGroupFeignService);

        Assertions.assertEquals(0, service.getInstanceGroupStatistics().getTotal());
    }

    @Test
    public void whenInstanceGroupsExistAndDoHaveInstanceIdsThenReturnZero() {

        IInstanceGroupFeignService instanceGroupFeignService = Mockito.mock(IInstanceGroupFeignService.class);

        InstanceGroupApi instanceGroupApi = new InstanceGroupApi();
        instanceGroupApi.setInstanceIds(Collections.singletonList("id"));

        Mockito.when(instanceGroupFeignService.getInstanceGroups()).thenReturn(Collections.singletonList(instanceGroupApi));

        AdminOvhUtilsServiceImpl service = new AdminOvhUtilsServiceImpl(instanceGroupFeignService);

        Assertions.assertEquals(0, service.getInstanceGroupStatistics().getTotal());
    }
}
