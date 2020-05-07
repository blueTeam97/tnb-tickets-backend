package com.blue.tnb.repository;

import com.blue.tnb.constants.Status;
import com.blue.tnb.exception.TicketWithoutUserException;
import com.blue.tnb.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findAll();

    List<Ticket> findAllByUserId(Long userId);

    Ticket getOne(Long id);
}
