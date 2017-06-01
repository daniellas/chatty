package dl.chatty.chat.stream;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import dl.chatty.chat.mapping.ChatMapper;
import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.chat.view.ChatView;
import dl.chatty.security.Roles;
import dl.chatty.security.UsernameEnforcer;
import dl.chatty.security.UsernameSupplier;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class DefaultChatStreams implements ChatStreams {

    private final ChatRepository chatRepo;

    private final UsernameSupplier usernameSupplier;

    private final UsernameEnforcer usernameEnforcer;

    @Override
    public Observable<Collection<ChatView>> findAll() {
        return Observable
                .fromCallable(() -> {
                    return chatRepo.findByCreatedBy(usernameEnforcer.apply(null));
                })
                .map(ChatMapper::toViewList);
    }

    @Secured(Roles.CUSTOMER)
    @Override
    public Observable<ChatView> create(ChatView chat) {
        String currentUser = usernameSupplier.getOrThrowNoCredentials();

        return Observable.just(chat)
                .map(ChatMapper::toEntity)
                .map(t -> {
                    t.setCreatedBy(currentUser);

                    return chatRepo.save(t);
                })
                .map(ChatMapper::toView);
    }

    @Override
    public Observable<ChatView> getOne(Long id) {
        return Observable.just(id)
                .map(i -> Optional.ofNullable(chatRepo.findOne(i)))
                .map(Optional::get)
                .map(ChatMapper::toView);
    }

}
