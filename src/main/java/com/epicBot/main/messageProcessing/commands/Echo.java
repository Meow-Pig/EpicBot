package com.epicBot.main.messageProcessing.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Echo implements Command {
    @Override
    public void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event) {

        String content = message.getContentRaw();
        String command = content.substring(1, content.indexOf(" ")).toLowerCase();

        channel.sendMessage(content.substring(content.indexOf(" ")+1)).queue();
        if (command.length() > 4) {
            for (int i = 0; i < Integer.parseInt(command.substring(4))-1; i++){
                channel.sendMessage(content.substring(content.indexOf(" ")+1)).queue();
            }
        }
        message.delete().queue();
    }
}
