package com.backend.sys.service;

import com.backend.sys.dto.response.CategoryResponse;
import com.backend.sys.dto.response.CommentResponse;
import com.backend.sys.dto.response.TicketResponse;
import com.backend.sys.dto.response.UserResponse;
import com.backend.sys.entity.Category;
import com.backend.sys.entity.Comment;
import com.backend.sys.entity.Ticket;
import com.backend.sys.entity.User;
import java.util.Comparator;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {
    public UserResponse toUser(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }

    public CategoryResponse toCategory(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive()
        );
    }

    public CommentResponse toComment(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getMessage(),
                toUser(comment.getAuthor()),
                comment.getCreatedAt()
        );
    }

    public TicketResponse toTicket(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                toCategory(ticket.getCategory()),
                toUser(ticket.getRequester()),
                toUser(ticket.getAssignedAgent()),
                ticket.getComments().stream()
                        .sorted(Comparator.comparing(Comment::getCreatedAt))
                        .map(this::toComment)
                        .toList(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}
