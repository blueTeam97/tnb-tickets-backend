package com.blue.tnb.repository;

import com.blue.tnb.model.Play;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@ComponentScan
public interface PlayRepository extends JpaRepository<Play, Long> {

    @Query("Select p FROM Play p WHERE p.playDate > current_timestamp") // - for user
    List<Play> findAll();

    @Query(value = "Select p.* from Play p Join Ticket t on p.id=t.play_id",nativeQuery = true)
    List<Play> findAllNoRestriction();

    Optional<Play> findAllByPlayName(String playName);

    Optional<Play> findAllById(@Param("id") Long id);

    Optional<Play> findById(Long id);

    Optional<Play> findByPlayName(String playName);


}
