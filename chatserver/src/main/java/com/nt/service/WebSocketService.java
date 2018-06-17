package com.nt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.nt.net.WiselyMessage;
import com.nt.util.Constant;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 广播
     * 发给所有在线用户
     *
     * @param msg
     */
    public void sendMsg(WiselyMessage msg) {
        template.convertAndSend(Constant.PRODUCERPATH, msg);
    }

    /**
     * 发送给指定用户
     * @param users
     * @param msg
     */
    public void send2Users(List<String> users, WiselyMessage msg) {
        users.forEach(userName -> {
            template.convertAndSendToUser(userName, Constant.P2PPUSHPATH, msg);
        });
    }
}