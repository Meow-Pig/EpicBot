package com.epicBot.main.listeners;

import com.epicBot.main.messageProcessing.*;

import com.epicBot.main.messageProcessing.guild.GuildMessageProcessor;
import com.epicBot.main.messageProcessing.guild.ReactionProcessor;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class customEventListener extends ListenerAdapter {

    GuildMessageProcessor gmp;
    PrivateMessageProcessor pmp;
    ReactionProcessor rp;

    public customEventListener(GuildMessageProcessor guildMP, PrivateMessageProcessor privateMP, ReactionProcessor reactionP) {

        gmp = guildMP;
        pmp = privateMP;
        rp = reactionP;

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {

            if (event.isFromType(ChannelType.PRIVATE)) {
                System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                        event.getMessage().getContentDisplay());

                pmp.process(event);

            } else {
                System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                        event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                        event.getMessage().getContentDisplay());

                gmp.process(event);

            }
        }
    }

    @Override
    public void onGenericMessageReaction(GenericMessageReactionEvent event){
        try {
            if (event.isFromGuild() && !event.getUser().isBot())
                    rp.processReaction(event);
        } catch (IllegalStateException e) {
            System.out.println("Reaction not in guild");
        }
    }

}