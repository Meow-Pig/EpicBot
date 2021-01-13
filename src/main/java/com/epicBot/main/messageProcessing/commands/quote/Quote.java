package com.epicBot.main.messageProcessing.commands.quote;

import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;

public class Quote {

    private int       id;
    private String    text;
    private String    speaker;
    private Date      date = null;
    private QuoteType type = null;

    public Quote(){ }

    public Quote(Message message) {
        String   raw   = message.getContentRaw();
        String[] lines = raw.split("\n");
        int dIndex = lines[2].indexOf(",");
        int tIndex = lines[2].indexOf("(");

        this.id   = Integer.parseInt(raw.substring(7,11));
        this.text = lines[1].substring(2);
        if (dIndex > 0 && tIndex > 0) /* BOTH*/{
            this.type = QuoteType.valueOf(lines[2].substring(tIndex + 1, lines[2].lastIndexOf(")")).toUpperCase());
            this.setDate(lines[2].substring(dIndex+2));
            this.speaker = lines[2].substring(1,tIndex-1);
        } else if (tIndex > 0) /*ONLY type*/{
            this.type = QuoteType.valueOf(lines[2].substring(tIndex + 1, lines[2].lastIndexOf(")")).toUpperCase());
            this.speaker = lines[2].substring(1,tIndex-1);
        } else if (dIndex > 0) /* ONLY date */{
            this.setDate(lines[2].substring(dIndex+2));
            this.speaker = lines[2].substring(1, dIndex);
        } else /*No date or type*/ {
            this.speaker = lines[2].substring(1);
        }
    }

    public boolean isValid() {
        System.out.println(text+speaker);
        return text!=null&&speaker!=null;
    }

    public void setId(int id)              { this.id      = id; }
    public void setText(String text)       { this.text    = text; }
    public void setSpeaker(String speaker) { this.speaker = speaker; }
    public void setType(QuoteType type)    { this.type    = type; }
    public void setDate(Date date)         { this.date    = date; }
    public void setDate(String date) {
        //String must be:
        // MM/DD/YYYY
        // MM/YYYY
        // YYYY
        if (date.contains("/")) {
            int[] l = Arrays.stream(date.split("/")).mapToInt(Integer::parseInt).toArray();
            if (l.length == 3) {
                this.date = new Date(l[2], l[0], l[1]);
            } else if (l.length == 2) {
                this.date = new Date(l[1], l[0]);
            }
        } else {
            this.date = new Date(Integer.parseInt(date));
        }
    }

    public void update(Quote newData){
        if (newData.text != null) {
            setText(newData.text);
        }
        if (newData.speaker != null) {
            setSpeaker(newData.speaker);
        }
        if (newData.type != null) {
            setType(newData.type);
        }
        if (newData.date != null) {
            setDate(newData.date);
        }
    }


    @Override
    public String toString(){
        if (type != null && date != null){
            return String.format("Quote: %04d\n" +
                             "> %s\n" +
                             "-%s (%s), %s",this.id,this.text,this.speaker, this.type.toString().charAt(0)+this.type.toString().substring(1).toLowerCase(), this.date.toString());
        } else if (type != null){
            return String.format("Quote: %04d\n" +
                    "> %s\n" +
                    "-%s (%s)",this.id,this.text,this.speaker, this.type.toString().charAt(0)+this.type.toString().substring(1).toLowerCase());
        } else if (date != null){
            return String.format("Quote: %04d\n" +
                    "> %s\n" +
                    "-%s, %s",this.id,this.text,this.speaker, this.date.toString());
        } else {
            return String.format("Quote: %04d\n" +
                    "> %s\n" +
                    "-%s",this.id,this.text,this.speaker);
        }
    }


}

