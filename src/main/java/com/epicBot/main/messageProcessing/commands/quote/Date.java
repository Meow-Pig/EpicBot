package com.epicBot.main.messageProcessing.commands.quote;

public class Date {

    public int year=-1;
    public int month=-1;
    public int day=-1;

    public Date(int y, int m, int d){
        year  = y;
        month = m;
        day   = d;
    }
    public Date(int y, int m){
        year  = y;
        month = m;
    }
    public Date(int y){
        year  = y;
    }

    @Override
    public String toString(){
        String out = Integer.toString(year);
        if (month != -1){
            out = Integer.toString(month)+"/"+out;
        }
        if (day != -1){
            //M/YYYY
            out = out.substring(0,out.indexOf("/")+1)+Integer.toString(day)+out.substring(out.indexOf("/"));
        }
        return out;
    }
}
