package com.epicBot.main.messageProcessing.commands.quote;

import com.epicBot.main.Main;
import com.epicBot.main.setup.Configs;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class QuoteLibrary {

    private static TextChannel quoteChannel = null;

    private static final ArrayList<Quote>   quoteList = new ArrayList<>();
    private static final ArrayList<Message> msgList   = new ArrayList<>();

    private static final Random rand = new Random();

    private static void revQuoteList()
    {
        ArrayList<Quote> ogList = new ArrayList<>(quoteList);
        quoteList.clear();
        for (int i = ogList.size() - 1; i >= 0; i--) {
            quoteList.add(ogList.get(i));
        }
    }

    private static void revMsgList()
    {
        ArrayList<Message> ogList = new ArrayList<>(msgList);
        msgList.clear();
        for (int i = ogList.size() - 1; i >= 0; i--) {
            msgList.add(ogList.get(i));
        }
    }

    public static void init() {
        AtomicLong id = new AtomicLong(-1L);
        Main.jda.getCategoryById(Configs.botCategoryId).getChannels().forEach(guildChannel -> {
            if (guildChannel.getName().equals("quotes")){
                id.set(guildChannel.getIdLong());
            }
        });
        if (id.get()!=-1L) {
            quoteChannel = Main.jda.getTextChannelById(id.get());
        } else {
            quoteChannel = Main.jda.getTextChannelById(Main.jda.getCategoryById(Configs.botCategoryId).createTextChannel("quote").complete().getIdLong());
        }
        updateLib();
    }

    public static void updateLib(){
        quoteList.clear();
        //Get all messages and iterate through all of them
        System.out.println("Updating Quote Library...");
        quoteChannel.getHistory().retrievePast(100).complete().forEach(message -> {
            quoteList.add(new Quote(message));
            msgList.add(message);
        });
        revQuoteList();
        revMsgList();
        System.out.println("Finished");
    }

    public static void addQuote(Quote quote){
        quote.setId(quoteList.size());
        quoteList.add(quote);
        quoteChannel.sendMessage(quote.toString()).queue();
    }

    public static void editQuote(int id, Quote newQuote){
        getQuote(id).update(newQuote);
        msgList.get(id).editMessage(getQuote(id).toString()).queue();
    }

    public static Quote getQuote(int id){
        return quoteList.get(id);
    }

    public static Quote getRandomQuote(){
        return getQuote(rand.nextInt(quoteList.size()));
    }

}
