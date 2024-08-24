package me.zowpy.region.menus.prompt;

import lombok.RequiredArgsConstructor;
import me.zowpy.region.RegionPlugin;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SimpleStringPrompt extends StringPrompt {

    private final String text;
    private final Consumer<String> action;

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        action.accept(s);

        return Prompt.END_OF_CONVERSATION;
    }

    public void start(Player player) {
        ConversationFactory factory = new ConversationFactory(RegionPlugin.getInstance())
                .withFirstPrompt(this)
                .withLocalEcho(false);

        player.beginConversation(factory.buildConversation(player));
    }
}
