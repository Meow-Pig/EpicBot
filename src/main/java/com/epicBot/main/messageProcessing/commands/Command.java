package com.epicBot.main.messageProcessing.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public interface Command {

    //Main method, will do most of the processing
    void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event);

    public static ArrayList<String[]> parseCommand(String sArgs){
        ArrayList<String[]> args = new ArrayList<>();
        new ArrayList<>(Arrays.asList(sArgs.split(" -"))).subList(1,sArgs.split(" -").length).forEach(arg ->{
            int i = arg.indexOf("=");
            if (i>0) {
                args.add(new String[]{arg.substring(0, i).toLowerCase(), arg.substring(i + 1)});
            } else {
                args.add(new String[]{arg.toLowerCase()});
            }
        });
        return args;
    }

}
