package com.example.tickets.web;

import com.example.tickets.model.*;
import com.example.tickets.repo.TicketRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class TicketController {

    record CreateRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 2000) String description,
        @NotNull Priority priority) {}

    record UpdateRequest(@Size(max = 4000) String solution, @NotNull Status status) {}

    private final TicketRepository repo;

    public TicketController(TicketRepository repo) { this.repo = repo; }

    // ---- Public: users ----
    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket create(@Valid @RequestBody CreateRequest r) {
        Ticket t = new Ticket();
        t.setRequesterName(r.name().trim());
        t.setRequesterEmail(r.email().trim().toLowerCase());
        t.setTitle(r.title().trim());
        t.setDescription(r.description().trim());
        t.setPriority(r.priority());
        return repo.save(t);
    }

    // Tracking needs ticket id + the email used to raise it
    @GetMapping("/tickets/{id}")
    public Ticket track(@PathVariable Long id, @RequestParam String email) {
        return repo.findById(id)
            .filter(t -> t.getRequesterEmail().equalsIgnoreCase(email.trim()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    // ---- Admin (HTTP Basic) ----
    @GetMapping("/admin/tickets")
    public List<Ticket> list(@RequestParam(required = false) Status status) {
        return status == null ? repo.findAllByOrderByCreatedAtDesc()
                              : repo.findByStatusOrderByCreatedAtDesc(status);
    }

    @PutMapping("/admin/tickets/{id}")
    public Ticket update(@PathVariable Long id, @Valid @RequestBody UpdateRequest r) {
        Ticket t = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
        t.setSolution(r.solution());
        t.setStatus(r.status());
        return repo.save(t);
    }
}
