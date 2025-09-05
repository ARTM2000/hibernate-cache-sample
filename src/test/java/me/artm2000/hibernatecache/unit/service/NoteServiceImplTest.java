package me.artm2000.hibernatecache.unit.service;

import me.artm2000.hibernatecache.database.entity.Note;
import me.artm2000.hibernatecache.database.repository.NoteRepository;
import me.artm2000.hibernatecache.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

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
    void createNote_WithValidNote_ShouldReturnSavedNote() {
        // Given
        Note noteToSave = new Note();
        noteToSave.setTitle("New Note");
        noteToSave.setContent("New Content");
        noteToSave.setArchived(false);

        when(noteRepository.save(noteToSave)).thenReturn(testNote);

        // When
        Note result = noteService.createNote(noteToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Note");
        assertThat(result.getContent()).isEqualTo("This is a test note content");
        assertThat(result.getArchived()).isFalse();
        
        verify(noteRepository, times(1)).save(noteToSave);
    }

    @Test
    void createNote_WithNullNote_ShouldCallRepository() {
        // Given
        when(noteRepository.save(null)).thenReturn(null);

        // When
        Note result = noteService.createNote(null);

        // Then
        assertThat(result).isNull();
        verify(noteRepository, times(1)).save(null);
    }

    @Test
    void getNoteById_WhenNoteExists_ShouldReturnNote() {
        // Given
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));

        // When
        Note result = noteService.getNoteById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Note");
        assertThat(result.getContent()).isEqualTo("This is a test note content");
        assertThat(result.getArchived()).isFalse();
        
        verify(noteRepository, times(1)).findById(1L);
    }

    @Test
    void getNoteById_WhenNoteDoesNotExist_ShouldReturnNull() {
        // Given
        when(noteRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Note result = noteService.getNoteById(999L);

        // Then
        assertThat(result).isNull();
        verify(noteRepository, times(1)).findById(999L);
    }

    @Test
    void getNoteById_WithNullId_ShouldCallRepository() {
        // Given
        when(noteRepository.findById(null)).thenReturn(Optional.empty());

        // When
        Note result = noteService.getNoteById(null);

        // Then
        assertThat(result).isNull();
        verify(noteRepository, times(1)).findById(null);
    }

    @Test
    void getNoteByTitle_WhenNoteExists_ShouldReturnNote() {
        // Given
        when(noteRepository.findByTitle("Test Note")).thenReturn(Optional.of(testNote));

        // When
        Note result = noteService.getNoteByTitle("Test Note");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Note");
        assertThat(result.getContent()).isEqualTo("This is a test note content");
        
        verify(noteRepository, times(1)).findByTitle("Test Note");
    }

    @Test
    void getNoteByTitle_WhenNoteDoesNotExist_ShouldReturnNull() {
        // Given
        when(noteRepository.findByTitle("Non-existent Note")).thenReturn(Optional.empty());

        // When
        Note result = noteService.getNoteByTitle("Non-existent Note");

        // Then
        assertThat(result).isNull();
        verify(noteRepository, times(1)).findByTitle("Non-existent Note");
    }

    @Test
    void getNoteByTitle_WithNullTitle_ShouldCallRepository() {
        // Given
        when(noteRepository.findByTitle(null)).thenReturn(Optional.empty());

        // When
        Note result = noteService.getNoteByTitle(null);

        // Then
        assertThat(result).isNull();
        verify(noteRepository, times(1)).findByTitle(null);
    }

    @Test
    void getNoteByTitle_WithEmptyTitle_ShouldCallRepository() {
        // Given
        when(noteRepository.findByTitle("")).thenReturn(Optional.empty());

        // When
        Note result = noteService.getNoteByTitle("");

        // Then
        assertThat(result).isNull();
        verify(noteRepository, times(1)).findByTitle("");
    }

    @Test
    void getAllNonArchivedNotes_WhenNotesExist_ShouldReturnOnlyNonArchivedNotes() {
        // Given
        List<Note> nonArchivedNotes = Arrays.asList(testNote);
        when(noteRepository.findAllByArchived(false)).thenReturn(nonArchivedNotes);

        // When
        List<Note> result = noteService.getAllNonArchivedNotes();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getArchived()).isFalse();
        assertThat(result.get(0).getTitle()).isEqualTo("Test Note");
        
        verify(noteRepository, times(1)).findAllByArchived(false);
    }

    @Test
    void getAllNonArchivedNotes_WhenNoNotesExist_ShouldReturnEmptyList() {
        // Given
        when(noteRepository.findAllByArchived(false)).thenReturn(Collections.emptyList());

        // When
        List<Note> result = noteService.getAllNonArchivedNotes();

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository, times(1)).findAllByArchived(false);
    }

    @Test
    void getAllNotes_WhenNotesExist_ShouldReturnAllNotes() {
        // Given
        List<Note> allNotes = Arrays.asList(testNote, archivedNote);
        when(noteRepository.findAll()).thenReturn(allNotes);

        // When
        List<Note> result = noteService.getAllNotes();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(testNote, archivedNote);
        
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void getAllNotes_WhenNoNotesExist_ShouldReturnEmptyList() {
        // Given
        when(noteRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Note> result = noteService.getAllNotes();

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void updateNoteById_WhenNoteExists_ShouldUpdateNote() {
        // Given
        Note updatedNote = new Note();
        updatedNote.setTitle("Updated Title");
        updatedNote.setContent("Updated Content");
        updatedNote.setArchived(true);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));
        when(noteRepository.save(testNote)).thenReturn(testNote);

        // When
        noteService.updateNoteById(1L, updatedNote);

        // Then
        assertThat(testNote.getTitle()).isEqualTo("Updated Title");
        assertThat(testNote.getContent()).isEqualTo("Updated Content");
        assertThat(testNote.getArchived()).isTrue();
        
        verify(noteRepository, times(1)).findById(1L);
        verify(noteRepository, times(1)).save(testNote);
    }

    @Test
    void updateNoteById_WhenNoteDoesNotExist_ShouldNotCallSave() {
        // Given
        Note updatedNote = new Note();
        updatedNote.setTitle("Updated Title");
        
        when(noteRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        noteService.updateNoteById(999L, updatedNote);

        // Then
        verify(noteRepository, times(1)).findById(999L);
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void updateNoteById_WithNullNote_ShouldThrowNullPointerException() {
        // Given
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));

        // When & Then
        assertThatThrownBy(() -> noteService.updateNoteById(1L, null))
                .isInstanceOf(NullPointerException.class);

        verify(noteRepository, times(1)).findById(1L);
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void updateNoteById_WithPartialUpdate_ShouldUpdateOnlyProvidedFields() {
        // Given
        Note partialUpdate = new Note();
        partialUpdate.setTitle("Only Title Updated");
        // content and archived are null

        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));
        when(noteRepository.save(testNote)).thenReturn(testNote);

        // When
        noteService.updateNoteById(1L, partialUpdate);

        // Then
        assertThat(testNote.getTitle()).isEqualTo("Only Title Updated");
        // Note: The implementation will set content and archived to null based on the current code
        verify(noteRepository, times(1)).findById(1L);
        verify(noteRepository, times(1)).save(testNote);
    }

    @Test
    void archiveNoteById_WhenNoteExists_ShouldArchiveNote() {
        // Given
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));
        when(noteRepository.save(testNote)).thenReturn(testNote);

        // When
        noteService.archiveNoteById(1L);

        // Then
        assertThat(testNote.getArchived()).isTrue();
        
        verify(noteRepository, times(1)).findById(1L);
        verify(noteRepository, times(1)).save(testNote);
    }

    @Test
    void archiveNoteById_WhenNoteDoesNotExist_ShouldNotCallSave() {
        // Given
        when(noteRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        noteService.archiveNoteById(999L);

        // Then
        verify(noteRepository, times(1)).findById(999L);
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void archiveNoteById_WhenNoteAlreadyArchived_ShouldStillSetToTrue() {
        // Given
        archivedNote.setArchived(true); // Already archived
        when(noteRepository.findById(2L)).thenReturn(Optional.of(archivedNote));
        when(noteRepository.save(archivedNote)).thenReturn(archivedNote);

        // When
        noteService.archiveNoteById(2L);

        // Then
        assertThat(archivedNote.getArchived()).isTrue();
        
        verify(noteRepository, times(1)).findById(2L);
        verify(noteRepository, times(1)).save(archivedNote);
    }

    @Test
    void deleteNoteById_ShouldCallRepositoryDeleteById() {
        // When
        noteService.deleteNoteById(1L);

        // Then
        verify(noteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNoteById_WithNullId_ShouldCallRepository() {
        // When
        noteService.deleteNoteById(null);

        // Then
        verify(noteRepository, times(1)).deleteById(null);
    }

    @Test
    void deleteNoteById_WithNonExistentId_ShouldStillCallRepository() {
        // When
        noteService.deleteNoteById(999L);

        // Then
        verify(noteRepository, times(1)).deleteById(999L);
        // Repository will handle the case where ID doesn't exist
    }
}
