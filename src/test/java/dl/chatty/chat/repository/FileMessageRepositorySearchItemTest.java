package dl.chatty.chat.repository;

import java.nio.file.Paths;

import static org.junit.Assert.*;

import org.junit.Test;

import dl.chatty.chat.repository.FileMessageRepository.SearchItem;

public class FileMessageRepositorySearchItemTest {

    @Test
    public void shouldBeValid() {
        SearchItem item = SearchItem.of(Paths.get("id_1_creator"));

        assertTrue(SearchItem.isValid(item));
    }

    @Test
    public void shouldNotBeValidOnMissingSegment() {
        SearchItem item = SearchItem.of(Paths.get("id_1"));

        assertFalse(SearchItem.isValid(item));
    }

    @Test
    public void shouldNotBeValidOnAdditionalSegment() {
        SearchItem item = SearchItem.of(Paths.get("id_1_sender_1"));

        assertFalse(SearchItem.isValid(item));
    }

    @Test
    public void shouldNotBeValidOnTimestampLessZero() {
        SearchItem item = SearchItem.of(Paths.get("id_-1_sender"));

        assertFalse(SearchItem.isValid(item));
    }

    @Test
    public void shouldNotBeValidOnNonNumberTimestamp() {
        SearchItem item = SearchItem.of(Paths.get("id_a_sender"));

        assertFalse(SearchItem.isValid(item));
    }

}
