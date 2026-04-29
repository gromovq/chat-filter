package it.gromov.chatfilter.bootstrap;

import co.aikar.commands.PaperCommandManager;
import it.gromov.chatfilter.command.ZChatFilterCommand;
import it.gromov.chatfilter.config.ConfigFactory;
import it.gromov.chatfilter.config.PluginConfig;
import it.gromov.chatfilter.database.DatabaseManager;
import it.gromov.chatfilter.filter.AdFilterService;
import it.gromov.chatfilter.listener.ChatListener;
import it.gromov.chatfilter.notify.telegram.TelegramNotifier;
import it.gromov.chatfilter.service.ViolationService;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatFilterPlugin extends JavaPlugin {

    private PluginConfig config;
    private DatabaseManager databaseManager;
    private ViolationService violationService;
    private AdFilterService adFilterService;
    private TelegramNotifier telegramNotifier;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.reloadEverything();

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new ZChatFilterCommand(this));
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
    }

    public void reloadEverything() {
        reloadConfig();
        this.config = ConfigFactory.load(this);

        if (databaseManager != null) {
            databaseManager.close();
        }

        this.databaseManager = new DatabaseManager(this, config.database);
        this.databaseManager.init();
        this.violationService = new ViolationService(this, databaseManager);
        this.telegramNotifier = new TelegramNotifier(getLogger(), config.telegram);
        this.adFilterService = new AdFilterService(config.antiAds);
    }

    public PluginConfig getPluginConfig() {
        return config;
    }

    public ViolationService getViolationService() {
        return violationService;
    }

    public AdFilterService getAdFilterService() {
        return adFilterService;
    }

    public TelegramNotifier getTelegramNotifier() {
        return telegramNotifier;
    }
}
