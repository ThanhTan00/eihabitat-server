package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.ChatBotMessageReq;
import com.eihabitat.eihabitat_server.dto.request.MessageReq;
import com.eihabitat.eihabitat_server.dto.response.ChatBotMessageResponse;
import com.eihabitat.eihabitat_server.dto.response.ChatConversationResponse;
import com.eihabitat.eihabitat_server.dto.response.ChatRoomResponse;
import com.eihabitat.eihabitat_server.dto.response.MessageResponse;
import com.eihabitat.eihabitat_server.entity.*;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.MessageMapper;
import com.eihabitat.eihabitat_server.repository.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatService {
    ChatMessageRepository chatMessageRepository;
    ChatRoomRepository chatRoomRepository;
    MessageRepository messageRepository;
    UserRepository userRepository;
    ChatBotRepository chatBotRepository;

    MessageMapper messageMapper;
    SimpMessagingTemplate messagingTemplate;


//    public List<ChatConversationResponse> getConversations(String userId) {
//        List<ChatMessage> messages = chatMessageRepository.findConversationsByUserId(userId);
//
//        Set<String> participantIds = new HashSet<>();
//        for (ChatMessage message : messages) {
//            if (!message.getSenderId().equals(userId)) {
//                participantIds.add(message.getSenderId());
//            }
//            if (!message.getRecipientId().equals(userId)) {
//                participantIds.add(message.getRecipientId());
//            }
//        }
//
//        List<ChatConversationResponse> conversations = new ArrayList<>();
//        for (String participantId : participantIds) {
//            User user = userRepo.findById(participantId).orElseThrow(() ->
//                    new AppException(ErrorCode.USER_NOT_EXISTED));
//
//            List<ChatMessage> message = chatMessageRepository
//                    .findMessagesBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
//                            userId, participantId, Sort.by(Sort.Direction.DESC, "timestamp"));
//            ChatMessage lastMessage = messages.isEmpty() ? null : message.get(0);
//
//            ChatConversationResponse response = new ChatConversationResponse();
//            response.setId(user.getId());
//            response.setUserProfileName(user.getProfileName());
//            response.setUserAvatar(user.getProfileAvatar());
//            response.setLastMessage(lastMessage != null ? lastMessage.getContent() : null);
//
//            conversations.add(response);
//        }
//
//        return conversations;
//    }
//
//    public List<ChatMessage> getChatHistory(String senderId, String recipientId) {
//        return chatMessageRepository.findChatHistory(senderId, recipientId)
//                .stream()
//                .sorted(Comparator.comparing(ChatMessage::getTimestamp))
//                .collect(Collectors.toList());
//    }
//
//    public ChatMessage sendMessage(ChatMessage message) {
//        message.setTimestamp(LocalDateTime.now());
//        ChatMessage savedMessage = chatMessageRepository.save(message);
//        messagingTemplate.convertAndSend("/topic/messages", savedMessage);
//        return savedMessage;
//    }
//
//    public List<ChatMessage> getAllMessages() {
//        return chatMessageRepository.findAll();
//    }
//
//    public List<ChatMessage> getMessagesBetweenUsers(String senderId, String recipientId) {
//        return chatMessageRepository
//                .findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByTimestampDesc(
//                        senderId, recipientId, senderId, recipientId);
//    }
//
//    public void clearAllMessages() {
//        chatMessageRepository.deleteAll();
//    }
    public List<ChatRoomResponse> getAllChatRooms(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderIdOrRecipientId(userId, userId);
        List<ChatRoomResponse> chatRoomResponses = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            User friend;
            if (chatRoom.getSenderId().equals(userId)) {
                friend = userRepository.findById(chatRoom.getRecipientId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            } else {
                friend = userRepository.findById(chatRoom.getSenderId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            }

            Message latestMess = messageRepository.findFirstByChatRoomIdOrderByTimestampDesc(chatRoom.getId());

            ChatRoomResponse chatRoomResponse = ChatRoomResponse.builder()
                    .id(chatRoom.getId())
                    .userId(friend.getId())
                    .userName(friend.getFirstName()+" "+friend.getLastName())
                    .userAvatar(friend.getProfileAvatar())
                    .lastMessage(latestMess.getContent())
                    .lastSender(latestMess.getSenderId())
                    .seen(latestMess.isSeen())
                    .timestamp(latestMess.getTimestamp())
                    .build();
            chatRoomResponses.add(chatRoomResponse);
        }
        return chatRoomResponses;
    }

    public MessageResponse sendMessage(MessageReq messageReq) {
        String roomId;
        if (chatRoomRepository.existsBySenderIdAndRecipientId(messageReq.getSenderId(), messageReq.getRecipientId())) {
            roomId = chatRoomRepository.findBySenderIdAndRecipientId(messageReq.getSenderId(), messageReq.getRecipientId()).getId();
        } else if (chatRoomRepository.existsBySenderIdAndRecipientId(messageReq.getRecipientId(), messageReq.getSenderId())) {
            roomId = chatRoomRepository.findBySenderIdAndRecipientId(messageReq.getRecipientId(), messageReq.getSenderId()).getId();
        } else {
            ChatRoom chatRoom = ChatRoom.builder()
                    .senderId(messageReq.getSenderId())
                    .recipientId(messageReq.getRecipientId())
                    .build();
            roomId = chatRoomRepository.save(chatRoom).getId();
        }
        Message message = Message.builder()
                .chatRoomId(roomId)
                .content(messageReq.getContent())
                .senderId(messageReq.getSenderId())
                .timestamp(LocalDateTime.now())
                .seen(false)
                .build();
        MessageResponse messageResponse = messageMapper.toMessageResponse(messageRepository.save(message));
        User user = userRepository.findById(messageReq.getSenderId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        messageResponse.setSenderAvatar(user.getProfileAvatar());
        messageResponse.setSenderUrl(user.getUserUrl());

        messagingTemplate.convertAndSendToUser(roomId, "/chatRoom", messageResponse);

        return messageResponse;
    }

    public List<MessageResponse> getMessageHistory(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        List<Message> messages = messageRepository.findTop20ByChatRoomIdOrderByTimestampDesc(roomId);
        List<MessageResponse> messageResponses = new ArrayList<>();

        for (Message message : messages) {
            MessageResponse messageResponse = messageMapper.toMessageResponse(message);
            User sender = userRepository.findById(message.getSenderId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            messageResponse.setSenderAvatar(sender.getProfileAvatar());
            messageResponse.setSenderUrl(sender.getUserUrl());
            messageResponses.add(messageResponse);
        }
        return messageResponses;
    }

    public ChatBot chatWithBot(String userId, ChatBotMessageReq chatBotMessageReq){
        LocalDateTime sendAt = LocalDateTime.now();
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://14.225.253.213:5000/chat";
        //String url = "http://localhost:5000/chat";
        ChatBotMessageResponse response = restTemplate.postForObject(url, chatBotMessageReq, ChatBotMessageResponse.class);
        log.info(response.toString());
        ChatBot chatBot = ChatBot.builder()
                .userId(userId)
                .message(chatBotMessageReq.getMessage())
                .response(response.getResponse())
                .sendAt(sendAt)
                .receiveAt(LocalDateTime.now())
                .build();
        return chatBotRepository.save(chatBot);
    }

    public List<ChatBot> getChatBotHistory(String userId) {
        return chatBotRepository.findAllByUserId(Sort.by(Sort.Direction.ASC, "sendAt"), userId);
    }
}
