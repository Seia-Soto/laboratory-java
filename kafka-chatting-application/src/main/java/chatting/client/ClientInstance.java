package chatting.client;

import chatting.client.network.Client;
import chatting.model.ChatRoomInfo;
import chatting.model.Message;
import chatting.model.User;

import java.util.*;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;

public enum ClientInstance {
    INSTANCE;

    private User user;
    private Client client;
    private final SubmissionPublisher<Message> messagePublisher = new SubmissionPublisher<> ();
    private final Set<ChatRoomInfo> chatRoomInfos = new HashSet<>();
    private final Set<Integer> joinChatRoomNos = new HashSet<>();
    private final List<Message> receivedMessages = new ArrayList<>();

    public static ClientInstance getInstance() {
        return INSTANCE;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<ChatRoomInfo> getChatRoomInfos() {
        return chatRoomInfos.stream()
                .sorted(Comparator.comparingInt(ChatRoomInfo::getNo))
                .collect(Collectors.toList());
    }

    public void addChatRoomInfos(Set<ChatRoomInfo> chatRoomInfos) {
        this.chatRoomInfos.addAll(chatRoomInfos);
    }

    public void changeChatRoomInfo(ChatRoomInfo chatRoomInfo) {
        chatRoomInfos.removeIf(c -> c.getNo() == chatRoomInfo.getNo());
        chatRoomInfos.add(chatRoomInfo);
    }

    public void addChatRoomNo(int chatRoomNo) {
        joinChatRoomNos.add(chatRoomNo);
        System.out.println("Current user(" + user + ") room list joined => "  + this.joinChatRoomNos);
    }

    public void removeChatRoomNo(int chatRoomNo) {
        joinChatRoomNos.remove(chatRoomNo);
        receivedMessages.removeAll(
                receivedMessages.stream()
                        .filter(message -> message.getChatRoomNo() == chatRoomNo)
                        .collect(Collectors.toList())
        );
        System.out.println("Current user(" + user + ") room list joined => "  + this.joinChatRoomNos);
    }

    public boolean isJoinedChatRoomNo(int chatRoomNo) {
        return joinChatRoomNos.contains(chatRoomNo);
    }

    public void addMessage(Message message) {
        receivedMessages.add(message);
    }

    public void publishMessage(Message message) {
        System.out.println("Publishing > " + message);
        messagePublisher.submit(message);
    }

    public List<Message> getMessagesInChatRoomNo(int chatRoomNo) {
        return receivedMessages.stream()
                .filter(message -> message.getChatRoomNo() == chatRoomNo)
                .collect(Collectors.toList());
    }

    public void subscribe(Subscriber<Message> subscriber) {
        messagePublisher.subscribe(subscriber);
    }

    public void send(Message message) {
        client.send(message);
    }
}
