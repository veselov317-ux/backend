package com.backend.sys.repository;

import com.backend.sys.entity.Ticket;
import com.backend.sys.entity.TicketStatus;
import com.backend.sys.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    List<Ticket> findByRequesterOrderByUpdatedAtDesc(User requester);

    List<Ticket> findAllByOrderByUpdatedAtDesc();

    long countByStatus(TicketStatus status);
    long countByStatusAndRequester(TicketStatus status, User requester);

    long countByRequester(User requester);
}
