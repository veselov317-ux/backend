package com.backend.sys.repository;

import com.backend.sys.entity.Ticket;
import com.backend.sys.entity.TicketStatus;
import com.backend.sys.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByRequesterOrderByUpdatedAtDesc(User requester);

    List<Ticket> findAllByOrderByUpdatedAtDesc();

    @Query("SELECT t FROM Ticket t " +
            "WHERE (:requester IS NULL OR t.requester = :requester) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:categoryId IS NULL OR t.category.id = :categoryId) " +
            "AND (:search IS NULL OR (LOWER(t.title) LIKE LOWER(concat('%',:search,'%')) OR LOWER(t.description) LIKE LOWER(concat('%',:search,'%')))) " +
            "ORDER BY t.updatedAt DESC")
    Page<Ticket> findByFilters(@Param("requester") User requester,
                               @Param("status") TicketStatus status,
                               @Param("categoryId") Long categoryId,
                               @Param("search") String search,
                               Pageable pageable);

    long countByStatus(TicketStatus status);
    long countByStatusAndRequester(TicketStatus status, User requester);

    long countByRequester(User requester);
}
