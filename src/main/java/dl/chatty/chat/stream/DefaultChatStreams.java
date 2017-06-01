package dl.chatty.chat.stream;

import java.util.Collection;
import java.util.Optional;

import org.springframework.security.access.annotation.Secured;

import dl.chatty.chat.mapping.ChatMapper;
import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.chat.view.ChatView;
import dl.chatty.concurrency.ExecutorsProvider;
import dl.chatty.security.CurrentUsernameSupplier;
import dl.chatty.security.Roles;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultChatStreams implements ChatStreams {

    private final ChatRepository chatRepo;

    private final CurrentUsernameSupplier usernameSupplier;

    private final ExecutorsProvider executorsProvider;

    @Override
    public Observable<Collection<ChatView>> findAll() {
        return Observable
                .fromCallable(() -> {
                    return chatRepo.findAll(null);
                })
                .observeOn(fileIOScheduler())
                .map(ChatMapper::toViewList);
    }

    @Secured(Roles.CUSTOMER)
    @Override
    public Observable<ChatView> create(ChatView chat) {
        String currentUser = usernameSupplier.getOrThrowNoCredentials();

        return Observable.just(chat)
                .observeOn(fileIOScheduler())
                .map(ChatMapper::toEntity)
                .map(t -> {
                    return chatRepo.create(t, currentUser);
                })
                .map(Optional::get)
                .map(ChatMapper::toView);
    }

    @Override
    public Observable<ChatView> getOne(String id) {
        return Observable.just(id)
                .observeOn(fileIOScheduler())
                .map(chatRepo::getOne)
                .map(Optional::get)
                .map(ChatMapper::toView);
    }

    private Scheduler fileIOScheduler() {
        return Schedulers.from(executorsProvider.fileIOExecutor());
    }
}
