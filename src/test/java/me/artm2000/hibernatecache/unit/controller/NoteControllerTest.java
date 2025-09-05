package me.artm2000.hibernatecache.unit.controller;

import me.artm2000.hibernatecache.controller.NoteController;
import me.artm2000.hibernatecache.database.entity.Note;
import me.artm2000.hibernatecache.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    private Note testNote;
    private Note archivedNote;

    @BeforeEach
    void setUp() {
        testNote = new Note();
        testNote.setId(1L);
        testNote.setTitle("Test Note");
        testNote.setContent("This is a test note content");
        testNote.setArchived(false);

        archivedNote = new Note();
        archivedNote.setId(2L);
        archivedNote.setTitle("Archived Note");
        archivedNote.setContent("This is an archived note content");
        archivedNote.setArchived(true);
    }

    @Test
    void createNote_WithValidNote_ShouldReturnCreatedNote() {
        // Given
        Note inputNote = new Note();
        inputNote.setTitle("New Note");
        inputNote.setContent("New Content");
        inputNote.setArchived(false);

        when(noteService.createNote(inputNote)).thenReturn(testNote);

        // When
        Note result = noteController.createNote(inputNote);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Note");
        assertThat(result.getContent()).isEqualTo("This is a test note content");
        assertThat(result.getArchived()).isFalse();

        verify(noteService, times(1)).createNote(inputNote);
    }

    @Test
    void createNote_WithNullNote_ShouldHandleGracefully() {
        // Given
        when(noteService.createNote(null)).thenReturn(null);

        // When
        Note result = noteController.createNote(null);

        // Then
        assertThat(result).isNull();
        verify(noteService, times(1)).createNote(null);
    }

    @Test
    void createNote_WithEmptyNote_ShouldCreateNote() {
        // Given
        Note emptyNote = new Note();
        when(noteService.createNote(emptyNote)).thenReturn(testNote);

        // When
        Note result = noteController.createNote(emptyNote);

        // Then
        assertThat(result).isEqualTo(testNote);
        verify(noteService, times(1)).createNote(emptyNote);
    }

    @Test
    void getNoteById_WhenNoteExists_ShouldReturnNote() {
        // Given
        when(noteService.getNoteById(1L)).thenReturn(testNote);

        // When
        Note result = noteController.getNoteById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Note");
        assertThat(result.getContent()).isEqualTo("This is a test note content");
        assertThat(result.getArchived()).isFalse();

        verify(noteService, times(1)).getNoteById(1L);
    }

    @Test
    void getNoteById_WhenNoteNotFound_ShouldReturnNull() {
        // Given
        when(noteService.getNoteById(999L)).thenReturn(null);

        // When
        Note result = noteController.getNoteById(999L);

        // Then
        assertThat(result).isNull();
        verify(noteService, times(1)).getNoteById(999L);
    }

    @Test
    void getNoteById_WithNullId_ShouldCallService() {
        // Given
        when(noteService.getNoteById(null)).thenReturn(null);

        // When
        Note result = noteController.getNoteById(null);

        // Then
        assertThat(result).isNull();
        verify(noteService, times(1)).getNoteById(null);
    }

    @Test
    void getNoteByTitle_WhenNoteExists_ShouldReturnNote() {
        // Given
        when(noteService.getNoteByTitle("Test Note")).thenReturn(testNote);

        // When
        Note result = noteController.getNoteByTitle("Test Note");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Note");
        assertThat(result.getContent()).isEqualTo("This is a test note content");

        verify(noteService, times(1)).getNoteByTitle("Test Note");
    }

    @Test
    void getNoteByTitle_WhenNoteNotFound_ShouldReturnNull() {
        // Given
        when(noteService.getNoteByTitle("Non-existent")).thenReturn(null);

        // When
        Note result = noteController.getNoteByTitle("Non-existent");

        // Then
        assertThat(result).isNull();
        verify(noteService, times(1)).getNoteByTitle("Non-existent");
    }

    @Test
    void getNoteByTitle_WithNullTitle_ShouldCallService() {
        // Given
        when(noteService.getNoteByTitle(null)).thenReturn(null);

        // When
        Note result = noteController.getNoteByTitle(null);

        // Then
        assertThat(result).isNull();
        verify(noteService, times(1)).getNoteByTitle(null);
    }

    @Test
    void getNoteByTitle_WithEmptyTitle_ShouldCallService() {
        // Given
        when(noteService.getNoteByTitle("")).thenReturn(null);

        // When
        Note result = noteController.getNoteByTitle("");

        // Then
        assertThat(result).isNull();
        verify(noteService, times(1)).getNoteByTitle("");
    }

    @Test
    void getAllNonArchivedNotes_WhenNotesExist_ShouldReturnNonArchivedNotes() {
        // Given
        List<Note> nonArchivedNotes = Arrays.asList(testNote);
        when(noteService.getAllNonArchivedNotes()).thenReturn(nonArchivedNotes);

        // When
        List<Note> result = noteController.getAllNonArchivedNotes();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getArchived()).isFalse();
        assertThat(result.get(0).getTitle()).isEqualTo("Test Note");

        verify(noteService, times(1)).getAllNonArchivedNotes();
    }

    @Test
    void getAllNonArchivedNotes_WhenNoNotesExist_ShouldReturnEmptyList() {
        // Given
        when(noteService.getAllNonArchivedNotes()).thenReturn(Collections.emptyList());

        // When
        List<Note> result = noteController.getAllNonArchivedNotes();

        // Then
        assertThat(result).isEmpty();
        verify(noteService, times(1)).getAllNonArchivedNotes();
    }

    @Test
    void getAllNotes_WhenNotesExist_ShouldReturnAllNotes() {
        // Given
        List<Note> allNotes = Arrays.asList(testNote, archivedNote);
        when(noteService.getAllNotes()).thenReturn(allNotes);

        // When
        List<Note> result = noteController.getAllNotes();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(testNote, archivedNote);

        verify(noteService, times(1)).getAllNotes();
    }

    @Test
    void getAllNotes_WhenNoNotesExist_ShouldReturnEmptyList() {
        // Given
        when(noteService.getAllNotes()).thenReturn(Collections.emptyList());

        // When
        List<Note> result = noteController.getAllNotes();

        // Then
        assertThat(result).isEmpty();
        verify(noteService, times(1)).getAllNotes();
    }

    @Test
    void updateNoteById_WithValidData_ShouldReturnNoContent() {
        // Given
        Note updateNote = new Note();
        updateNote.setTitle("Updated Title");
        updateNote.setContent("Updated Content");
        updateNote.setArchived(true);

        doNothing().when(noteService).updateNoteById(eq(1L), eq(updateNote));

        // When
        ResponseEntity<Void> result = noteController.updateNoteById(1L, updateNote);

        // Then
        assertThat(result.getStatusCode().value()).isEqualTo(204); // No Content
        assertThat(result.getBody()).isNull();

        verify(noteService, times(1)).updateNoteById(1L, updateNote);
    }

    @Test
    void updateNoteById_WithNullNote_ShouldStillCallService() {
        // Given
        doNothing().when(noteService).updateNoteById(eq(1L), eq(null));

        // When
        ResponseEntity<Void> result = noteController.updateNoteById(1L, null);

        // Then
        assertThat(result.getStatusCode().value()).isEqualTo(204);
        verify(noteService, times(1)).updateNoteById(1L, null);
    }

    @Test
    void updateNoteById_WithNullId_ShouldStillCallService() {
        // Given
        Note updateNote = new Note();
        doNothing().when(noteService).updateNoteById(eq(null), eq(updateNote));

        // When
        ResponseEntity<Void> result = noteController.updateNoteById(null, updateNote);

        // Then
        assertThat(result.getStatusCode().value()).isEqualTo(204);
        verify(noteService, times(1)).updateNoteById(null, updateNote);
    }

    @Test
    void archiveNoteById_WithValidId_ShouldReturnNoContent() {
        // Given
        doNothing().when(noteService).archiveNoteById(1L);

        // When
        ResponseEntity<Void> result = noteController.archiveNoteById(1L);

        // Then
        assertThat(result.getStatusCode().value()).isEqualTo(204); // No Content
        assertThat(result.getBody()).isNull();

        verify(noteService, times(1)).archiveNoteById(1L);
    }

    @Test
    void archiveNoteById_WithNullId_ShouldStillCallService() {
        // Given
        doNothing().when(noteService).archiveNoteById(null);

        // When
        ResponseEntity<Void> result = noteController.archiveNoteById(null);

        // Then
        assertThat(result.getStatusCode().value()).isEqualTo(204);
        verify(noteService, times(1)).archiveNoteById(null);
    }

    @Test
    void archiveNoteById_WithNonExistentId_ShouldStillReturnNoContent() {
        // Given
        doNothing().when(noteService).archiveNoteById(999L);

        // When
        ResponseEntity<Void> result = noteController.archiveNoteById(999L);

        // Then
        assertThat(result.getStatusCode().value()).isEqualTo(204);
        verify(noteService, times(1)).archiveNoteById(999L);
    }

    @Test
    void deleteNoteById_WithValidId_ShouldReturnNoContent() {
        // Given
        doNothing().when(noteService).deleteNoteById(1L);

        // When
        ResponseEntity<Void> result = noteController.deleteNoteById(1L);

        // Then
        assertThat(result.getStatusCode().value()).isEqualTo(204); // No Content
        assertThat(result.getBody()).isNull();

        verify(noteService, times(1)).deleteNoteById(1L);
    }

    @Test
    void deleteNoteById_WithNullId_ShouldStillCallService() {
        // Given
        doNothing().when(noteService).deleteNoteById(null);

        // When
        ResponseEntity<Void> result = noteController.deleteNoteById(null);

        // Then
        assertThat(result.getStatusCode().value()).isEqualTo(204);
        verify(noteService, times(1)).deleteNoteById(null);
    }

    @Test
    void deleteNoteById_WithNonExistentId_ShouldStillReturnNoContent() {
        // Given
        doNothing().when(noteService).deleteNoteById(999L);

        // When
        ResponseEntity<Void> result = noteController.deleteNoteById(999L);

        // Then
        assertThat(result.getStatusCode().value()).isEqualTo(204);
        verify(noteService, times(1)).deleteNoteById(999L);
    }

    @Test
    void createNote_WithSpecialCharacters_ShouldHandleCorrectly() {
        // Given
        Note specialNote = new Note();
        specialNote.setTitle("Special chars: @#$%");
        specialNote.setContent("Content with 🚀 emoji");

        when(noteService.createNote(specialNote)).thenReturn(specialNote);

        // When
        Note result = noteController.createNote(specialNote);

        // Then
        assertThat(result).isEqualTo(specialNote);
        verify(noteService, times(1)).createNote(specialNote);
    }
}
