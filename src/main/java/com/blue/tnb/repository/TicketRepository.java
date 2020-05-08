package com.blue.tnb.repository;

import com.blue.tnb.constants.Status;
import com.blue.tnb.exception.TicketWithoutUserException;
import com.blue.tnb.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findAll();

    List<Ticket> findAllByUserId(Long userId);

    Ticket getOne(Long id);

    /* @Query("SELECT u FROM User u where u.userId = :id")
    List<Ticket> getAllByUserId(@Param("id") Long id);*/

    @Query("SELECT t FROM Ticket t where t.playId = :playId")
    List<Ticket> findAllByPlayId(@Param("playId") Long playId);
}
