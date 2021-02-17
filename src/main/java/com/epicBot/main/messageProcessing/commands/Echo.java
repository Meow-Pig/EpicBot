package com.epicBot.main.messageProcessing.commands;

import com.epicBot.main.Main;
import com.epicBot.main.messageProcessing.PrivateMessageProcessor;
import com.epicBot.main.setup.Configs;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Echo implements Command {
    @Override
    public void processCommand(Message message, MessageChannel mChannel, MessageReceivedEvent event) {

        String content = message.getContentRaw();

        ArrayList<String[]> args = Command.parseCommand(content.substring(content.indexOf(" ")));

        AtomicReference<String> text            = new AtomicReference<>("");
        AtomicInteger repeat                    = new AtomicInteger(1);
        AtomicReference<MessageChannel> channel = new AtomicReference<>(mChannel);
        AtomicReference<User> messageRecipient  = new AtomicReference<>(null);
        AtomicBoolean delete                    = new AtomicBoolean(true);
        ArrayList<String> errors                = new ArrayList<>();

        args.forEach(arg -> {
            switch (arg[0]){
                case "h":
                case "help":
                    errors.add("Help");
                    return;
                case "m":
                case "message":
                    try {
                        text.set(arg[1]);
                    } catch (IndexOutOfBoundsException e){
                        errors.add("Missing message argument");
                    }
                    break;
                case "r":
                case "repeat":
                    try {
                        repeat.set(Integer.parseInt(arg[1]));
                    } catch (NumberFormatException e){
                        errors.add("Invalid repeat argument");
                    } catch (IndexOutOfBoundsException e){
                        errors.add("Missing repeat argument");
                    }
                    break;
                case "c":
                case "channel":
                    try{
                        MessageChannel c = message.getJDA().getTextChannelById(arg[1].substring(arg[1].indexOf("#")+1,arg[1].indexOf("#")+19));
                        if (c == null) throw new NullPointerException();
                        if (c.getName().startsWith("pm-")) throw new Exception();
                        channel.set(c);
                    } catch (NullPointerException e){
                        errors.add("Invalid channel");
                    } catch (IndexOutOfBoundsException e){
                        errors.add("Missing channel argument");
                    } catch (Exception e) {
                        errors.add("Cannot echo into a pm log channel");
                    }
                    break;
                case "person":
                case "dm":
                case "pm":
                    try {
                        User receiver = message.getJDA().retrieveUserById(arg[1].substring(arg[1].indexOf("!") + 1, arg[1].indexOf("!") + 19)).complete();
                        if (receiver == null) throw new NullPointerException();
                        messageRecipient.set(receiver);
                    } catch (NullPointerException e){
                        System.out.println(e.toString());
                        errors.add("Invalid pm recipient");
                    } catch (IndexOutOfBoundsException e){
                        errors.add("Missing pm argument");
                    }
                    break;
                case "d":
                case "delete":
                    try {
                        switch (arg[1].toLowerCase()) {
                            case "t":
                            case "true":
                            case "y":
                            case "yes":
                                delete.set(true);
                                break;
                            case "f":
                            case "false":
                            case "n":
                            case "no":
                                delete.set(false);
                                break;
                            default:
                                errors.add("Invalid delete argument");
                        }
                    } catch (IndexOutOfBoundsException e){
                        errors.add("Missing delete argument");
                    }
            }
        });

        if (errors.size() == 0) {
            if (delete.get()){
                message.delete().queue();
            }
            if (messageRecipient.get() != null) {
                TextChannel outChannel = PrivateMessageProcessor.getOutChannel(messageRecipient.get());
                for (int i = 0; i < repeat.get(); i++) {
                    messageRecipient.get().openPrivateChannel().queue(c -> c.sendMessage(text.get()).queue(message1 -> {
                        outChannel.sendMessage(Main.jda.getSelfUser().getName()+": "+message1.getContentDisplay()).queue();
                    }));
                }
            } else {
                for (int i = 0; i < repeat.get(); i++) {
                    channel.get().sendMessage(text.get()).queue();
                }
            }
        } else {
            if (errors.contains("Help")){
                this.showHelpMessage(mChannel);
            } else {
                StringBuilder errorMessage = new StringBuilder("Echo command invalid:");
                errors.forEach(e -> errorMessage.append("\n> ").append(e));
                errorMessage.append("\nFor command help, do " + Configs.key + "echo -h/-help");
                channel.get().sendMessage(errorMessage.toString()).queue();
            }
        }
    }

    public void showHelpMessage(MessageChannel channel){
        channel.sendMessage("**Echo Command Help**```" +
                "-h/-help              Shows help message\n" +
                "-m/-message=[Message] Sets the message to be echoed by Cringebot*\n" +
                "-r/repeat=[Repeat #]  Sets the number of times to repeat the message. Defaults to 1.\n" +
                "-c/-channel=[Channel] Sets the channel to send the message to. Defaults to the current channel. Will not work with recipient set.\n" +
                "-pm/-dm=[Recipient]   Sets the recipient of the message. Defaults to nobody.\n" +
                "-d/-delete=[Delete?]  Sets if your command should be deleted. Defaults to true. Accepted inputs are\n" +
                "  False:\n"+
                "    n,no,f,false\n" +
                "  True:\n"+
                "    y,yes,t,true\n" +
                "\nExample Usage: !echo -m=Something Cringebot will say -c= #general -d=f -r=10\n\n" +
                "Please make sure the channels and people get the blue highlight when sending a message.\n*Denotes a required argument \nArguments are case-insensitive```").queue();
    }
}
