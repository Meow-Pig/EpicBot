package com.epicBot.main.messageProcessing.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Poll implements Command{

    //Example !poll yes:🟢,no:🔴,I hate him:😠,Should-I-make-Him-Admin?

    @Override
    public void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event) {


        String content = message.getContentDisplay();

        ArrayList<String> args = new ArrayList<>(Arrays.asList(content.substring(6).split(",")));

        EmbedBuilder eb = new EmbedBuilder()
                .setColor(new Color(242193135))
                .setAuthor(event.getJDA().getSelfUser().getName() + "'s Epic Poll Function", null, event.getJDA().getSelfUser().getAvatarUrl());

        args.forEach(arg -> {
            if (arg.contains(":")) {
                String[] a = arg.split(":");
                eb.addField(a[1], "**"+a[0]+":0**", true);
            } else {
                eb.setDescription("**" + arg.replace("-", " ") + "**");
            }
        });

        channel.sendMessage(eb.build()).queue(m -> {
            m.getEmbeds().get(0).getFields().forEach(f ->{
                m.addReaction(f.getName()).queue();
            });
        });
    }
}
