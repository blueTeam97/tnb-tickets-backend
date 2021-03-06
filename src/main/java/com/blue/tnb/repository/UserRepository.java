package com.blue.tnb.repository;

import com.blue.tnb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query(value="Select id from User u WHERE u.email= :userEmail",nativeQuery = true)
    Long getUserIdByEmail(@Param("userEmail") String userEmail);

    @Query(value = "Select email From User u WHERE u.id = :id", nativeQuery = true)
    Optional<String> getEmailByUserId(@Param("id") Long id);
//    @Query(value="Select * from User u WHERE u.subscriber= true",nativeQuery = true)
//    List<User> getAllSubscribers();

    @Query(value="With tab_user as (Select u.id as user_id, u.email,t.book_date,RANK() OVER (PARTITION BY u.id ORDER BY book_date desc) as rk\n" +
            "\t\t\t\t\tfrom User u left join Ticket t on u.id = t.user_id \n" +
            "\t\t\t\t      where u.subscriber = 1)\n" +
            "Select distinct email from tab_user \n" +
            "where rk = 1 \n" +
            "and TIMESTAMPDIFF(day,ifnull(book_date,DATE_SUB(current_timestamp, INTERVAL 12 month)),current_timestamp) >= 30",nativeQuery = true)
    List<String> getAllSubscribersThatCanBookTickets();

    @Modifying
    @Transactional
    @Query(value = "Update user set subscriber = 1-(Select s.subscriber from (select * from user) as s where s.id = :userId) where id = :userId",nativeQuery = true)
    void updateSubscribeForUser(@Param("userId") Long id1);

    @Modifying
    @Transactional
    @Query(value="Update user set last_book=current_timestamp where id= :userId",nativeQuery = true)
    void updateLastBookForUser(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value="Update user set last_book=(last_book-INTERVAL 29 DAY-INTERVAL 23 HOUR-INTERVAL 59 MINUTE) where id= :userId and (Select Count(id) from ticket where user_id= :userId and play_id= :playId and status='pickedup') = 0",nativeQuery = true)
    void clearUserLastBookedDate(@Param("userId") Long userId,@Param("playId") Long playId);
//    (last_book-INTERVAL 29 DAY-INTERVAL 23 HOUR-INTERVAL 55 MINUTE)
}
