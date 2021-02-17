package com.epicBot.main.messageProcessing.commands;

import com.epicBot.main.Main;
import com.epicBot.main.setup.Configs;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RPS implements Command {

    private final Random rand = new Random();

    private enum opts {
        ROCK(0),
        PAPER(1),
        SCISSORS(2);

        private final int num;
        opts(int num) {
            this.num = num;
        }
        public int getNum(){
            return this.num;
        }
    }

    @Override
    public void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event) {
        String content = message.getContentDisplay();
        RPS.opts choice = getPChoice(content.substring(content.indexOf(" ")+1).toLowerCase());
        if (choice == null){
            error(channel); return;
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
                outTxt = "You Lose :(";
                break;
            case "Draw":
                outTxt = "It's A Tie!";
                break;
        }
        String finalOutTxt = outTxt;
        channel.sendMessage(playerImg + " vs. ...3...").queue((m) -> {
            m.editMessage(playerImg + " vs. ..2..").queueAfter(1, TimeUnit.SECONDS);
            m.editMessage(playerImg + " vs. .1.").queueAfter(2, TimeUnit.SECONDS);
            m.editMessage(playerImg + " vs. "+compImg).queueAfter(3, TimeUnit.SECONDS);
            m.editMessage(playerImg + " vs. "+compImg +"\n" + finalOutTxt).queueAfter(4, TimeUnit.SECONDS);
        });
    }

    private RPS.opts getPChoice(String in){
        switch (in) {
            case "\uD83E\uDEA8":
            case "r":
                return RPS.opts.ROCK;
            case "\uD83D\uDCC4":
            case "p":
                return RPS.opts.PAPER;
            case "✂️":
            case "s":
                return RPS.opts.SCISSORS;
        }
        return null;
    }

    private void error(MessageChannel channel) {
        channel.sendMessage(Configs.key +"rps is a command that lets you play rock paper scissors. Please use one of the 3 following formats:\n```"+Configs.key+"rps [YOUR CHOICE]\n"+Configs.key+"rps [\uD83E\uDEA8/\uD83D\uDCC4/✂️]\n"+Configs.key+"rps [R/P/S️]\n"+Configs.key+"rps [r/p/s️]``` where [] contain required arguments. \nNote that the paper is called \"page_facing_up\" in Discord.").queue();
    }

}
