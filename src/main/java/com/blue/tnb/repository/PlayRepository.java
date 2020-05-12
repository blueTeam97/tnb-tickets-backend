package com.blue.tnb.repository;

import com.blue.tnb.model.Play;
import com.blue.tnb.model.Ticket;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@ComponentScan
public interface PlayRepository extends JpaRepository<Play, Long> {
    @Query("SELECT p FROM Play p")
    List<Play> findAll();

    Optional<Play> findAllByPlayName(String playName);

    Optional<Play> findAllById(@Param("id") Long id);

    @Query("SELECT p FROM Play p WHERE p.id=:id")
    Optional<Play> findOneById(@Param("id") Long id);

    Play getOne(Long id);
   /* @Query("SELECT u FROM User u where u.userId = :id")
    List<Ticket> getAllByUserId(@Param("id") Long id);*/


}
