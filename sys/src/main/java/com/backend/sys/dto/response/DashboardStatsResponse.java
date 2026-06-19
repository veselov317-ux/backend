package com.backend.sys.dto.response;

import java.util.Map;

public record DashboardStatsResponse(
        long totalTickets,
        long myTickets,
        long users,
        long agents,
        Map<String, Long> ticketsByStatus
) {
}
