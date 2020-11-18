package com.epicBot.main.setup;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class BuilderSetup {

    public static JDA buildWithConfigs(String token){
        try {
            return JDABuilder.createDefault("NzY0MzI4NzU1MjcxMzAzMTY5.X4Eqnw.OGcv72iIDrmqjx0gyshJhIWkc9Q", GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                    .setActivity(Activity.watching("Sam poop"))
                    .build().awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
