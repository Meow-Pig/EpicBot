package com.epicBot.main.messageProcessing.guild;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import java.util.List;

public class ReactionProcessor {


    public void processReaction(GenericMessageReactionEvent event){
        System.out.println("Reaction");
        Message m = event.retrieveMessage().complete();
        MessageEmbed e = m.getEmbeds().get(0);

        if (!e.isEmpty() && e.getAuthor().getName().contains("Poll")){

            EmbedBuilder eb = new EmbedBuilder(e).clearFields();

            List<MessageEmbed.Field> fields = e.getFields();

            fields.forEach(f -> { m.getReactions().forEach(r ->{
                if (r.getReactionEmote().getEmoji().equals(f.getName())) {
                    eb.addField(r.getReactionEmote().getEmoji(), "**" + f.getValue().split(":")[0] + ":" + (r.getCount()-1) + "**", true);
                }
            });});
            m.editMessage(eb.build()).queue();
        }
    }
}
