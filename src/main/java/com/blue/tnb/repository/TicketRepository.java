package com.blue.tnb.repository;

import com.blue.tnb.dto.TicketDTO;
import com.blue.tnb.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("SELECT t FROM Ticket t")
    List<Ticket> findAll();

    Ticket getOne(Long id);

    @Query("SELECT t FROM Ticket t WHERE t.id= :id")
    Ticket findOneById(@Param("id")Long id);

    @Query("SELECT t FROM Ticket t WHERE t.playId= :playId")
    List<Ticket> findAllByPlayId(@Param("playId")Long playId);

    @Query(value = "SELECT Count(t.id) FROM Ticket t WHERE t.status='free' AND t.play_id= :playId",nativeQuery = true)
    Long countAllAvailableByPlayId(@Param("playId") Long playId);

    @Query(value = "Select t.* from Ticket t Join Play p On t.play_id=p.id where t.user_id= :userId",nativeQuery = true)
    List<Ticket> findAllByUserId(@Param("userId") Long userId);

    @Query(value ="Select * from Ticket t where t.play_id= :playId AND t.status='free'",nativeQuery = true)
    List<Ticket> findAllAvailableByPlayId(@Param("playId") Long playId);

    @Query(value = "SELECT t.* FROM Ticket t WHERE t.status='booked' AND t.play_id= :playId",nativeQuery = true)
     List<Ticket> findAllBookedTicketsByPlayId(Long playId);


    @Modifying
    @Transactional
    @Query(value="DELETE FROM Ticket WHERE id=:ticket_id",nativeQuery = true)
    void deleteTicket(@Param("ticket_id") Long id);

    @Query(value = "SELECT Count(t.id) FROM Ticket t WHERE t.status='booked' AND t.play_id= :playId",nativeQuery = true)
    Long countAllBookedTicketsByPlayId(Long playId);

    @Query(value="SELECT DISTINCT CONCAT(u.email, \"||\", p.name, \"||\", p.play_date) FROM user u LEFT JOIN ticket t ON u.id = t.user_id  LEFT JOIN play p ON t.play_id = p.id WHERE t.status = 'booked'",nativeQuery = true)
    List<String> findEmailsForTicketStatusBooked();

    @Query(value = "Select subscriber from user where email= :userEmail", nativeQuery = true)
    Boolean getSubscribeStateForUser(@Param("userEmail") String userEmail);
}
