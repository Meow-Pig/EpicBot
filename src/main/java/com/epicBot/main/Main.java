package com.epicBot.main;

import com.epicBot.main.messageProcessing.guild.GuildMessageProcessor;
import com.epicBot.main.messageProcessing.guild.ReactionProcessor;
import com.epicBot.main.setup.BuilderSetup;
import com.epicBot.main.listeners.customEventListener;
import com.epicBot.main.messageProcessing.*;

import net.dv8tion.jda.api.JDA;

public class Main {

    //Run like this:
    //Main.java [token] [key]
    //key is what will be used as a command prefix. like "/" or "!" or ">"
    //token is the token for your bot
    public static void main(String[] args){

        //Create processors to deal with incoming events like messages and reactions
        GuildMessageProcessor gmp = new GuildMessageProcessor(args[1]);
        PrivateMessageProcessor pmp = new PrivateMessageProcessor();
        ReactionProcessor rp = new ReactionProcessor();

        JDA jda = BuilderSetup.buildWithConfigs(args[0]);
        if (jda!=null) {
            jda.addEventListener(new customEventListener(gmp, pmp, rp));
            System.out.println("Successfully Logged in as " + jda.getSelfUser().getName());
        } else
            System.out.println("Failed to login");

    }

}
