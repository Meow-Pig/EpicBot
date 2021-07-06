package com.epicBot.main.messageProcessing.commands.Audio;

import com.epicBot.main.messageProcessing.commands.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Disconnect implements Command {

    @Override
    public void processCommand(Message message, MessageChannel mChannel, MessageReceivedEvent event) {

        String content = message.getContentRaw();

        ArrayList<String[]> args = Command.parseCommand(content.substring(content.indexOf(" ")));

        AtomicReference<String> text = new AtomicReference<>("");

        args.forEach(arg -> {
            switch (arg[0]) {
                case "m":
                case "message":
                    text.set(arg[1]);
            }
        });
        try {
            CustomVocalCord.say(text.get(), event.getMember().getVoiceState().getChannel());
        } catch (NullPointerException e){
            mChannel.sendMessage("Please connect to a voice channel").queue();
        }
    }
}
