package me.artm2000.hibernatecache.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.artm2000.hibernatecache.database.entity.Note;
import me.artm2000.hibernatecache.database.repository.NoteRepository;
import me.artm2000.hibernatecache.service.NoteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    @Override
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    @Override
    public Note getNoteByTitle(String title) {
        return noteRepository.findByTitle(title).orElse(null);
    }

    @Override
    public List<Note> getAllNonArchivedNotes() {
        return noteRepository.findAllByArchived(false);
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public void updateNoteById(Long id, Note note) {
        Optional<Note> existingNote = noteRepository.findById(id);
        if (existingNote.isPresent()) {
            Note currentNote = existingNote.get();
            currentNote.setTitle(note.getTitle());
            currentNote.setContent(note.getContent());
            currentNote.setArchived(note.getArchived());
            noteRepository.save(currentNote);
        }
    }

    @Override
    public void archiveNoteById(Long id) {
        Optional<Note> existingNote = noteRepository.findById(id);
        if (existingNote.isPresent()) {
            Note currentNote = existingNote.get();
            currentNote.setArchived(true);
            noteRepository.save(currentNote);
        }
    }

    @Override
    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }
}
