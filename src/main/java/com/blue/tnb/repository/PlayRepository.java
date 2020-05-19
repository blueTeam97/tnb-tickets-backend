package com.blue.tnb.repository;

import com.blue.tnb.model.Play;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@ComponentScan
public interface PlayRepository extends JpaRepository<Play, Long> {

    @Query("Select p FROM Play p WHERE p.playDate > current_timestamp") // - for user
    List<Play> findAll();

    @Query(value = "Select p.* from Play p Join Ticket t on p.id=t.play_id",nativeQuery = true)
    List<Play> findAllNoRestriction();
    @Query(value = "SELECT * from play p where (SELECT Count(t.id) FROM Ticket t WHERE t.status='free' AND t.play_id= p.id)>0 and p.available_date>=current_timestamp()",nativeQuery = true)
    List<Play> getAllAvailablePlays();

    Optional<Play> findAllByPlayName(String playName);

    Optional<Play> findAllById(@Param("id") Long id);

    Optional<Play> findById(Long id);

    Optional<Play> findByPlayName(String playName);

    @Query(value = "Select * from Play p where p.available_date >= :localDateTimeFrom \n" +
            "  AND p.available_date < :localDateTimeTo ;",nativeQuery = true)
    List<Play> getNextAvailablePlays(@Param("localDateTimeFrom") LocalDateTime localDateTimeFrom,
                                     @Param("localDateTimeTo")LocalDateTime localDateTimeTo);

}
