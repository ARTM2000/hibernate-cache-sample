package me.artm2000.hibernatecache.service;

import me.artm2000.hibernatecache.database.entity.Note;

import java.util.List;

public interface NoteService {
    Note createNote(Note note);

    Note getNoteById(Long id);

    Note getNoteByTitle(String title);

    List<Note> getAllNonArchivedNotes();

    List<Note> getAllNotes();

    void updateNoteById(Long id, Note note);

    void archiveNoteById(Long id);

    void deleteNoteById(Long id);
}
