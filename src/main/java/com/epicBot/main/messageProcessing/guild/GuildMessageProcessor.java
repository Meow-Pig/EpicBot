package com.epicBot.main.messageProcessing.guild;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GuildMessageProcessor {

    private enum RPS {
        ROCK(0),
        PAPER(1),
        SCISSORS(2);

        private final int num;
        RPS(int num) {
            this.num = num;
        }
        public int getNum(){
            return this.num;
        }
    }

    String key;
    Random rand = new Random();
    public GuildMessageProcessor(String Key){ key = Key; }
    public String getKey(){
        return key;
    }

    private final long timeoutRoleID = 771046070721314856L;
    private final long adminRoleID   = 762319399133642803L;
    private final long timeoutVCID   = 763244466135367700L;
    private final long generalVCID   = 762164204890882058L;


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
            channel.sendMessage("Help command coming soon.").queue();
        } else if (command.startsWith("echo")) {
            channel.sendMessage(event.getMessage().getContentRaw().substring(event.getMessage().getContentRaw().indexOf(" ")+1)).queue();
            if (command.length() > 4) {
                for (int i = 0; i < Integer.parseInt(command.substring(4))-1; i++){
                    channel.sendMessage(event.getMessage().getContentRaw().substring(event.getMessage().getContentRaw().indexOf(" ")+1)).queue();
                }
            }
            event.getMessage().delete().queue();
        }  else if (command.equals("print")) {
            System.out.println(event.getMessage().getContentRaw().substring(event.getMessage().getContentRaw().indexOf(" ")+1));
        } else if (command.equals("timeout")) {
            if (event.getMessage().getMember().getRoles().contains(event.getGuild().getRoleById(adminRoleID))){
                Guild g = event.getGuild();
                //channel.sendMessage("Command under heavy development").queue();
                List<Member> targets = event.getMessage().getMentionedMembers();
                targets.forEach(target -> {
                    if (target.getVoiceState().inVoiceChannel()) {
                        if (!target.getRoles().contains(g.getRoleById(timeoutRoleID))) {
                            g.moveVoiceMember(target, g.getVoiceChannelById(timeoutVCID)).queue();
                            g.addRoleToMember(target, g.getRoleById(timeoutRoleID)).queue();
                            g.deafen(target, true).queue();
                            channel.sendMessage(target.getEffectiveName() + " is now in a timeout.").queue();
                        } else {
                            g.deafen(target, false).queue();
                            g.moveVoiceMember(target, g.getVoiceChannelById(generalVCID)).queue();
                            g.removeRoleFromMember(target, g.getRoleById(timeoutRoleID)).queue();
                            channel.sendMessage(target.getEffectiveName() + " is no longer in a timeout.").queue();
                        }
                    } else {
                        channel.sendMessage(target.getEffectiveName() + " is not connected to voice.").queue();
                    }
                });
            } else
                channel.sendMessage("You do not have permission to execute this command").queue();
        } else if (command.equals("poll")) {
            //Example !poll yes:🟢,no:🔴,I hate him:😠,Should-I-make-Him-Admin?
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
            channel.sendMessage(eb.build()).queue(message -> {
                message.getEmbeds().get(0).getFields().forEach(f ->{
                    message.addReaction(f.getName()).queue();
                });
            });
        } else if (command.equals("rps")) {
            String args =  content.substring(content.indexOf(" ")+1);
            RPS choice = null;
            switch (args) {
                case "\uD83E\uDEA8":
                case "R":
                    choice = RPS.ROCK;
                    break;
                case "\uD83D\uDCC4":
                case "P":
                    choice = RPS.PAPER;
                    break;
                case "✂️":
                case "S":
                    choice = RPS.SCISSORS;
                    break;
            }
            if (choice == null){
                channel.sendMessage("Command not properly formatted. Please use one of the 2 following formats:\n```"+key+"rps [\uD83E\uDEA8/\uD83D\uDCC4/✂️]\n"+key+"rps [R/P/S️]``` where [] contain required arguments and <> contain optional arguments.\nNote that the paper is called \":page_facing_up:\" in Discord.").queue();
                return;
            }
            int compChoice = rand.nextInt(3);
            String compImg   = (compChoice==0)?"\uD83E\uDEA8":(compChoice==1)?"\uD83D\uDCC4":"✂️";
            String playerImg = (choice.getNum()==0)?"\uD83E\uDEA8":(choice.getNum()==1)?"\uD83D\uDCC4":"✂️";
            String WLD = (choice.getNum()==compChoice)?"Draw":(choice.getNum()==(compChoice+1)%3)?"Win":"Loose";
            String outTxt = "";
            switch (WLD) {
                case "Win":
                    outTxt = "YOU WIN!!";
                    break;
                case "Loose":
                    outTxt = "You Loose :(";
                    break;
                case "Draw":
                    outTxt = "It's A Tie!";
                    break;
            }
            String finalOutTxt = outTxt;
            channel.sendMessage(playerImg + " vs. ...3...").queue((message) -> {
                message.editMessage(playerImg + " vs. ..2..").queueAfter(1, TimeUnit.SECONDS);
                message.editMessage(playerImg + " vs. .1.").queueAfter(2, TimeUnit.SECONDS);
                message.editMessage(playerImg + " vs. "+compImg).queueAfter(3, TimeUnit.SECONDS);
                message.editMessage(playerImg + " vs. "+compImg +"\n" + finalOutTxt).queueAfter(4, TimeUnit.SECONDS);
            });

        }
    }


    private void processNotCommand(MessageReceivedEvent event, MessageChannel channel, String content){

        if (event.getMessage().isMentioned(event.getJDA().getSelfUser()) && !event.getMessage().mentionsEveryone()) {
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
