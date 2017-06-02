package dl.chatty.chat.repository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dl.chatty.IntegrationTestBase;
import dl.chatty.chat.entity.Chat;
import dl.chatty.chat.entity.Message;

public class MessageRepositoryITest extends IntegrationTestBase {

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private ChatRepository chatRepo;

    @Test
    public void chatMessagesChouldBeOrdered() {
        Chat chat = chatRepo.save(Chat.of(null, "chat", "customer", null));

        messageRepo.save(Arrays.asList(
                Message.of(null, "1", "customer", null, chat),
                Message.of(null, "2", "customer", null, chat),
                Message.of(null, "3", "customer", null, chat)));

        List<Message> result = messageRepo.findByChatIdOrderById(chat.getId());

        assertEquals("1", result.get(0).getMessage());
        assertEquals("2", result.get(1).getMessage());
        assertEquals("3", result.get(2).getMessage());
    }
}
