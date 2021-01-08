package com.epicBot.main.messageProcessing.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class Timeout implements Command{

    private final long timeoutRoleID = 771046070721314856L;
    private final long adminRoleID   = 762319399133642803L;
    private final long timeoutVCID   = 763244466135367700L;
    private final long generalVCID   = 762164204890882058L;

    @Override
    public void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event) {
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
        } else {
            channel.sendMessage("You do not have permission to execute this command").queue();
        }
    }
}
