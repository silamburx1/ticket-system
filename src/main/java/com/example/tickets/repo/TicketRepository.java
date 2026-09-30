package com.example.tickets.repo;

import com.example.tickets.model.*;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByOrderByCreatedAtDesc();
    List<Ticket> findByStatusOrderByCreatedAtDesc(Status status);
}
