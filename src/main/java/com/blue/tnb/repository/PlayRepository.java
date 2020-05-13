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

    //@Query("SELECT p FROM Play p") - for admin
    @Query("Select p FROM Play p WHERE p.playDate > current_timestamp") // - for user
    List<Play> findAll();

    Optional<Play> findAllByPlayName(String playName);

    Optional<Play> findAllById(@Param("id") Long id);

    Optional<Play> findById(Long id);

    Optional<Play> findByPlayName(String playName);

  /*  @Query(value="CALL add_play_with_tickets(:playName,:availableDate,:playDate,:linkLink,:nrTickets,:generatedD)", nativeQuery = true)
    Long addPlay(@Param("playName") String playName,
                 @Param("availableDate") LocalDateTime availableDate,
                 @Param("playDate") LocalDateTime playDate,
                 @Param("linkLink") String link,
                 @Param("nrTickets") int nrTickets,
                 @PathVariable("generatedD") int generatedD);*/

}
