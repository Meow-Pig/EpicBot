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

    //If you are using an ide like IntelliJ Eclipse or VScode there should be an option to set the arguments when you run it.
    //Set the arguments parameter to "[token] [key]"

    public static GuildMessageProcessor gmp;
    public static PrivateMessageProcessor pmp;
    public static ReactionProcessor rp;

    public static String key;

    public static void main(String[] args) {

        key = args[1];

        //Create processors to deal with incoming events like messages and reactions
        gmp = new GuildMessageProcessor(key);
        pmp = new PrivateMessageProcessor();
        rp = new ReactionProcessor();

        //Create JDA
        JDA jda = BuilderSetup.buildWithConfigs(args[0]);
        if (jda != null) {
            jda.addEventListener(new customEventListener(gmp, pmp, rp));
            System.out.println("Successfully Logged in as " + jda.getSelfUser().getName());
        } else
            System.out.println("Failed to login");
    }
}
