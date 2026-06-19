package com.backend.sys.controller;

import com.backend.sys.dto.response.DashboardStatsResponse;
import com.backend.sys.service.DashboardService;
import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public DashboardStatsResponse getStats(Principal principal) {
        return dashboardService.getStats(principal.getName());
    }
}
