package chatting.model;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class MessageTest {
    @Test
    public void json_변환() {
        Message message = Message.builder()
                .messageType(Message.MessageType.CLIENT)
                .user(new User())
                .message("테스트")
                .time(LocalDateTime.now())
                .build();

        String json = message.toJsonString();
        Message convertedMessage = Message.jsonToMessage(json);

        assertEquals(message, convertedMessage);
    }

}