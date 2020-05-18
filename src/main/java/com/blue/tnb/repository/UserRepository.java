package com.blue.tnb.repository;

import com.blue.tnb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query(value="Select id from User u WHERE u.email= :userEmail",nativeQuery = true)
    Long getUserIdByEmail(@Param("userEmail") String userEmail);
//    @Query(value="Select * from User u WHERE u.subscriber= true",nativeQuery = true)
//    List<User> getAllSubscribers();

    @Query(value="With tab_user as (Select u.id as user_id, u.email,t.book_date,RANK() OVER (PARTITION BY u.id ORDER BY book_date desc) as rk\n" +
            "\t\t\t\t\tfrom User u left join Ticket t on u.id = t.user_id \n" +
            "\t\t\t\t      where u.subscriber = 1)\n" +
            "Select email from tab_user \n" +
            "where rk = 1 \n" +
            "and TIMESTAMPDIFF(day,ifnull(book_date,DATE_SUB(current_timestamp, INTERVAL 12 month)),current_timestamp) >= 30",nativeQuery = true)
    List<String> getAllSubscribersThatCanBookTickets();
}
