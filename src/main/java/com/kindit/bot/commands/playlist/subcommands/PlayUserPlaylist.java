package com.kindit.bot.commands.playlist.subcommands;

import com.kindit.bot.commands.Command;
import com.kindit.bot.commands.Subcommand;
import com.kindit.bot.data.JsonUserPlaylistData;
import com.kindit.bot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayUserPlaylist extends Subcommand {

    public PlayUserPlaylist(String name, String description, Command parentCommand) {
        super(name, description, parentCommand);
    }

    @Override
    public SubcommandData getSubCommandData() {
        return new SubcommandData(userName, description);
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        event.deferReply().setEphemeral(true).queue();

        TextChannel textChannel = event.getChannel().asTextChannel();
        JsonUserPlaylistData playlistData = new JsonUserPlaylistData(event.getMember().getIdLong());
        MessageEmbed responseEmbed;

        if (!event.getMember().getVoiceState().inAudioChannel()) {
            responseEmbed = Command.replyEmbed("You need to be in a voice channel for this command work.", Command.BAD_COLOR);
        }
        else {
            connectPlayer(event);
            addPlaylistToPlayer(playlistData, textChannel);
            responseEmbed = Command.replyEmbed("Playlist has been added to the queue", Command.GOOD_COLOR);
        }

        event.getHook().sendMessageEmbeds(responseEmbed).queue();
    }

    private void connectPlayer(SlashCommandInteractionEvent event) {
        final AudioManager audioManager = event.getGuild().getAudioManager();
        final VoiceChannel memberchannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

        if (!audioManager.isConnected()) {
            audioManager.openAudioConnection(memberchannel);
        }
    }

    private void addPlaylistToPlayer(JsonUserPlaylistData playlistData, TextChannel textChannel) {
        playlistData.getUserTracks().forEach((key, value) -> PlayerManager.getINSTANCE().loadAndPlay(textChannel, value));
    }
}
