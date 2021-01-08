package com.epicBot.main.messageProcessing.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Help implements Command {

    @Override
    public void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event) {
        channel.sendMessage("(Command Not Finished)\n" +
                "List of commands (capitalization irrelevant):\n" +
                "-help\n" +
                "-ping\n" +
                "-echo\n" +
                "-print (debugging only)\n" +
                "-timeout\n" +
                "-poll\n" +
                "-rps\n").queue();
    }
}
