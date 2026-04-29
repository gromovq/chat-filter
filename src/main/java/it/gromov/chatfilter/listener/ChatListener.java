package it.gromov.chatfilter.listener;

import it.gromov.chatfilter.bootstrap.ChatFilterPlugin;
import it.gromov.chatfilter.filter.FilterResult;
import it.gromov.chatfilter.util.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final ChatFilterPlugin plugin;

    public ChatListener(ChatFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        FilterResult result = plugin.getAdFilterService().check(event.getMessage());
        if (!result.isBlocked()) {
            return;
        }

        event.setCancelled(true);
        plugin.getViolationService().record(player, event.getMessage(), result.getReason());
        player.sendMessage(Color.of(plugin.getPluginConfig().messages.blocked));

        if (plugin.getPluginConfig().antiAds.ghostMessage) {
            player.sendMessage(Color.of(plugin.getPluginConfig().messages.blockedGhostEcho.replace("%message%", event.getMessage())));
        }
    }
}
