package com.example.demo.web.admin.game.command;

import com.example.demo.web.admin.game.command.service.IAdminGameServerCommandService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AdminGameServerCommandControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAdminGameServerCommandService service;

    @Test
    public void whenUserIsUnauthorizedThenExpectLoginScreen() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/admin/game-servers/create")
                .with(SecurityMockMvcRequestPostProcessors.csrf());

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    public void whenUserIsRegularUserThenExpectForbidden() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/admin/game-servers/create")
                .with(SecurityMockMvcRequestPostProcessors.user("regular").roles("REGULAR"))
                .with(SecurityMockMvcRequestPostProcessors.csrf());

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void whenUserIsAdminThenReturnOk() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/admin/game-servers/create")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf());

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenRequestHasInvalidFormThenExpectErrors() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/admin/game-servers/create")
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf());

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("form", "name", "selectedStatus", "gameId", "regionId", "flavorId", "imageId"));
    }

    @Test
    public void whenRequestIsValidAndNameAlreadyExistsThenExpectErrors() throws Exception {

        Mockito.when(service.existsByName(Mockito.anyString())).thenReturn(true);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/admin/game-servers/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(validForm())
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf());

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("form", "name"));
    }

    @Test
    public void whenRequestIsValidThenExpectNoErrors() throws Exception {

        Mockito.when(service.existsByName(Mockito.anyString())).thenReturn(false);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/admin/game-servers/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(validForm())
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf());

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors());
    }

    @Test
    public void whenRequestIsValidThenExpectSuccessModelTemplate() throws Exception {

        Mockito.when(service.existsByName(Mockito.anyString())).thenReturn(false);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/admin/game-servers/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(validForm())
                .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf());

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.view().name("component/modal-response"));
    }

    private MultiValueMap<String, String> validForm() {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "name");
        map.add("selectedStatus", "ACTIVE");
        map.add("gameId", UUID.randomUUID().toString());
        map.add("regionId", UUID.randomUUID().toString());
        map.add("flavorId", UUID.randomUUID().toString());
        map.add("imageId", UUID.randomUUID().toString());

        return map;
    }
}
