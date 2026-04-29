package it.gromov.chatfilter.config.section;

import eu.okaeri.configs.OkaeriConfig;

public class TelegramSection extends OkaeriConfig {
    public boolean enabled = false;
    public String botToken = "";
    public String adminChatId = "";
    public Proxy proxy = new Proxy();

    public static class Proxy extends OkaeriConfig {
        public boolean enabled = false;
        public String type = "SOCKS";
        public String host = "127.0.0.1";
        public int port = 1080;
    }
}
