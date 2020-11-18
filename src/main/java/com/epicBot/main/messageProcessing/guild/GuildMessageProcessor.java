package com.epicBot.main.messageProcessing.guild;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.ChannelManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.InviteAction;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;
import net.dv8tion.jda.internal.handle.GuildSetupController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GuildMessageProcessor {

    String key;
    public GuildMessageProcessor(String Key){ key = Key; }


    public void process(MessageReceivedEvent event){

        MessageChannel channel = event.getChannel();
        String content = event.getMessage().getContentStripped();

        if (event.getMessage().getContentStripped().startsWith(key))
            processCommand(event,channel,content);
        else
            processNotCommand(event,channel,content);
    }

    private void processCommand(MessageReceivedEvent event, MessageChannel channel, String content) {

        String command;

        if (content.contains(" "))
            command = content.substring(1, content.indexOf(" ")).toLowerCase();
        else
            command = content.substring(1).toLowerCase();

        if (command.endsWith("ing")) {
            channel.sendMessage(command.substring(0, command.indexOf("ing")) + "ong").queue();
        } else if (command.equals("help")) {
            channel.sendMessage("Help command coming soon.");
        } else if (command.equals("timeout")) {
            if (event.getMessage().getMember().getRoles().contains(event.getGuild().getRoleById(762319399133642803L))){
                Guild g = event.getGuild();
                //channel.sendMessage("Command under heavy development").queue();
                List<Member> targets = event.getMessage().getMentionedMembers();
                targets.forEach(target -> {
                    if (target.getVoiceState().inVoiceChannel()) {
                        if (!target.getRoles().contains(g.getRoleById(771046070721314856L))) {
                            g.moveVoiceMember(target, g.getVoiceChannelById(763244466135367700L)).queue();
                            g.addRoleToMember(target, g.getRoleById(771046070721314856L)).queue();
                            g.deafen(target, true).queue();
                            channel.sendMessage(target.getEffectiveName() + " is now in a timeout.").queue();
                        } else {
                            g.deafen(target, false).queue();
                            g.moveVoiceMember(target, g.getVoiceChannelById(762164204890882058L)).queue();
                            g.removeRoleFromMember(target, g.getRoleById(771046070721314856L)).queue();
                            channel.sendMessage(target.getEffectiveName() + " is no longer in a timeout.").queue();
                        }
                    } else {
                        channel.sendMessage(target.getEffectiveName() + " is not connected to voice.").queue();
                    }
                });
            } else
                channel.sendMessage("You do not have permission to execute this command").queue();
        } else if (command.equals("poll")) {
            //Example !poll yes:🟢 no:🔴 Should-I-make-Erik-admin?
            ArrayList<String> args = new ArrayList<>(Arrays.asList(content.substring(6).split(" ")));
            EmbedBuilder eb = new EmbedBuilder()
                    .setColor(new Color(242193135))
                    .setAuthor("Cringebot's Epic Poll Function", null, event.getJDA().getSelfUser().getAvatarUrl());
            args.forEach(arg -> {
                if (arg.contains(":")) {
                    String[] a = arg.split(":");
                    eb.addField(a[1], a[0]+":0", true);
                } else {
                    eb.setDescription(arg.replace("-", " "));
                }
            });
            channel.sendMessage(eb.build()).queue(message -> {
                message.getEmbeds().get(0).getFields().forEach(f ->{
                    message.addReaction(f.getName()).queue();
                });
            });
        }
    }


    private void processNotCommand(MessageReceivedEvent event, MessageChannel channel, String content){

        if (event.getMessage().isMentioned(event.getJDA().getSelfUser())) {
            URL url;
            try {
                url = new URL("https://api.kanye.rest/?format=text");
                Scanner sc = new Scanner(url.openStream());
                channel.sendMessage(sc.nextLine()).queue();
                sc.close();
            } catch (IOException e) {
                event.getMessage().getChannel().sendMessage("(API down. Insert Kanye quote here)").queue();
                e.printStackTrace();
            }
        } else if (content.toLowerCase().contains("i'm ")||content.toLowerCase().contains("i’m ")){
            String afterIm = content.substring(Math.max(content.toLowerCase().indexOf("i’m "),content.toLowerCase().indexOf("i'm "))+3);
            if (afterIm.contains("."))
                channel.sendMessage("Hi"+afterIm.substring(0,afterIm.indexOf("."))+", I'm "+event.getJDA().getSelfUser().getName()).queue();
            else
                channel.sendMessage("Hi"+afterIm+", I'm "+event.getJDA().getSelfUser().getName()).queue();
        }

    }


}
