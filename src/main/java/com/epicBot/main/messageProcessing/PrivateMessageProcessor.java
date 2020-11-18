package com.epicBot.main.messageProcessing;

import com.epicBot.main.assets.ASCIIart;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PrivateMessageProcessor {

    public void process(MessageReceivedEvent event){
        PrivateChannel channel = event.getPrivateChannel();
        channel.sendMessage(
                "Congrats! You found my hidden easter egg!\n" +
                        "Now right now it does nothing, but Parker may try to do something more with PMs in the future, so he made a framework for responding to them and felt it would be a waste not to use it.\n" +
                        "For now, have some LOTR ASCII art:\n" +
                        "```" + ASCIIart.lotrPoem + "```"
        ).queue();
    }

}
