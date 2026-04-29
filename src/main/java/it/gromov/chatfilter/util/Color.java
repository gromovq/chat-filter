package it.gromov.chatfilter.util;

import org.bukkit.ChatColor;

public final class Color {
    private Color() {}

    public static String of(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
