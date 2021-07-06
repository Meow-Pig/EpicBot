package com.epicBot.main.messageProcessing.commands.Audio;

import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import vocalcord.CommandChain;
import vocalcord.UserStream;
import vocalcord.VocalCord;

public class CustomVocalCord {

    private static final VocalCord cord;

    private static final CordCallbacks callbacks = new CordCallbacks();

    static {
        cord = VocalCord.newConfig(callbacks).withTTS( SsmlVoiceGender.MALE ,
           true).withLanguage("en-AU").build();
    }

    public static void say(String text, VoiceChannel channel) throws NullPointerException {
        if (channel != null) {
            cord.connect(channel);
            cord.say(text);

            return;
        }
        throw new NullPointerException();
    }

    private static class CordCallbacks implements VocalCord.Callbacks{
        @Override
        public void onTranscribed(User user, String transcript) {

        }

        @Override
        public CommandChain onTranscribed() {
            return null;
        }

        @Override
        public boolean canWakeBot(User user) {
            return false;
        }

        @Override
        public void onWake(UserStream userStream, int keywordIndex) {

        }

        @Override
        public void onTTSCompleted() {

        }
    }
}
