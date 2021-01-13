package com.epicBot.main.messageProcessing.commands.quote;

import com.epicBot.main.Main;
import com.epicBot.main.messageProcessing.commands.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class QuoteCommand implements Command {

    @Override
    public void processCommand(Message message, MessageChannel channel, MessageReceivedEvent event) {
        //Create an arraylist of arrays
        //Each array has the arg name (converted to lowercase) and the arg itself
        //Example Arraylist:
        //{{"d","12-02-2020"},
        // {"q","I like ham and cheese in bacon grease"},
        // {"s","Billy Bob Joe"}}
        //Original message:
        //!quote -D=12-02-2020 -q=I like ham and cheese in bacon grease -S=Billy Bob Joe
        Quote quote = new Quote();

        AtomicReference<Boolean> newQuote = new AtomicReference<>(true );
        AtomicReference<Boolean> edit     = new AtomicReference<>(false);
        AtomicInteger editID              = new AtomicInteger(-1);

        ArrayList<String[]> args = Command.parseCommand(message.getContentDisplay().substring(message.getContentDisplay().indexOf(" ")));
        args.forEach(l -> {
            switch (l[0]) {
                case "q":
                case "quote":
                    quote.setText(l[1]);
                    break;
                case "s":
                case "speaker":
                    quote.setSpeaker(l[1]);
                    break;
                case "d":
                case "date":
                    quote.setDate(l[1]);
                    break;
                case "t":
                case "type":
                    switch (l[1].toLowerCase()){
                        case "irl":
                            quote.setType(QuoteType.IRL);
                            break;
                        case "discord":
                        case "d":
                            quote.setType(QuoteType.DISCORD);
                            break;
                        case "text":
                        case "t":
                            quote.setType(QuoteType.TEXT);
                            break;
                        case "game":
                        case "g":
                            quote.setType(QuoteType.GAME);
                            break;
                        default:
                            channel.sendMessage("Invalid use of the command. INVALID TYPE: "+l[1]+"\nUse "+ Main.key+"quote -h/-help for help").queue();
                            newQuote.set(false);
                    }
                    break;
                case "h":
                case "help":
                    sendHelpMessage(channel);
                    newQuote.set(false);
                    break;
                case "id":
                    channel.sendMessage(QuoteLibrary.getQuote(Integer.parseInt(l[1])).toString()).queue();
                    newQuote.set(false);
                    break;
                case "r":
                case "random":
                    channel.sendMessage(QuoteLibrary.getRandomQuote().toString()).queue();
                    newQuote.set(false);
                    break;
                case "e":
                case "edit":
                    editID.set(Integer.parseInt(l[1]));
                    newQuote.set(false);
                    edit.set(true);
                    break;
                default:
                    channel.sendMessage("Invalid use of the command. INVALID ARG: "+l[0]+"\nUse "+ Main.key+"quote -h/-help for help").queue();
                    newQuote.set(false);
            }
        });
        if (newQuote.get()) {
            if (quote.isValid()) {
                QuoteLibrary.addQuote(quote);
                channel.sendMessage("Quote successfully added!").queue();
            } else {
                channel.sendMessage("Invalid use of the command. MISSING QUOTE OR SPEAKER\nUse "+ Main.key+"quote -h/-help for help").queue();
            }
        } if (edit.get()){
            QuoteLibrary.editQuote(editID.get(),quote);
            channel.sendMessage("Quote successfully edited!").queue();
        }
    }

    public void sendHelpMessage(MessageChannel channel){
        channel.sendMessage("**Quote Command Help**```" +
                "-h/-help              Shows help message\n" +
                "-r/-random            Shows a random quote from the library\n" +
                "-id=[id]              Shows the quote from the library with the id of [id]\n" +
                "-e/-edit=[id]         Instead of creating a new quote the quote with the id of [id] will be edited\n" +
                "-q/-quote=[Quote]     Sets the contents of the quote*\n" +
                "-s/-speaker=[Speaker] Sets the speaker*\n" +
                "-t/-type=[Type]       Sets the type. Acceptable types are:\n" +
                "  irl\n" +
                "  d/discord\n" +
                "  t/text (a text message)\n" +
                "  g/game (game chat)\n" +
                "-d/-date=[Date]       Sets the date. Acceptable formats are:\n" +
                "  M/D/Y\n" +
                "  M/Y\n" +
                "  Y\n" +
                "  Use \"/\" please\n\n" +
                "Example Usage: !quote -q=Let there be light! -s=god -t=irl\n\n" +
                "*Denotes a required argument (only when adding a new quote to the library)\nArguments are case-insensitive```").queue();
    }

}
