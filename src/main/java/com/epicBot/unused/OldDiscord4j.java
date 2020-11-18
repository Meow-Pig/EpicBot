/*package com.epicBot.unused;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.GuildMemberEditSpec;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class OldDiscord4j {

    private static final Map<String, Command> commands = new HashMap<>();

    private GuildMemberEditSpec editUserVC(GuildMemberEditSpec gmes, Mono<Member> member, VoiceChannel channel) {
        gmes.setNewVoiceChannel(channel.getId());
        return gmes;
    }

    static {
        commands.put("ping", event -> event.getMessage()
                .getChannel().block()
                .createMessage("Pong!").block());
        commands.put("timeout", event -> {
            event.getMessage()
                    .getChannel().block()
                    .createMessage("Command disabled because Parker is bad at coding").block();
            String content = event.getMessage().getContent();

            reactor.core.publisher.Flux<Member> members = event.getMessage().getUserMentions().flatMap(users -> users.asMember(event.getGuild().block().getId()));
            members.blockFirst().edit(guildMemberEditSpec -> guildMemberEditSpec.setNickname("poopy"));
            members.toIterable().forEach(m -> m.edit(guildMemberES -> guildMemberES.setDeafen(true)));

            Iterable<String> names = members.map(m -> m.getDisplayName()).toIterable();
            names.forEach(s -> event.getMessage().getChannel().block().createMessage(s + " has been put in a timeout.").block());


        });
        commands.put("ding", event -> event.getMessage()
                .getChannel().block()
                .createMessage("Dong!").block());

    }

    interface Command {
        void execute(MessageCreateEvent event);
    }

    public static void main(String[] args) {
        GatewayDiscordClient client = DiscordClientBuilder.create("NzY0MzI4NzU1MjcxMzAzMTY5.X4Eqnw.OGcv72iIDrmqjx0gyshJhIWkc9Q").build().login().block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                });

        client.getEventDispatcher().on(MessageCreateEvent.class)
                // subscribe is like block, in that it will *request* for action
                // to be done, but instead of blocking the thread, waiting for it
                // to finish, it will just execute the results asynchronously.
                .subscribe(event -> {
                    final String content = event.getMessage().getContent(); // 3.1 Message.getContent() is a String
                    for (final Map.Entry<String, Command> entry : commands.entrySet()) {
                        // We will be using ! as our "prefix" to any command in the system.
                        if (content.startsWith('!' + entry.getKey())) {
                            entry.getValue().execute(event);
                            break;
                        } else if ((content.toLowerCase().contains("i'm ") || content.toLowerCase().contains("i’m ")) && !event.getMessage().getUserData().id().equals(client.getSelfId().asString())) {
                            int num = Math.max(content.toLowerCase().indexOf("i'm "), content.toLowerCase().indexOf("i’m "));
                            String thing = content.substring(num+4);
                            event.getMessage().getChannel().block().createMessage("Hi "+thing+", I'm "+client.getSelf().block().asMember(event.getGuild().block().getId()).block().getDisplayName()).block();
                            break;
                        } else if (Objects.equals(event.getMessage().getUserMentions().blockFirst(), client.getSelf().block())) {
                            try {
                                URL url = new URL("https://api.kanye.rest/?format=text");
                                Scanner sc = new Scanner(url.openStream());
                                event.getMessage().getChannel().block().createMessage(sc.nextLine()).block();
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                });


        client.onDisconnect().block();
    }
}*/