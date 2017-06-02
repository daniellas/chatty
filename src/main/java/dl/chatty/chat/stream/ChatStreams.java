package dl.chatty.chat.stream;

import java.util.Collection;

import dl.chatty.chat.view.ChatView;
import io.reactivex.Observable;

/**
 * Chat Reactive streams provider
 * 
 * @author Daniel Łaś
 *
 */
public interface ChatStreams {

    /**
     * Find all chats
     * 
     * @return {@link ChatView} collection {@link Observable}
     */
    Observable<Collection<ChatView>> findAll();

    /**
     * Creates new chat
     * 
     * @param chat
     *            to create
     * @return {@link ChatView} {@link Observable}
     */
    Observable<ChatView> create(ChatView chat);

    /**
     * Get one chat
     * 
     * @param id
     *            of chat
     * @return {@link ChatView} {@link Observable}
     */
    Observable<ChatView> getOne(Long id);

}
