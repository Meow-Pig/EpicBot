package com.epicBot.main.messageProcessing.commands;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class Shutdown implements Command {

    private static final ArrayList<Long> authorizedUsers = new ArrayList<>();

    static {
        authorizedUsers.add(582686402592899093L);
        authorizedUsers.add(528666415377678377L);
    }

    @Override
    public void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event) {
        if (authorizedUsers.contains(event.getAuthor().getIdLong())){
            channel.sendMessage("Shutting down").queue();
            event.getJDA().getPresence().setStatus(OnlineStatus.OFFLINE);
            event.getJDA().shutdown();
        } else {
            final String[] authList = {"```"};
            authorizedUsers.forEach(id -> {
                authList[0] += event.getJDA().retrieveUserById(id).complete().getName() + "\n";
            });
            channel.sendMessage("You are not authorized to use this command. Authorized users are: " + authList[0]).queue();
        }
    }
}
