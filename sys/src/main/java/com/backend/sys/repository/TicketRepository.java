package com.backend.sys.repository;

import com.backend.sys.entity.Ticket;
import com.backend.sys.entity.TicketStatus;
import com.backend.sys.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByRequesterOrderByUpdatedAtDesc(User requester);

    List<Ticket> findAllByOrderByUpdatedAtDesc();

    long countByStatus(TicketStatus status);

    long countByRequester(User requester);
}
