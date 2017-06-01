package dl.chatty.chat.repository;

import java.nio.file.Paths;

import static org.junit.Assert.*;

import org.junit.Test;

import dl.chatty.chat.repository.FileChatRepository.SearchItem;

public class FileChatRepositorySearchItemTest {

    @Test
    public void shouldBeValid() {
        SearchItem item = SearchItem.of(Paths.get("id_creator"));

        assertTrue(SearchItem.isValid(item));
    }

    @Test
    public void shouldNotBeValidOnMissingSegment() {
        SearchItem item = SearchItem.of(Paths.get("id"));

        assertFalse(SearchItem.isValid(item));
    }

    @Test
    public void shouldNotBeValidOnAdditionalSegment() {
        SearchItem item = SearchItem.of(Paths.get("id_id_1"));

        assertFalse(SearchItem.isValid(item));
    }

}
