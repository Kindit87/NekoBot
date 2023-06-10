package com.kindit.bot.listeners;

import com.kindit.bot.listeners.commands.Command;
import com.kindit.bot.listeners.commands.Player;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private static final Command[] commands = {
            new Player()
    };

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (Command command : commands) {
            command.interaction(event);
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandDataList = new ArrayList<>();

        for (Command command : commands) {
            commandDataList.add(command.getSubCommandsData());
        }

        event.getJDA().updateCommands().addCommands(commandDataList).queue();
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandDataList = new ArrayList<>();

        for (Command command : commands) {
            commandDataList.add(command.getCommandData());
        }

        event.getGuild().updateCommands().addCommands(commandDataList).queue();
    }
}