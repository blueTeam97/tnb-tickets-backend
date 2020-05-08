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

    List<Play> findAll();
    Optional<Play> findAllByPlayName(String playName);
    Optional<Play> findAllById(Long id);

   /* @Query("SELECT u FROM User u where u.userId = :id")
    List<Ticket> getAllByUserId(@Param("id") Long id);*/


}
