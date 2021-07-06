package com.epicBot.main.messageProcessing.commands;

import com.epicBot.main.Main;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class Status implements Command {

    @Override
    public void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event) {

        String content = message.getContentRaw();

        ArrayList<String[]> args = Command.parseCommand(content.substring(content.indexOf(" ")));

        var ref = new Object() {
            Activity activity;
        };

        args.forEach(arg -> {
            if (arg.length != 2){
                channel.sendMessage("Invalid formatting of arg -"+arg[0]).queue();
                return;
            }
            switch (arg[0]){
                case "w":
                case "watching":
                case "watch":
                    ref.activity = Activity.watching(arg[1]);
                    break;
                case "l":
                case "listen":
                case "listening":
                    ref.activity = Activity.listening(arg[1]);
                    break;
                case "p":
                case "play":
                case "playing":
                    ref.activity = Activity.playing(arg[1]);
                    break;
                default:
                    channel.sendMessage("Unknown arg: -"+arg[0]).queue();
            }
        });
        Main.jda.getPresence().setActivity(ref.activity);
        channel.sendMessage("Successfully set activity").queue();
    }

}
