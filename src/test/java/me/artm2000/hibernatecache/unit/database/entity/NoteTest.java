package me.artm2000.hibernatecache.unit.database.entity;

import me.artm2000.hibernatecache.database.entity.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoteTest {

    private Note note;

    @BeforeEach
    void setUp() {
        note = new Note();
    }

    @Test
    void testNoteCreation() {
        // When
        Note newNote = new Note();

        // Then
        assertThat(newNote).isNotNull();
        assertThat(newNote.getId()).isNull();
        assertThat(newNote.getTitle()).isNull();
        assertThat(newNote.getContent()).isNull();
        assertThat(newNote.getArchived()).isFalse(); // Default value
    }

    @Test
    void testSettersAndGetters() {
        // Given
        Long id = 1L;
        String title = "Test Note";
        String content = "This is a test note content";
        Boolean archived = true;

        // When
        note.setId(id);
        note.setTitle(title);
        note.setContent(content);
        note.setArchived(archived);

        // Then
        assertThat(note.getId()).isEqualTo(id);
        assertThat(note.getTitle()).isEqualTo(title);
        assertThat(note.getContent()).isEqualTo(content);
        assertThat(note.getArchived()).isEqualTo(archived);
    }

    @Test
    void testDefaultArchivedValue() {
        // When
        Note newNote = new Note();

        // Then
        assertThat(newNote.getArchived()).isFalse();
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("Test Note");
        note1.setContent("Test Content");
        note1.setArchived(false);

        Note note2 = new Note();
        note2.setId(1L);
        note2.setTitle("Test Note");
        note2.setContent("Test Content");
        note2.setArchived(false);

        Note note3 = new Note();
        note3.setId(2L);
        note3.setTitle("Different Note");
        note3.setContent("Different Content");
        note3.setArchived(true);

        // Then
        assertThat(note1).isEqualTo(note2);
        assertThat(note1).isNotEqualTo(note3);
        assertThat(note1.hashCode()).isEqualTo(note2.hashCode());
        assertThat(note1.hashCode()).isNotEqualTo(note3.hashCode());
    }

    @Test
    void testToString() {
        // Given
        note.setId(1L);
        note.setTitle("Test Note");
        note.setContent("Test Content");
        note.setArchived(false);

        // When
        String toString = note.toString();

        // Then
        assertThat(toString).isNotNull();
        assertThat(toString).contains("Test Note");
        assertThat(toString).contains("Test Content");
        assertThat(toString).contains("false");
    }

    @Test
    void testArchivedToggle() {
        // Given
        note.setArchived(false);

        // When
        note.setArchived(true);

        // Then
        assertThat(note.getArchived()).isTrue();

        // When
        note.setArchived(false);

        // Then
        assertThat(note.getArchived()).isFalse();
    }

    @Test
    void testNullValues() {
        // When
        note.setTitle(null);
        note.setContent(null);
        note.setArchived(null);

        // Then
        assertThat(note.getTitle()).isNull();
        assertThat(note.getContent()).isNull();
        assertThat(note.getArchived()).isNull();
    }

    @Test
    void testEmptyStrings() {
        // When
        note.setTitle("");
        note.setContent("");

        // Then
        assertThat(note.getTitle()).isEmpty();
        assertThat(note.getContent()).isEmpty();
    }

    @Test
    void testLongContent() {
        // Given
        String longContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ".repeat(100);

        // When
        note.setContent(longContent);

        // Then
        assertThat(note.getContent()).isEqualTo(longContent);
        assertThat(note.getContent().length()).isGreaterThan(5000);
    }

    @Test
    void testSpecialCharactersInTitle() {
        // Given
        String specialTitle = "Note with special chars: @#$%^&*()_+-={}[]|\\:;\"'<>?,./";

        // When
        note.setTitle(specialTitle);

        // Then
        assertThat(note.getTitle()).isEqualTo(specialTitle);
    }

    @Test
    void testUnicodeContent() {
        // Given
        String unicodeContent = "Unicode test: 你好世界 🌍 emoji test 🚀";

        // When
        note.setContent(unicodeContent);

        // Then
        assertThat(note.getContent()).isEqualTo(unicodeContent);
    }
}
