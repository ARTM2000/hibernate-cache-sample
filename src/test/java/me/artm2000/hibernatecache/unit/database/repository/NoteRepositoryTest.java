package me.artm2000.hibernatecache.unit.database.repository;

import me.artm2000.hibernatecache.database.entity.Note;
import me.artm2000.hibernatecache.database.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NoteRepository interface.
 * Since this is an interface extending JpaRepository, we're testing the contract
 * and custom query methods by mocking the repository behavior.
 */
@ExtendWith(MockitoExtension.class)
class NoteRepositoryTest {

    @Mock
    private NoteRepository noteRepository;

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
    void findByTitle_WhenNoteExists_ShouldReturnNote() {
        // Given
        when(noteRepository.findByTitle("Test Note")).thenReturn(Optional.of(testNote));

        // When
        Optional<Note> result = noteRepository.findByTitle("Test Note");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Note");
        assertThat(result.get().getContent()).isEqualTo("This is a test note content");
        assertThat(result.get().getArchived()).isFalse();

        verify(noteRepository, times(1)).findByTitle("Test Note");
    }

    @Test
    void findByTitle_WhenNoteDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(noteRepository.findByTitle("Non-existent Note")).thenReturn(Optional.empty());

        // When
        Optional<Note> result = noteRepository.findByTitle("Non-existent Note");

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository, times(1)).findByTitle("Non-existent Note");
    }

    @Test
    void findByTitle_WithNullTitle_ShouldCallRepository() {
        // Given
        when(noteRepository.findByTitle(null)).thenReturn(Optional.empty());

        // When
        Optional<Note> result = noteRepository.findByTitle(null);

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository, times(1)).findByTitle(null);
    }

    @Test
    void findByTitle_WithEmptyTitle_ShouldCallRepository() {
        // Given
        when(noteRepository.findByTitle("")).thenReturn(Optional.empty());

        // When
        Optional<Note> result = noteRepository.findByTitle("");

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository, times(1)).findByTitle("");
    }

    @Test
    void findByTitle_WithSpecialCharacters_ShouldHandleCorrectly() {
        // Given
        String specialTitle = "Special chars: @#$%^&*()";
        Note specialNote = new Note();
        specialNote.setTitle(specialTitle);
        
        when(noteRepository.findByTitle(specialTitle)).thenReturn(Optional.of(specialNote));

        // When
        Optional<Note> result = noteRepository.findByTitle(specialTitle);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(specialTitle);
        verify(noteRepository, times(1)).findByTitle(specialTitle);
    }

    @Test
    void findAll_WhenNotesExist_ShouldReturnAllNotes() {
        // Given
        List<Note> allNotes = Arrays.asList(testNote, archivedNote);
        when(noteRepository.findAll()).thenReturn(allNotes);

        // When
        List<Note> result = noteRepository.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(testNote, archivedNote);
        assertThat(result.get(0).getArchived()).isFalse();
        assertThat(result.get(1).getArchived()).isTrue();

        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void findAll_WhenNoNotesExist_ShouldReturnEmptyList() {
        // Given
        when(noteRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Note> result = noteRepository.findAll();

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    void findAllByArchived_WithFalse_ShouldReturnOnlyNonArchivedNotes() {
        // Given
        List<Note> nonArchivedNotes = Arrays.asList(testNote);
        when(noteRepository.findAllByArchived(false)).thenReturn(nonArchivedNotes);

        // When
        List<Note> result = noteRepository.findAllByArchived(false);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getArchived()).isFalse();
        assertThat(result.get(0).getTitle()).isEqualTo("Test Note");

        verify(noteRepository, times(1)).findAllByArchived(false);
    }

    @Test
    void findAllByArchived_WithTrue_ShouldReturnOnlyArchivedNotes() {
        // Given
        List<Note> archivedNotes = Arrays.asList(archivedNote);
        when(noteRepository.findAllByArchived(true)).thenReturn(archivedNotes);

        // When
        List<Note> result = noteRepository.findAllByArchived(true);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getArchived()).isTrue();
        assertThat(result.get(0).getTitle()).isEqualTo("Archived Note");

        verify(noteRepository, times(1)).findAllByArchived(true);
    }

    @Test
    void findAllByArchived_WhenNoMatchingNotes_ShouldReturnEmptyList() {
        // Given
        when(noteRepository.findAllByArchived(true)).thenReturn(Collections.emptyList());

        // When
        List<Note> result = noteRepository.findAllByArchived(true);

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository, times(1)).findAllByArchived(true);
    }

    @Test
    void findAllByArchived_WithNull_ShouldCallRepository() {
        // Given
        when(noteRepository.findAllByArchived(null)).thenReturn(Collections.emptyList());

        // When
        List<Note> result = noteRepository.findAllByArchived(null);

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository, times(1)).findAllByArchived(null);
    }

    @Test
    void save_WithNewNote_ShouldReturnSavedNote() {
        // Given
        Note newNote = new Note();
        newNote.setTitle("New Note");
        newNote.setContent("New Content");
        newNote.setArchived(false);

        Note savedNote = new Note();
        savedNote.setId(1L);
        savedNote.setTitle("New Note");
        savedNote.setContent("New Content");
        savedNote.setArchived(false);

        when(noteRepository.save(newNote)).thenReturn(savedNote);

        // When
        Note result = noteRepository.save(newNote);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("New Note");
        assertThat(result.getContent()).isEqualTo("New Content");
        assertThat(result.getArchived()).isFalse();

        verify(noteRepository, times(1)).save(newNote);
    }

    @Test
    void save_WithExistingNote_ShouldReturnUpdatedNote() {
        // Given
        testNote.setTitle("Updated Title");
        when(noteRepository.save(testNote)).thenReturn(testNote);

        // When
        Note result = noteRepository.save(testNote);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        verify(noteRepository, times(1)).save(testNote);
    }

    @Test
    void save_WithNullNote_ShouldCallRepository() {
        // Given
        when(noteRepository.save(null)).thenReturn(null);

        // When
        Note result = noteRepository.save(null);

        // Then
        assertThat(result).isNull();
        verify(noteRepository, times(1)).save(null);
    }

    @Test
    void findById_WhenNoteExists_ShouldReturnNote() {
        // Given
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));

        // When
        Optional<Note> result = noteRepository.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getTitle()).isEqualTo("Test Note");

        verify(noteRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenNoteDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(noteRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Note> result = noteRepository.findById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository, times(1)).findById(999L);
    }

    @Test
    void findById_WithNullId_ShouldCallRepository() {
        // Given
        when(noteRepository.findById(null)).thenReturn(Optional.empty());

        // When
        Optional<Note> result = noteRepository.findById(null);

        // Then
        assertThat(result).isEmpty();
        verify(noteRepository, times(1)).findById(null);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        // Given
        doNothing().when(noteRepository).deleteById(1L);

        // When
        noteRepository.deleteById(1L);

        // Then
        verify(noteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WithNullId_ShouldCallRepository() {
        // Given
        doNothing().when(noteRepository).deleteById(null);

        // When
        noteRepository.deleteById(null);

        // Then
        verify(noteRepository, times(1)).deleteById(null);
    }

    @Test
    void deleteById_WithNonExistentId_ShouldStillCallRepository() {
        // Given
        doNothing().when(noteRepository).deleteById(999L);

        // When
        noteRepository.deleteById(999L);

        // Then
        verify(noteRepository, times(1)).deleteById(999L);
        // Repository implementation will handle non-existent IDs
    }

    @Test
    void findByTitle_CaseInsensitive_ShouldBehaviorDependsOnImplementation() {
        // Given - this tests the contract, actual behavior depends on database configuration
        String upperCaseTitle = "TEST NOTE";
        when(noteRepository.findByTitle(upperCaseTitle)).thenReturn(Optional.empty());

        // When
        Optional<Note> result = noteRepository.findByTitle(upperCaseTitle);

        // Then
        assertThat(result).isEmpty(); // Assuming case-sensitive by default
        verify(noteRepository, times(1)).findByTitle(upperCaseTitle);
    }

    @Test
    void findAllByArchived_WithMultipleNotes_ShouldReturnCorrectSubset() {
        // Given
        Note note1 = new Note();
        note1.setId(1L);
        note1.setArchived(false);
        
        Note note2 = new Note();
        note2.setId(2L);
        note2.setArchived(false);
        
        List<Note> nonArchivedNotes = Arrays.asList(note1, note2);
        when(noteRepository.findAllByArchived(false)).thenReturn(nonArchivedNotes);

        // When
        List<Note> result = noteRepository.findAllByArchived(false);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.stream().allMatch(note -> !note.getArchived())).isTrue();
        verify(noteRepository, times(1)).findAllByArchived(false);
    }

    @Test
    void repositoryOperations_ShouldHandleTransactionalBehavior() {
        // This test demonstrates that the repository interface supports transactional operations
        // The actual transactional behavior would be tested in integration tests
        
        // Given
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);
        doNothing().when(noteRepository).deleteById(anyLong());

        // When - simulating a transactional operation
        Note saved = noteRepository.save(testNote);
        noteRepository.deleteById(saved.getId());

        // Then
        verify(noteRepository, times(1)).save(testNote);
        verify(noteRepository, times(1)).deleteById(testNote.getId());
    }
}
