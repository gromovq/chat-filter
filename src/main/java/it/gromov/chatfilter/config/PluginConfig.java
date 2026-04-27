package it.gromov.chatfilter.config;

import eu.okaeri.configs.OkaeriConfig;
import it.gromov.chatfilter.config.section.AntiAdsSection;
import it.gromov.chatfilter.config.section.DatabaseSection;
import it.gromov.chatfilter.config.section.MessagesSection;
import it.gromov.chatfilter.config.section.TelegramSection;

public class PluginConfig extends OkaeriConfig {
    public AntiAdsSection antiAds = new AntiAdsSection();
    public DatabaseSection database = new DatabaseSection();
    public TelegramSection telegram = new TelegramSection();
    public MessagesSection messages = new MessagesSection();
}
