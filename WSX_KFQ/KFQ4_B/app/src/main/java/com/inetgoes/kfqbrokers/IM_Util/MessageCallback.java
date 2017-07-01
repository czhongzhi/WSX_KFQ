package com.inetgoes.kfqbrokers.IM_Util;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by czz on 2015/11/24.
 */
public interface MessageCallback {
    public void dealMessage(Chat chat, Message message);
}
