package dl.chatty.chat.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import dl.chatty.chat.stream.ChatStreams;
import dl.chatty.chat.view.ChatView;
import dl.chatty.stream.DeferredResultSubscriber;

@RestController
@RequestMapping(value = "/chats")
public class ChatController {

    @Autowired
    private ChatStreams chatStreams;

    @RequestMapping(method = RequestMethod.GET)
    public DeferredResult<Collection<ChatView>> findAl() {
        return DeferredResultSubscriber.subscribe(chatStreams.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DeferredResult<ChatView> get(@PathVariable String id) {
        return DeferredResultSubscriber.subscribe(chatStreams.getOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public DeferredResult<ChatView> create(@RequestBody ChatView chat) {
        return DeferredResultSubscriber.subscribe(chatStreams.create(chat));
    }
}
