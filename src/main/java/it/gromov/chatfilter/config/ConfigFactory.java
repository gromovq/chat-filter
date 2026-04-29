package it.gromov.chatfilter.config;

import it.gromov.chatfilter.config.section.AntiAdsSection;
import it.gromov.chatfilter.config.section.DatabaseSection;
import it.gromov.chatfilter.config.section.MessagesSection;
import it.gromov.chatfilter.config.section.TelegramSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ConfigFactory {
    private ConfigFactory() {}

    public static PluginConfig load(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        PluginConfig cfg = new PluginConfig();
        AntiAdsSection aa = cfg.antiAds;
        aa.enabled = yaml.getBoolean("anti-ads.enabled", aa.enabled);
        aa.ghostMessage = yaml.getBoolean("anti-ads.ghost-message", aa.ghostMessage);
        aa.whitelist = yaml.getStringList("anti-ads.whitelist");
        aa.patterns = yaml.getStringList("anti-ads.patterns");
        aa.blockedWords = yaml.getStringList("anti-ads.blocked-words");

        DatabaseSection db = cfg.database;
        db.type = yaml.getString("database.type", db.type);
        db.sqlite.file = yaml.getString("database.sqlite.file", db.sqlite.file);
        db.mysql.host = yaml.getString("database.mysql.host", db.mysql.host);
        db.mysql.port = yaml.getInt("database.mysql.port", db.mysql.port);
        db.mysql.database = yaml.getString("database.mysql.database", db.mysql.database);
        db.mysql.username = yaml.getString("database.mysql.username", db.mysql.username);
        db.mysql.password = yaml.getString("database.mysql.password", db.mysql.password);
        db.mysql.useSsl = yaml.getBoolean("database.mysql.use-ssl", db.mysql.useSsl);

        TelegramSection tg = cfg.telegram;
        tg.enabled = yaml.getBoolean("telegram.enabled", tg.enabled);
        tg.botToken = yaml.getString("telegram.bot-token", tg.botToken);
        tg.adminChatId = yaml.getString("telegram.admin-chat-id", tg.adminChatId);
        tg.proxy.enabled = yaml.getBoolean("telegram.proxy.enabled", tg.proxy.enabled);
        tg.proxy.type = yaml.getString("telegram.proxy.type", tg.proxy.type);
        tg.proxy.host = yaml.getString("telegram.proxy.host", tg.proxy.host);
        tg.proxy.port = yaml.getInt("telegram.proxy.port", tg.proxy.port);

        MessagesSection msg = cfg.messages;
        msg.blocked = yaml.getString("messages.blocked", msg.blocked);
        msg.blockedGhostEcho = yaml.getString("messages.blocked-ghost-echo", msg.blockedGhostEcho);
        msg.reloadDone = yaml.getString("messages.reload-done", msg.reloadDone);
        msg.noPermission = yaml.getString("messages.no-permission", msg.noPermission);
        return cfg;
    }
}
