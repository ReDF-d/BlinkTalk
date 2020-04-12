package com.redf.chatwebapp.controller;


import com.redf.chatwebapp.controller.interfaces.viewBeautify.RoomBeautify;
import com.redf.chatwebapp.controller.interfaces.viewBeautify.RoomSanitizer;
import com.redf.chatwebapp.dao.FriendshipDAOImpl;
import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.FriendshipEntityRepository;
import com.redf.chatwebapp.dao.repo.MessageEntityRepository;
import com.redf.chatwebapp.dao.repo.RoomEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.UserDetails;
import com.redf.chatwebapp.dto.ChatCreateDto;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/chats")
@Transactional
public class ChatsController implements RoomSanitizer {

    private RoomEntityRepository roomEntityRepository;
    private UserService userService;
    private ArrayList<RoomEntity> rooms;
    private MessageEntityRepository messageEntityRepository;
    private FriendshipDAOImpl friendshipDAO;
    private FriendshipEntityRepository friendshipEntityRepository;


    @Autowired
    public ChatsController(RoomEntityRepository roomEntityRepository, UserService userService, MessageEntityRepository messageEntityRepository, FriendshipDAOImpl friendshipDAO, FriendshipEntityRepository friendshipEntityRepository) {
        setRoomEntityRepository(roomEntityRepository);
        setUserService(userService);
        setMessageEntityRepository(messageEntityRepository);
        setFriendshipDAO(friendshipDAO);
        setFriendshipEntityRepository(friendshipEntityRepository);
    }


    ChatsController() {
    }


    @ModelAttribute("createDto")
    public ChatCreateDto getChatDto() {
        return new ChatCreateDto();
    }


    @GetMapping
    public ModelAndView getChatsPage() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity principal = getUserService().findById(userDetails.getId());
        List<RoomBeautify> roomsBeautify = new ArrayList<>();
        setRooms((ArrayList<RoomEntity>) getRoomEntityRepository().findRoomsByMemberId(principal.getId()));
        addRooms(getRooms(), roomsBeautify, principal.getId(), getMessageEntityRepository());
        ModelAndView modelAndView = new ModelAndView("chats");
        modelAndView.addObject("friends", getFriendshipDAO().getUserFriends(principal.getId(), getFriendshipEntityRepository().getUserFriends(principal.getId())));
        modelAndView.addObject("chats", roomsBeautify);
        return modelAndView;
    }


    @Contract(pure = true)
    private RoomEntityRepository getRoomEntityRepository() {
        return roomEntityRepository;
    }


    private void setRoomEntityRepository(RoomEntityRepository roomEntityRepository) {
        this.roomEntityRepository = roomEntityRepository;
    }


    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }


    private void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Contract(pure = true)
    private ArrayList<RoomEntity> getRooms() {
        return rooms;
    }


    private void setRooms(ArrayList<RoomEntity> rooms) {
        this.rooms = rooms;
    }

    @Contract(pure = true)
    private MessageEntityRepository getMessageEntityRepository() {
        return messageEntityRepository;
    }


    private void setMessageEntityRepository(MessageEntityRepository messageEntityRepository) {
        this.messageEntityRepository = messageEntityRepository;
    }


    @Contract(pure = true)
    private FriendshipDAOImpl getFriendshipDAO() {
        return friendshipDAO;
    }


    private void setFriendshipDAO(FriendshipDAOImpl friendshipDAO) {
        this.friendshipDAO = friendshipDAO;
    }


    @Contract(pure = true)
    private FriendshipEntityRepository getFriendshipEntityRepository() {
        return friendshipEntityRepository;
    }


    private void setFriendshipEntityRepository(FriendshipEntityRepository friendshipEntityRepository) {
        this.friendshipEntityRepository = friendshipEntityRepository;
    }
}
