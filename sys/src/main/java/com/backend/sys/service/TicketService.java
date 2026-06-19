package com.backend.sys.service;

import com.backend.sys.dto.request.CommentCreateRequest;
import com.backend.sys.dto.request.TicketCreateRequest;
import com.backend.sys.dto.request.TicketUpdateRequest;
import com.backend.sys.dto.response.CommentResponse;
import com.backend.sys.dto.response.TicketResponse;
import com.backend.sys.entity.Category;
import com.backend.sys.entity.Comment;
import com.backend.sys.entity.Role;
import com.backend.sys.entity.Ticket;
import com.backend.sys.entity.TicketStatus;
import com.backend.sys.entity.User;
import com.backend.sys.exception.ResourceNotFoundException;
import com.backend.sys.repository.CategoryRepository;
import com.backend.sys.repository.CommentRepository;
import com.backend.sys.repository.TicketRepository;
import com.backend.sys.repository.UserRepository;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ResponseMapper mapper;

    public TicketService(
            TicketRepository ticketRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            CommentRepository commentRepository,
            ResponseMapper mapper
    ) {
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getTickets(String currentEmail, TicketStatus status, Long categoryId, String search) {
        User currentUser = getUser(currentEmail);
        List<Ticket> tickets = canSeeAllTickets(currentUser)
                ? ticketRepository.findAllByOrderByUpdatedAtDesc()
                : ticketRepository.findByRequesterOrderByUpdatedAtDesc(currentUser);

        return tickets.stream()
                .filter(ticket -> status == null || ticket.getStatus() == status)
                .filter(ticket -> categoryId == null || ticket.getCategory().getId().equals(categoryId))
                .filter(ticket -> matchesSearch(ticket, search))
                .map(mapper::toTicket)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicket(Long id, String currentEmail) {
        Ticket ticket = findTicket(id);
        assertCanAccess(ticket, getUser(currentEmail));
        return mapper.toTicket(ticket);
    }

    @Transactional
    @CacheEvict(value = "dashboardStats", allEntries = true)
    public TicketResponse createTicket(TicketCreateRequest request, String currentEmail) {
        User requester = getUser(currentEmail);
        Category category = categoryRepository.findById(request.categoryId())
                .filter(Category::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Active category not found"));

        Ticket ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setCategory(category);
        ticket.setRequester(requester);
        ticket.setStatus(TicketStatus.OPEN);
        return mapper.toTicket(ticketRepository.save(ticket));
    }

    @Transactional
    @CacheEvict(value = "dashboardStats", allEntries = true)
    public TicketResponse updateTicket(Long id, TicketUpdateRequest request, String currentEmail) {
        User currentUser = getUser(currentEmail);
        if (!canSeeAllTickets(currentUser)) {
            throw new AccessDeniedException("Only support agents and admins can update ticket workflow");
        }

        Ticket ticket = findTicket(id);
        if (request.status() != null) {
            ticket.setStatus(request.status());
        }
        if (request.assignedAgentId() != null) {
            User agent = userRepository.findById(request.assignedAgentId())
                    .filter(user -> user.getRole() == Role.AGENT || user.getRole() == Role.ADMIN)
                    .orElseThrow(() -> new ResourceNotFoundException("Agent not found"));
            ticket.setAssignedAgent(agent);
            if (ticket.getStatus() == TicketStatus.OPEN) {
                ticket.setStatus(TicketStatus.IN_PROGRESS);
            }
        }
        return mapper.toTicket(ticket);
    }

    @Transactional
    public CommentResponse addComment(Long ticketId, CommentCreateRequest request, String currentEmail) {
        User currentUser = getUser(currentEmail);
        Ticket ticket = findTicket(ticketId);
        assertCanAccess(ticket, currentUser);

        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setAuthor(currentUser);
        comment.setMessage(request.message());
        ticket.getComments().add(comment);
        return mapper.toComment(commentRepository.save(comment));
    }

    private Ticket findTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void assertCanAccess(Ticket ticket, User user) {
        if (canSeeAllTickets(user) || ticket.getRequester().getId().equals(user.getId())) {
            return;
        }
        throw new AccessDeniedException("You do not have access to this ticket");
    }

    private boolean canSeeAllTickets(User user) {
        return user.getRole() == Role.AGENT || user.getRole() == Role.ADMIN;
    }

    private boolean matchesSearch(Ticket ticket, String search) {
        if (!StringUtils.hasText(search)) {
            return true;
        }
        String normalized = search.toLowerCase();
        return ticket.getTitle().toLowerCase().contains(normalized)
                || ticket.getDescription().toLowerCase().contains(normalized);
    }
}
