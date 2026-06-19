package com.backend.sys.service;

import com.backend.sys.dto.response.DashboardStatsResponse;
import com.backend.sys.entity.Role;
import com.backend.sys.entity.TicketStatus;
import com.backend.sys.entity.User;
import com.backend.sys.exception.ResourceNotFoundException;
import com.backend.sys.repository.TicketRepository;
import com.backend.sys.repository.UserRepository;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public DashboardService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "dashboardStats", key = "#currentEmail")
    public DashboardStatsResponse getStats(String currentEmail) {
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Map<String, Long> byStatus = Arrays.stream(TicketStatus.values())
                .collect(Collectors.toMap(Enum::name, ticketRepository::countByStatus));

        return new DashboardStatsResponse(
                ticketRepository.count(),
                ticketRepository.countByRequester(currentUser),
                userRepository.count(),
                userRepository.findByRoleIn(java.util.List.of(Role.AGENT, Role.ADMIN)).size(),
                byStatus
        );
    }
}
