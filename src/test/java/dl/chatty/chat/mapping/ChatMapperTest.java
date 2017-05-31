package dl.chatty.chat.mapping;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import dl.chatty.chat.entity.Chat;
import dl.chatty.chat.view.ChatView;

public class ChatMapperTest {

    @Test
    public void shouldMapToView() {
        Date date = new Date();
        ChatView view = ChatMapper.toView(Chat.of("id", "title", "customer", date));

        assertEquals("id", view.getId());
        assertEquals("title", view.getTitle());
        assertEquals("customer", view.getCreatedBy());
        assertEquals(date, view.getCreateTs());
    }

    @Test
    public void shouldMapToViewList() {
        Date date = new Date();
        List<Chat> chats = Arrays.asList(
                Chat.of("id", "title", "customer", date),
                Chat.of("id1", "title1", "customer1", date));

        assertThat(ChatMapper.toViewList(chats),
                contains(
                        new ChatView("id", "title", "customer", date),
                        new ChatView("id1", "title1", "customer1", date)));
    }

    @Test
    public void shouldMapToEntity() {
        Date date = new Date();
        Chat entity = ChatMapper.toEntity(new ChatView("id", "title", "customer", date));

        assertEquals("id", entity.getId());
        assertEquals("title", entity.getTitle());
        assertEquals("customer", entity.getCreatedBy());
        assertEquals(date, entity.getCreateTs());
    }
}
