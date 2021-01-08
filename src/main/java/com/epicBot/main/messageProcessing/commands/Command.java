package com.epicBot.main.messageProcessing.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {

    //Main method, will do most of the processing
    void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event);

}
