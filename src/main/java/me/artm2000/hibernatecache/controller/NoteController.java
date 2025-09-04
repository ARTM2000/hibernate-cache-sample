package me.artm2000.hibernatecache.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.artm2000.hibernatecache.database.entity.Note;
import me.artm2000.hibernatecache.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    // create notes
    @PostMapping("/v1/notes")
    public Note createNote(@RequestBody Note note) {
        log.info("Creating note: {}", note);
        return noteService.createNote(note);
    }

    // get one note by id
    @GetMapping("/v1/notes/{id}")
    public Note getNoteById(@PathVariable Long id) {
        log.info("Getting note by id: {}", id);
        return noteService.getNoteById(id);
    }

    // get one note by title
    @GetMapping("/v1/notes/search")
    public Note getNoteByTitle(@RequestParam String title) {
        log.info("Getting note by title: {}", title);
        return noteService.getNoteByTitle(title);
    }

    // get all non-archived notes
    @GetMapping("/v1/notes")
    public List<Note> getAllNonArchivedNotes() {
        log.info("Getting all non-archived notes");
        return noteService.getAllNonArchivedNotes();
    }

    // get all notes
    @GetMapping("/v1/notes/all")
    public List<Note> getAllNotes() {
        log.info("Getting all notes");
        return noteService.getAllNotes();
    }

    // update note by id
    @PutMapping("/v1/notes/{id}")
    public ResponseEntity<Void> updateNoteById(@PathVariable Long id, @RequestBody Note note) {
        log.info("Updating note by id: {}", id);
        noteService.updateNoteById(id, note);
        return ResponseEntity.noContent().build();
    }

    // archive note by id
    @PatchMapping("/v1/notes/{id}/archive")
    public ResponseEntity<Void> archiveNoteById(@PathVariable Long id) {
        log.info("Archiving note by id: {}", id);
        noteService.archiveNoteById(id);
        return ResponseEntity.noContent().build();
    }

    // delete note by id
    @DeleteMapping("/v1/notes/{id}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long id) {
        log.info("Deleting note by id: {}", id);
        noteService.deleteNoteById(id);
        return ResponseEntity.noContent().build();
    }
}
