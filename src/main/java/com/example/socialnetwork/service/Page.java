package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.dto.FriendDTO;
import com.example.socialnetwork.dto.MessageDTO;
import com.example.socialnetwork.dto.RequestDTO;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;

import javax.security.auth.login.CredentialException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Page {
    private UserService serviceUser;
    private FriendshipService serviceFriendship;
    private FriendRequestService serviceFriendRequest;
    private MessageService serviceMessage;
    private ChatService chatService;
    private EvenimentService evenimentService;
    private Long idUser;


    public Page(UserService serviceUser, FriendshipService serviceFriendship, FriendRequestService serviceFriendRequest, MessageService serviceMessage, ChatService chatService, EvenimentService evenimentService){
        this.serviceUser = serviceUser;
        this.serviceFriendship = serviceFriendship;
        this.serviceFriendRequest = serviceFriendRequest;
        this.serviceMessage = serviceMessage;
        this.chatService = chatService;
        this.evenimentService = evenimentService;
    }

    public ChatService getChatService() {
        return chatService;
    }

    public EvenimentService getEvenimentService() {
        return evenimentService;
    }

    public MessageService getServiceMessage() {
        return serviceMessage;
    }

    public void setIdUser(Long idUser) throws CredentialException {
        try {
            serviceUser.findOne(idUser);
        } catch (ServiceException e) {
            throw new CredentialException("User with the given id does not exist");
        }
        this.idUser = idUser;
    }

    public Long getIdUser(){
        return idUser;
    }

    public void acceptFriendRequest(Long idSender) throws ValidationException, ServiceException, RepositoryException {
        serviceFriendRequest.updateFriendRequest(idSender, this.idUser, "approved");
        User first = serviceUser.findOne(idSender);
        User second = serviceUser.findOne(idUser);
        serviceFriendship.save(first, second);
    }

    public List<Chat> getAllChats() {
        return chatService.findAll(idUser);
    }

    public void declineFriendRequest(Long idSender) throws ValidationException, ServiceException {
        serviceFriendRequest.updateFriendRequest(idSender, idUser, "rejected");
    }

    public void deleteSentFriendRequest(Long idReceiver) throws ServiceException {
        serviceFriendRequest.deleteFriendRequest(idUser, idReceiver);
    }

    public List<FriendDTO> getFriends() throws ServiceException {
        User user = serviceUser.findOne(idUser);
        return serviceFriendship.getFriendsDTO(user);
    }

    public List<Friend> getListOfFriends() throws ServiceException {
        User user = serviceUser.findOne(idUser);
        return serviceFriendship.getFriends(user);
    }

    public List<User> getAllNonFriends() {
        Predicate<FriendRequest> predicate = x -> x.getIdReceivingUser().equals(idUser);
        Predicate<FriendRequest> predicate1 = x -> x.getIdSendingUser().equals(idUser);
        List<User> userList = StreamSupport.stream(serviceUser.findAll().spliterator(), false).toList();
        List<User> requests = StreamSupport.stream(serviceFriendRequest.findAll().spliterator(), false).toList()
                .stream()
                .filter(predicate.or(predicate1)).
                map(friendRequest -> {
                    try {
                        if (friendRequest.getIdSendingUser().equals(idUser))
                            return serviceUser.findOne(friendRequest.getIdReceivingUser());
                        else if (friendRequest.getIdReceivingUser().equals(idUser))
                            return serviceUser.findOne(friendRequest.getIdSendingUser());
                    } catch (ServiceException ignored) {
                    }
                    return null;
                }).toList();
        Predicate<User> predicate2 = x -> !requests.contains(x);
        Predicate<User> predicate3 = x -> !x.getId().equals(idUser);
        return userList.stream()
                .filter(predicate2.and(predicate3)).toList();
    }

    public void sendFriendRequest(Long idReceiver) throws ValidationException, ServiceException, RepositoryException {
        serviceFriendRequest.sendFriendRequest(idUser, idReceiver);
        //notifyObservers(new UserChangedEvent(ChangeEventType.UPDATE, null));
    }

    public Iterable<User> getAllPendingForReceiver() {
        Iterable<FriendRequest> requests = serviceFriendRequest.findAll();
        return StreamSupport.stream(requests.spliterator(), false).
                filter(friendRequest -> friendRequest.getIdReceivingUser().equals(idUser)).
                filter(friendRequest -> friendRequest.getStatus().equals("pending")).
                map(friendRequest -> {
                    try {
                        return serviceUser.findOne(friendRequest.getIdSendingUser());
                    } catch (ServiceException ignored) {}
                    return null;
                }).
                collect(Collectors.toList());
    }

    public Iterable<RequestDTO> getAllPendingForReceiverDTO() {
        Iterable<FriendRequest> requests = serviceFriendRequest.findAll();
        return StreamSupport.stream(requests.spliterator(), false).
                filter(friendRequest -> friendRequest.getIdReceivingUser().equals(idUser)).
                filter(friendRequest -> friendRequest.getStatus().equals("pending")).
                map(friendRequest -> {
                    try {
                        User user = serviceUser.findOne(friendRequest.getIdSendingUser());
                        RequestDTO requestDTO = new RequestDTO(user.getFirstName(), user.getLastName(), friendRequest.getStatus(), friendRequest.getDate(), user.getProfilePicture());
                        requestDTO.setIdUser(user.getId());
                        return requestDTO;
                    } catch (ServiceException ignored) {}
                    return null;
                }).
                collect(Collectors.toList());
    }

    public Iterable<RequestDTO> getAllRejectedAndApprovedForReceiverDTO() {
        Iterable<FriendRequest> requests = serviceFriendRequest.findAll();
        return StreamSupport.stream(requests.spliterator(), false).
                filter(friendRequest -> friendRequest.getIdReceivingUser().equals(idUser)).
                filter(friendRequest -> {
                    if(friendRequest.getStatus().equals("approved") || friendRequest.getStatus().equals("rejected"))
                        return true;
                    return false;
                }).
                map(friendRequest -> {
                    try {
                        User user = serviceUser.findOne(friendRequest.getIdSendingUser());
                        return new RequestDTO(user.getFirstName(), user.getLastName(), friendRequest.getStatus(), friendRequest.getDate(), user.getProfilePicture());
                    } catch (ServiceException ignored) {}
                    return null;
                }).
                collect(Collectors.toList());
    }

    public boolean wantsToBeFriend(User user){
        List<User> friends  = StreamSupport.stream(getAllPendingForReceiver().spliterator(), false).toList();
        if(friends.contains(user))
            return true;
        return false;
    }

    public Iterable<User> getAllPendingForSender(){
        Iterable<FriendRequest> requests = serviceFriendRequest.findAll();
        return StreamSupport.stream(requests.spliterator(), false).
                filter(friendRequest -> friendRequest.getIdSendingUser().equals(idUser)).
                filter(friendRequest -> friendRequest.getStatus().equals("pending")).
                map(friendRequest -> {
                    try {
                        return serviceUser.findOne(friendRequest.getIdReceivingUser());
                    } catch (ServiceException ignored) {}
                    return null;
                }).
                collect(Collectors.toList());
    }

    public void deleteFriendship(Long idFriend) throws ServiceException {
        serviceFriendship.delete(idUser, idFriend);
        try {
            serviceFriendRequest.deleteFriendRequest(idUser, idFriend);
        }catch (ServiceException ignored){}
        try {
            serviceFriendRequest.deleteFriendRequest(idFriend, idUser);
        }catch (ServiceException ignored){}
    }

    public void updateInfos(String firstName, String lastName, String gender, LocalDate birthdate) throws ValidationException, ServiceException, RepositoryException {
        serviceUser.update(idUser, firstName, lastName, gender, birthdate);
    }

    public User getUser() throws ServiceException {
        return serviceUser.findOne(idUser);
    }

    public Iterable<User> getUsers(){
        return serviceUser.findAll();
    }

    public void sendMessage(Long idGroup, String text, Long reply) throws ValidationException, RepositoryException {
        serviceMessage.send(idUser, idGroup, text, reply);
    }

    public void replyMessage(Long idMessage, String text) throws ValidationException, ServiceException, RepositoryException {
        serviceMessage.reply(idMessage, idUser, text);
    }

    public List<Message> getChatMessages(Long chatId) {
        return serviceMessage.getGroupChat(chatId);
    }

    public List<MessageDTO> getChatMessagesFromAPeriod(LocalDateTime startDate, LocalDateTime endDate, Long chatId){
        Iterable<Message> messages = serviceMessage.findAll();
        return StreamSupport.stream(messages.spliterator(), false)
                .filter(message -> message.getTime().isAfter(startDate) && message.getTime().isBefore(endDate) && message.getChatId().equals(chatId))
                .map(message -> {
                    try {
                        Long id = message.getId();
                        User user = serviceUser.findOne(message.getUserSenderId());
                        String name = user.getFirstName() + " " + user.getLastName();
                        LocalDateTime time = message.getTime();
                        String text = message.getText();
                        Long replyId = message.getReply();
                        String replyAt = null;
                        if(replyId != null)
                            replyAt = serviceMessage.findOne(message.getReply()).getText();
                        return new MessageDTO(id, name, time, text, replyAt);
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());
    }


    public boolean isAChatMember(Long id){
        Chat chat = chatService.findOne(id);
        return chat.getMembers().contains(idUser);
    }

    public List<MessageDTO> getRaportAllMessages(LocalDateTime begin, LocalDateTime end){
        Iterable<Message> messages = serviceMessage.findAll();
        return StreamSupport.stream(messages.spliterator(), false)
                .filter(message -> message.getTime().isAfter(begin) && message.getTime().isBefore(end) && isAChatMember(message.getChatId()) && !message.getUserSenderId().equals(idUser))
                .map(message -> {
                    try {
                        Long id = message.getId();
                        User user = serviceUser.findOne(message.getUserSenderId());
                        String name = user.getFirstName() + " " + user.getLastName();
                        LocalDateTime time = message.getTime();
                        String text = message.getText();
                        Long replyId = message.getReply();
                        String replyAt = null;
                        if(replyId != null)
                            replyAt = serviceMessage.findOne(message.getReply()).getText();

                        return new MessageDTO(id, name, time, text, replyAt);
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());

    }

    public List<FriendDTO> getRaportAllNewFriends(LocalDateTime begin, LocalDateTime end) throws ServiceException {
       return getFriends().stream().filter(friendship -> friendship.getDate().isAfter(begin.toLocalDate()) && friendship.getDate().isBefore(end.toLocalDate())).collect(Collectors.toList());
    }

    public Chat saveChat(Chat chat) {
        return chatService.save(chat);
    }

    public Chat deleteChat(Chat chat) {
        return chatService.delete(chat);
    }

    public void setProfilePicture(String path) throws ServiceException, ValidationException {
        User user = serviceUser.findOne(idUser);
        serviceUser.updateProfilePicture(user, path);
    }

    public FriendshipService getServiceFriendship(){
        return serviceFriendship;
    }

    public FriendRequestService getServiceFriendRequest(){
        return serviceFriendRequest;
    }

    public UserService getServiceUser(){
        return serviceUser;
    }

    public List<User> searchByName(String name){
        name = name.toLowerCase();
        Iterable<User> users = getUsers();
        String finalName = name;
        List<User> usersWithName = StreamSupport.stream(users.spliterator(), false).
                filter(user -> user.getFirstName().toLowerCase().startsWith(finalName) ||
                        user.getLastName().toLowerCase().startsWith(finalName)).collect(Collectors.toList());

        return usersWithName;
    }

    public boolean areFriends(Long idFriend){
        if(serviceFriendship.findOne(idUser, idFriend) != null)
            return true;
        return false;
    }

    public boolean requestExists(Long idReceiver){
        if(serviceFriendRequest.findOne(idUser, idReceiver) != null)
            return true;
        return false;
    }

    public boolean reverseRequestExists(Long idSender){
        if(serviceFriendRequest.findOne(idSender, idUser) != null)
            return true;
        return false;
    }

    public List<Eveniment> getEvents() {
        return evenimentService.findAll();
    }

    public void saveEvent(Eveniment eveniment) {
        evenimentService.save(eveniment, getIdUser());
    }

    public void sendNotifications() {
        List<EvenimentNotification> evenimentNotificationList = evenimentService.findAllNotifications(idUser);
        for(EvenimentNotification evenimentNotification : evenimentNotificationList) {
            Eveniment eveniment = evenimentService.findOneEveniment(evenimentNotification.getEveniment_id());
            long daysBetween = Period.between(LocalDate.now(), eveniment.getDate()).getDays();
            System.out.println(evenimentNotification.getNotification() + " -- " + evenimentNotification.getStatus() + " -- " + daysBetween);
            if(daysBetween == 1 && evenimentNotification.getNotification().equals("on") && evenimentNotification.getStatus().equals("notsent")) {
                evenimentNotification.setStatus("sent");
                evenimentService.updateEventNotification(evenimentNotification);
            }
        }
    }

    public void readNotifications() {
        List<EvenimentNotification> evenimentNotificationList = evenimentService.findAllNotifications(idUser);
        for(EvenimentNotification evenimentNotification : evenimentNotificationList) {
            Eveniment eveniment = evenimentService.findOneEveniment(evenimentNotification.getEveniment_id());
            if(evenimentNotification.getNotification().equals("on") && evenimentNotification.getStatus().equals("sent")) {
                evenimentNotification.setStatus("seen");
                evenimentService.updateEventNotification(evenimentNotification);
            }
        }
    }

    public List<EvenimentNotification> populateNotificationList() {
        return evenimentService.findAllNotifications(idUser)
                .stream()
                .filter(x -> x.getStatus().equals("sent"))
                .toList();
    }

    public Eveniment findOneEveniment(Long id) {
        return evenimentService.findOneEveniment(id);
    }

    public List<EvenimentNotification> getAllNotifications() {
        return evenimentService.findAllNotifications(idUser);
    }

    public void subscribeToEvent(EvenimentNotification evenimentNotification) {
        evenimentService.saveEventNotification(evenimentNotification);
    }

    public void unsubscribeFromEvent(EvenimentNotification evenimentNotification) {
        evenimentService.deleteEventNotification(evenimentNotification);
    }

    public void updateEventNotification(EvenimentNotification evenimentNotification) {
        evenimentService.updateEventNotification(evenimentNotification);
    }

    public EvenimentNotification findOneSubscribe(EvenimentNotification evenimentNotification) {
        return evenimentService.findOneEvenimentNotification(evenimentNotification);
    }

    public void deletePastEvents() {
        evenimentService.deletePastEvents();
    }
}
