package com.backend.sys.repository;

import com.backend.sys.entity.Comment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndTicketId(Long id, Long ticketId);
}
