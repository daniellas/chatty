package dl.chatty.chat.stream;

import java.util.Collection;

import dl.chatty.chat.view.ChatView;
import io.reactivex.Observable;

public interface ChatStreams {
    Observable<Collection<ChatView>> findAll();

    Observable<ChatView> create(ChatView chat);
    
    Observable<ChatView> getOne(Long id);

}
