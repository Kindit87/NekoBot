package com.kindit.bot.data;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

public class KeysResponce {
    public final Long[] guilds;
    public final String[] keys;
    public final String response;
    public final boolean reaction;
    public final boolean active;

    public KeysResponce(Long[] guilds, String[] keys, String response, boolean reaction, boolean active) {
        this.guilds = guilds;
        this.keys = keys;
        this.response = response;
        this.reaction = reaction;
        this.active = active;

        if (reaction && !isReaction(this.response)) {
            throw new RuntimeException("You meant to send this --> " + response + " <-- as a response, but that's not what it looks like");
        }
    }

    private boolean isReaction(String string) {
        return string.matches("(<a?)?:\\w+:(\\d{18}>)?");
    }
}
