package com.blue.tnb.repository;

import com.blue.tnb.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t")
    List<Ticket> findAll();

    Ticket getOne(Long id);

    @Query("SELECT t FROM Ticket t WHERE t.id= :id")
    Ticket findOneById(@Param("id") Long id);

    @Query("SELECT t FROM Ticket t WHERE t.playId= :playId")
    List<Ticket> findAllByPlayId(@Param("playId") Long playId);

    @Query(value = "SELECT Count(t.id) FROM Ticket t WHERE t.status='free' AND t.play_id= :playId", nativeQuery = true)
    Long countAllAvailableByPlayId(@Param("playId") Long playId);

    @Query(value = "Select t.* from Ticket t Join Play p On t.play_id=p.id where t.user_id= :userId", nativeQuery = true)
    List<Ticket> findAllByUserId(@Param("userId") Long userId);

    @Query(value = "Select * from Ticket t where t.play_id= :playId AND t.status='free'", nativeQuery = true)
    List<Ticket> findAllAvailableByPlayId(@Param("playId") Long playId);

    @Query(value = "SELECT t.* FROM Ticket t WHERE (t.status='booked' OR t.status='pickedup') AND t.play_id= :playId", nativeQuery = true)
    List<Ticket> findAllBookedTicketsByPlayId(Long playId);

    @Query(value = "Select * from ticket t where t.user_id= :userId and t.book_date= :bookDate", nativeQuery = true)
    Optional<Ticket> findTicketByUserIdAndBookDate(@Param("userId") Long userId, @Param("bookDate") String bookDate);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Ticket WHERE id=:ticket_id", nativeQuery = true)
    void deleteTicket(@Param("ticket_id") Long id);

    @Query(value = "SELECT Count(t.id) FROM Ticket t WHERE t.status='booked' AND t.play_id= :playId", nativeQuery = true)
    Long countAllBookedTicketsByPlayId(Long playId);

    @Query(value = "SELECT DISTINCT CONCAT(u.email, \"||\", p.name, \"||\", p.play_date) FROM user u LEFT JOIN ticket t ON u.id = t.user_id  \n" +
            "LEFT JOIN play p ON t.play_id = p.id WHERE t.status = 'booked' AND u.subscriber=1", nativeQuery = true)
    List<String> findEmailsForTicketStatusBooked();

    @Modifying
    @Transactional
    @Query(value = "UPDATE ticket SET user_id = NULL , status = 'free', book_date = NULL WHERE status = 'booked' AND id <> 0;", nativeQuery = true)
    void updateTicketStatusToFree();

    @Modifying
    @Transactional
    @Query(value = "UPDATE ticket AS t INNER JOIN play AS p ON t.play_id = p.id SET t.user_id = NULL, t.status = 'free', t.book_date = NULL WHERE t.status = 'booked' AND p.play_date < current_timestamp();", nativeQuery = true)
    void freeBookedTicketsOncePlayDateInThePast();

    @Query(value = "Select subscriber from user where email= :userEmail", nativeQuery = true)
    Boolean getSubscribeStateForUser(@Param("userEmail") String userEmail);

    @Modifying
    @Transactional
    @Query(value = "Update ticket set status='free', book_date=null, user_id=null where user_id= :userId and play_id= :playId and status!='pickedup'", nativeQuery = true)
    void unbookTicket(@Param("userId") Long userId, @Param("playId") Long playId);



    /*@Query(value="Select t.* from ticket t join user u on t.user_id = u.id where u.id= :userId order by t.book_date desc limit 4",nativeQuery = true)
    List<Ticket> getUserHistoryByPage(@Param("userId") Long userId)*/
}
