package com.epicBot.main;

import com.epicBot.main.messageProcessing.commands.quote.QuoteLibrary;
import com.epicBot.main.messageProcessing.guild.GuildMessageProcessor;
import com.epicBot.main.messageProcessing.guild.ReactionProcessor;
import com.epicBot.main.setup.BuilderSetup;
import com.epicBot.main.listeners.customEventListener;
import com.epicBot.main.messageProcessing.*;

import com.epicBot.main.setup.Configs;
import net.dv8tion.jda.api.JDA;

public class Main {

    //Run like this:
    //Main.java [token]
    //[token] the token for your bot

    //If you are using an ide like IntelliJ Eclipse or VScode there should be an option to set the arguments when you run it.
    //Set the arguments parameter to "[token]"

    public static GuildMessageProcessor gmp;
    public static PrivateMessageProcessor pmp;
    public static ReactionProcessor rp;

    public static JDA jda;

    public static void main(String[] args) {

        //Create processors to deal with incoming events like messages and reactions
        gmp = new GuildMessageProcessor(Configs.key);
        pmp = new PrivateMessageProcessor();
        rp = new ReactionProcessor();

        //Create JDA
        jda = BuilderSetup.buildWithConfigs(args[0]);
        if (jda != null) {
            jda.addEventListener(new customEventListener(gmp, pmp, rp));
            System.out.println("Successfully Logged in as " + jda.getSelfUser().getName());
        } else
            System.out.println("Failed to login");

        QuoteLibrary.init();

    }
}
