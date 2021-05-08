package com.example.demo.web.project.create;

import com.example.demo.game.entity.GameType;
import com.example.demo.web.project.create.command.IProjectCreateCommandService;
import com.example.demo.web.project.create.command.model.ProjectAddRegionRequest;
import com.example.demo.web.project.create.command.model.ProjectCreateRequest;
import com.example.demo.web.project.create.command.model.ProjectCreateResponse;
import com.example.demo.web.project.create.form.ProjectCreateBillingForm;
import com.example.demo.web.project.create.form.ProjectCreateForm;
import com.example.demo.web.project.create.form.ProjectCreateRegionForm;
import com.example.demo.web.project.create.form.ProjectCreateServerForm;
import com.example.demo.web.project.create.projection.model.FetchProjectAvailableGameMapQuery;
import com.example.demo.web.project.create.projection.model.FetchProjectAvailableGameMapResponse;
import com.example.demo.web.project.create.projection.model.FetchProjectAvailableRegionsMapQuery;
import com.example.demo.web.project.create.projection.model.FetchProjectAvailableRegionsMapResponse;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/project/create")
@RequiredArgsConstructor
public class ProjectCreateController {

    private final IProjectCreateCommandService commandService;
    private final QueryGateway queryGateway;

    @GetMapping("")
    public String getCreate(Model model) throws ExecutionException, InterruptedException {

        if (!model.containsAttribute("form")) {

            ProjectCreateForm form = new ProjectCreateForm();
            form.setAvailableGames(fetchAvailableGameMap());

            model.addAttribute("form", form);

        } else {

            ProjectCreateForm form = (ProjectCreateForm) model.getAttribute("form");
            form.setAvailableGames(fetchAvailableGameMap());
        }

        return "project/create/view-create";
    }

    @PostMapping("")
    public String postCreate(Model model, RedirectAttributes redirectAttributes, @Valid @ModelAttribute("form") ProjectCreateForm form, BindingResult results) {

        if(results.hasErrors()) {

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.form", results);
            redirectAttributes.addFlashAttribute("form", form);

            return "redirect:/project/create";
        }

        ProjectCreateRequest request = ProjectCreateRequest.builder()
                .projectName(form.getProjectName())
                .gameId(form.getSelectedGameId())
                .build();

        ProjectCreateResponse response = commandService.handleCreate(request);

        return String.format("redirect:/project/create/%s/region", response.getId());
    }

    @GetMapping("/{id}/region")
    public String getCreateRegion(Model model, @PathVariable("id") UUID id) throws ExecutionException, InterruptedException {

        if (!model.containsAttribute("form")) {

            ProjectCreateRegionForm form = new ProjectCreateRegionForm();
            form.setAvailableRegions(fetchAvailableRegionsMap(id));

            model.addAttribute("form", form);

        } else {

            ProjectCreateRegionForm form = (ProjectCreateRegionForm) model.getAttribute("form");
            form.setAvailableRegions(fetchAvailableRegionsMap(id));
        }

        return "project/create/view-region";
    }

    @PostMapping("/{id}/region")
    public String getPostRegion(Model model, @PathVariable("id") UUID id, RedirectAttributes redirectAttributes, @Valid @ModelAttribute("form") ProjectCreateRegionForm form, BindingResult results) {

        if(results.hasErrors()) {

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.form", results);
            redirectAttributes.addFlashAttribute("form", form);

            return String.format("redirect:/project/create/%s/region", id);
        }

        ProjectAddRegionRequest request = new ProjectAddRegionRequest(id, form.getSelectedRegionId());
        commandService.handleAddRegion(request);

        return String.format("redirect:/project/create/%s/server", id);
    }

    @GetMapping("/{id}/server")
    public String getCreateServer(Model model, @PathVariable("id") String id) {

        if (!model.containsAttribute("form")) {

            model.addAttribute("form", new ProjectCreateServerForm());
        }

        return "project/create/view-server";
    }

    @PostMapping("/{id}/server")
    public String postCreateServer(Model model, @PathVariable("id") String id, RedirectAttributes redirectAttributes, @Valid @ModelAttribute("form") ProjectCreateServerForm form, BindingResult results) {

        if(results.hasErrors()) {

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.form", results);
            redirectAttributes.addFlashAttribute("form", form);

            return String.format("redirect:/project/create/%s/server", id);
        }

        return String.format("redirect:/project/create/%s/billing", id);
    }

    @GetMapping("/{id}/billing")
    public String getCreateBilling(Model model, @PathVariable("id") String id) {

        if (!model.containsAttribute("form")) {

            model.addAttribute("form", new ProjectCreateBillingForm());
        }

        return "project/create/view-billing";
    }

    @PostMapping("/{id}/billing")
    public String postCreateBilling(Model model, @PathVariable("id") String id, RedirectAttributes redirectAttributes, @Valid @ModelAttribute("form")ProjectCreateBillingForm form, BindingResult results) {

        if(results.hasErrors()) {

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.form", results);
            redirectAttributes.addFlashAttribute("form", form);

            return String.format("redirect:/project/create/%s/billing", id);
        }

        return String.format("redirect:/project/dashboard/%s", id);
    }

    private ImmutableMap<String, GameType> fetchAvailableGameMap() throws ExecutionException, InterruptedException {

        FetchProjectAvailableGameMapQuery query = new FetchProjectAvailableGameMapQuery();
        FetchProjectAvailableGameMapResponse response = queryGateway.query(query, FetchProjectAvailableGameMapResponse.class).get();

        return response.getAvailableGames();
    }

    private ImmutableMap<String, String> fetchAvailableRegionsMap(UUID id) throws ExecutionException, InterruptedException {

        FetchProjectAvailableRegionsMapQuery query = new FetchProjectAvailableRegionsMapQuery(id);
        FetchProjectAvailableRegionsMapResponse response = queryGateway.query(query, FetchProjectAvailableRegionsMapResponse.class).get();

        return response.getAvailableRegions();
    }
}
