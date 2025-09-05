package me.artm2000.hibernatecache.database.repository;

import jakarta.persistence.QueryHint;
import me.artm2000.hibernatecache.database.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true"),
        @QueryHint(name = "org.hibernate.cacheRegion", value = "query.findNotesByTitle")
    })
    Optional<Note> findByTitle(String title);

    @Override
    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true"),
        @QueryHint(name = "org.hibernate.cacheRegion", value = "query.findAllNotes")
    })
    List<Note> findAll();

    @QueryHints({
        @QueryHint(name = "org.hibernate.cacheable", value = "true"),
        @QueryHint(name = "org.hibernate.cacheRegion", value = "query.findAllNotesByArchived")
    })
    List<Note> findAllByArchived(Boolean archived);
}
