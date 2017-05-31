package dl.chatty.chat.stream;

import java.util.Collection;
import java.util.Optional;

import dl.chatty.chat.mapping.ChatMapper;
import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.chat.view.ChatView;
import dl.chatty.security.CurrentUsernameSupplier;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultChatStreams implements ChatStreams {

    private final ChatRepository chatRepo;

    private final CurrentUsernameSupplier usernameSupplier;

    @Override
    public Observable<Collection<ChatView>> findAll() {
        return Observable.fromCallable(() -> {
            return chatRepo.findAll(null);
        }).map(ChatMapper::toViewList);
    }

    @Override
    public Observable<ChatView> create(ChatView chat) {
        return Observable.just(chat)
                .map(ChatMapper::toEntity)
                .map(t -> {
                    return chatRepo.create(t, usernameSupplier.justGet());
                })
                .map(Optional::get)
                .map(ChatMapper::toView);
    }

    @Override
    public Observable<ChatView> getOne(String id) {
        return Observable.just(id)
                .map(chatRepo::getOne)
                .map(Optional::get)
                .map(ChatMapper::toView);
    }

}
