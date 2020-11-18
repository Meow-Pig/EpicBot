package com.epicBot.testing;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;

class Testing{

    public static void main(String[] args){
        JDA jda;
        try {
            jda = JDABuilder.createDefault("NzY0MzI4NzU1MjcxMzAzMTY5.X4Eqnw.OGcv72iIDrmqjx0gyshJhIWkc9Q").build().awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            return;
        }
        Guild g = jda.getGuildById(762164204299616276l);
        g.getTextChannelById(762324038810337290L).retrieveMessageById(771967260608561164L).queue(m -> {
            System.out.println(m.getEmbeds().get(0));
            System.out.println(m.getEmbeds().get(0).getColor());
            System.out.println(m.getEmbeds().get(0).getAuthor().getName());
            System.out.println(m.getEmbeds().get(0).getDescription());
            m.getEmbeds().get(0).getFields().forEach(f -> {
                System.out.println(f);
                System.out.println(f.getName());
            });
        });
        jda.shutdown();
    }

}