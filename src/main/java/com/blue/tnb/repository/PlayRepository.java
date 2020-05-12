package com.blue.tnb.repository;

import com.blue.tnb.model.Play;
import com.blue.tnb.model.Ticket;
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

    //@Query("SELECT p FROM Play p") - for admin
    @Query("Select p FROM Play p WHERE p.playDate > current_timestamp") // - for user
    List<Play> findAll();

    Optional<Play> findById(Long id);

    Optional<Play> findByPlayName(String playName);


}
