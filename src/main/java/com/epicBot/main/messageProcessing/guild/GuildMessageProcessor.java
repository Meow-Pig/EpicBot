package com.epicBot.main.messageProcessing.guild;

import com.epicBot.main.Main;
import com.epicBot.main.messageProcessing.commands.*;
import com.epicBot.main.messageProcessing.commands.quote.QuoteCommand;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class GuildMessageProcessor {

    public final String key;

    private final RPS          rps;
    private final Poll         poll;
    private final Timeout      timeout;
    private final Help         help;
    private final Echo         echo;
    private final QuoteCommand quoteCommand;
    private final Shutdown     shutdown;
    public GuildMessageProcessor(String Key){
        //Initialize key
        key = Key;

        //Initialize command processors
        rps          = new RPS();
        poll         = new Poll();
        timeout      = new Timeout();
        help         = new Help();
        echo         = new Echo();
        quoteCommand = new QuoteCommand();
        shutdown     = new Shutdown();
    }



    public void process(MessageReceivedEvent event){

        MessageChannel channel = event.getChannel();
        String content = event.getMessage().getContentStripped();

        if (event.getMessage().getContentStripped().startsWith(key))
            processCommand(event,channel,content);
        else
            processNotCommand(event,channel,content);
    }


    private void processCommand(MessageReceivedEvent event, MessageChannel channel, String content) {

        //One line if statements baby!
        String command = content.contains(" ")?content.substring(1, content.indexOf(" ")).toLowerCase():content.substring(1).toLowerCase();

        if (command.endsWith("ing")) {
            channel.sendMessage(command.substring(0, command.indexOf("ing")) + "ong").queue();
        } else if (command.startsWith("echo")) {
            echo.processCommand(event.getMessage(), channel, event);
        } else {
            switch (command) {
                case "help":
                    help.processCommand(event.getMessage(), channel, event);
                    break;
                case "print":
                    System.out.println(event.getMessage().getContentRaw().substring(event.getMessage().getContentRaw().indexOf(" ") + 1));
                    break;
                case "timeout":
                    timeout.processCommand(event.getMessage(), channel, event);
                    break;
                case "poll":
                    poll.processCommand(event.getMessage(), channel, event);
                    break;
                case "rps":
                    rps.processCommand(event.getMessage(), channel, event);
                    break;
                case "quote":
                    quoteCommand.processCommand(event.getMessage(), channel, event);
                    break;
                case "shutdown":
                    shutdown.processCommand(event.getMessage(), channel, event);
                    break;
                default:
                    channel.sendMessage("That appears to be an invalid command. To view a list of commands, please use " + Main.key +"help").queue();
            }
        }
    }


    private void processNotCommand(MessageReceivedEvent event, MessageChannel channel, String content){

        if (event.getMessage().isMentioned(event.getJDA().getSelfUser()) && !event.getMessage().mentionsEveryone()) {
            URL url;
            try {
                url = new URL("https://api.kanye.rest/?format=text");
                Scanner sc = new Scanner(url.openStream());
                channel.sendMessage(sc.nextLine()).queue();
                sc.close();
            } catch (IOException e) {
                event.getMessage().getChannel().sendMessage("(API down. Insert Kanye quote here)").queue();
                e.printStackTrace();
            }
        } else if (content.toLowerCase().contains("i'm ")||content.toLowerCase().contains("i’m ")){
            String afterIm = content.substring(Math.max(content.toLowerCase().indexOf("i’m "),content.toLowerCase().indexOf("i'm "))+3);
            if (afterIm.contains("."))
                channel.sendMessage("Hi"+afterIm.substring(0,afterIm.indexOf("."))+", I'm "+event.getJDA().getSelfUser().getName()).queue();
            else
                channel.sendMessage("Hi"+afterIm+", I'm "+event.getJDA().getSelfUser().getName()).queue();
        }

    }


}
