package com.epicBot.main.messageProcessing.commands;

import com.epicBot.main.setup.Configs;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class Timeout implements Command{

    @Override
    public void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event) {
        if (event.getMessage().getMember().getRoles().contains(event.getGuild().getRoleById(Configs.adminRoleID))){
            Guild g = event.getGuild();
            //channel.sendMessage("Command under heavy development").queue();
            List<Member> targets = event.getMessage().getMentionedMembers();
            targets.forEach(target -> {
                if (target.getVoiceState().inVoiceChannel()) {
                    if (!target.getRoles().contains(g.getRoleById(Configs.timeoutRoleID))) {
                        g.moveVoiceMember(target, g.getVoiceChannelById(Configs.timeoutVCID)).queue();
                        g.addRoleToMember(target, g.getRoleById(Configs.timeoutRoleID)).queue();
                        g.deafen(target, true).queue();
                        channel.sendMessage(target.getEffectiveName() + " is now in a timeout.").queue();
                    } else {
                        g.deafen(target, false).queue();
                        g.moveVoiceMember(target, g.getVoiceChannelById(Configs.generalVCID)).queue();
                        g.removeRoleFromMember(target, g.getRoleById(Configs.timeoutRoleID)).queue();
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
