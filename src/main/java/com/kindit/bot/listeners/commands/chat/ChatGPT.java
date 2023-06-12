package com.kindit.bot.listeners.commands.chat;

import com.kindit.bot.data.JSONConfig;
import com.kindit.bot.listeners.commands.Command;
import com.kindit.bot.listeners.commands.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPT implements Command {
    private final String name = "chat-gpt";
    @Override
    public SubCommand[] getSubCommands() {
        return new SubCommand[] {

        };
    }

    @Override
    public CommandData getCommandData() {
        return Commands.slash(name, "ChatGPT command")
                .addOptions(
                        new OptionData(OptionType.STRING, "text", "Your text for chat-gpt", true)
                                .setMaxLength(256)
                );
    }

    @Override
    public void interaction(SlashCommandInteractionEvent event) throws Exception {
        if (!event.getFullCommandName().equals(name)) { return; }
        event.deferReply().queue();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(event.getMember().getNickname(), null, event.getMember().getEffectiveAvatarUrl());
        eb.setTitle(event.getOption("text").getAsString());
        eb.setDescription(chatGPT(event.getOption("text").getAsString()));
        eb.setFooter("OpenAI", "https://img.uxwing.com/wp-content/themes/uxwing/download/brands-social-media/chatgpt-icon.png");
        eb.setColor(new Color(25, 195, 125));

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }

    public static String chatGPT(String text) throws Exception {
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", JSONConfig.GPTKeyAPI);

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", text);
        data.put("max_tokens", 4000);
        data.put("temperature", 1.0);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        return (new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text")).toString();
    }
}
