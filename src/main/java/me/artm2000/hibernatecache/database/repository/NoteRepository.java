package me.artm2000.hibernatecache.database.repository;

import me.artm2000.hibernatecache.database.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Optional<Note> findByTitle(String title);
    List<Note> findAllByArchived(Boolean archived);
}
