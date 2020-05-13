package com.blue.tnb.repository;

import com.blue.tnb.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;


@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("SELECT t FROM Ticket t")
    List<Ticket> findAll();

    Ticket getOne(Long id);

    @Query("SELECT t FROM Ticket t WHERE t.id= :id")
    Ticket findOneById(@Param("id")Long id);

    @Query("SELECT t FROM Ticket t WHERE t.playId= :playId")
    List<Ticket> findAllByPlayId(@Param("playId")Long playId);

    @Query(value = "Select Count(t.id) FROM Ticket t WHERE t.status='booked' and t.play_id= :play_id",nativeQuery = true)
    Long countAllBookedTicketsByPlayId(@Param("play_id") Long playId);

    @Query(value = "SELECT Count(t.id) FROM Ticket t WHERE t.status='free' AND t.play_id= :playId",nativeQuery = true)
    Long countAllAvailableByPlayId(@Param("playId") Long playId);

    @Query(value = "Select * from Ticket t where t.user_id= :userId",nativeQuery = true)
    List<Ticket> findAllByUserId(@Param("userId") Long userId);

    @Query(value ="Select * from Ticket t where t.play_id= :playId AND t.status='free'",nativeQuery = true)
    List<Ticket> findAllAvailableByPlayId(@Param("playId") Long playId);
}
