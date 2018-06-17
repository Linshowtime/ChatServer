package com.nt.controller;

import java.util.List;

import javax.annotation.Resource;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.nt.entity.ChatMessage;
import com.nt.entity.Group;
import com.nt.entity.GroupMessage;
import com.nt.entity.GroupUser;
import com.nt.entity.User;
import com.nt.net.WiselyMessage;
import com.nt.net.WiselyResponse;
import com.nt.service.IChatMessageService;
import com.nt.service.IGroupMessageService;
import com.nt.service.IGroupService;
import com.nt.service.IGroupUserService;
import com.nt.service.IUserService;
import com.nt.service.WebSocketService;
import com.nt.util.Constant;

@Controller
public class WsController {
    @Resource
    WebSocketService webSocketService;
    @Autowired
	private IUserService userService;
    @Autowired
    private IChatMessageService chatMessageService;
    @Autowired
    private IGroupMessageService groupMessageService;
    @Autowired
    private IGroupService groupService;
    @Autowired
	private IGroupUserService groupUserService;
    @MessageMapping(Constant.FORETOSERVERPATH)//@MessageMapping和@RequestMapping功能类似，用于设置URL映射地址，浏览器向服务器发起请求，需要通过该地址。
    @SendTo(Constant.PRODUCERPATH)//如果服务器接受到了消息，就会对订阅了@SendTo括号中的地址传送消息。
    public WiselyResponse sendMessage(WiselyMessage message) throws Exception {
        List<String> users = Lists.newArrayList();
        if(message.getType()==3){
        User sender=userService.findByName(message.getSenderName());
        User receiver=userService.findByName(message.getReceiverName());
        ChatMessage m=new ChatMessage();
        m.setReceiverId(receiver.getId());
        m.setSenderId(sender.getId());
        m.setContent(message.getContent());
        chatMessageService.saveMessage(m);
        users.add(message.getReceiverName());
        webSocketService.send2Users(users,message);
        }
        if(message.getType()==0||message.getType()==1){
            User sender=userService.findByName(message.getSenderName());
            Group group=groupService.findGroupByName(message.getReceiverName());
            GroupMessage m=new GroupMessage();
            m.setGroupId(group.getId());
            m.setSenderId(sender.getId());
            m.setContent(message.getContent());
            m.setType(message.getType());
            groupMessageService.savaGroupMessage(m);
           List<GroupUser> list=groupUserService.findGroupUserByGroupId(group.getId());
           for(GroupUser user:list){
           User u=userService.findById(user.getUserId());
            users.add(u.getUsername());
           }
            webSocketService.send2Users(users,message);
            }
        return new WiselyResponse("消息已收到");
    }
}