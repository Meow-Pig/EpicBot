package com.epicBot.main.setup;

import net.dv8tion.jda.api.entities.Activity;

public class Configs {

    //Prefix for all commands "!" or ">" or whatever
    public static final String key = "!";

    //Activity to have the bot doing on startup.
    public static final Activity startupActivity = Activity.listening("Coding Jams");

    //Category containing bot channels. This will store Quotes as well as PM interactions.
    //I recommend putting the bot channel there too.
    public static Long botCategoryId = 811675806937251842L;

    public static final long adminRoleID   = 762319399133642803L;
    public static final long everyoneID    = 762164204299616276L;

    //Stuff for timeout
    public static final long timeoutRoleID = 771046070721314856L;
    public static final long timeoutVCID   = 763244466135367700L;
    public static final long generalVCID   = 762164204890882058L;

}
