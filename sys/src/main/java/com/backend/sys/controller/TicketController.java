package com.backend.sys.controller;

import com.backend.sys.dto.request.CommentCreateRequest;
import com.backend.sys.dto.request.TicketCreateRequest;
import com.backend.sys.dto.request.TicketUpdateRequest;
import com.backend.sys.dto.response.CommentResponse;
import com.backend.sys.dto.response.TicketResponse;
import com.backend.sys.entity.TicketStatus;
import com.backend.sys.service.TicketService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<TicketResponse> getTickets(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            Principal principal
    ) {
        return ticketService.getTickets(principal.getName(), status, categoryId, search);
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable Long id, Principal principal) {
        return ticketService.getTicket(id, principal.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse createTicket(@Valid @RequestBody TicketCreateRequest request, Principal principal) {
        return ticketService.createTicket(request, principal.getName());
    }

    @PatchMapping("/{id}")
    public TicketResponse updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketUpdateRequest request,
            Principal principal
    ) {
        return ticketService.updateTicket(id, request, principal.getName());
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentCreateRequest request,
            Principal principal
    ) {
        return ticketService.addComment(id, request, principal.getName());
    }
}
