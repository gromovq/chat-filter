package it.gromov.chatfilter.notify.telegram;

import it.gromov.chatfilter.config.section.TelegramSection;
import it.gromov.chatfilter.database.model.ViolationRecord;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class TelegramNotifier {
    private final Logger logger;
    private final TelegramSection config;

    public TelegramNotifier(Logger logger, TelegramSection config) {
        this.logger = logger;
        this.config = config;
    }

    public void notifyViolation(ViolationRecord record) {
        if (!config.enabled || config.botToken.isEmpty() || config.adminChatId.isEmpty()) {
            return;
        }

        String text = "🚨 ZChatFilter\n"
                + "Игрок: " + record.playerName + "\n"
                + "IP: " + record.ip + "\n"
                + "Причина: " + record.reason + "\n"
                + "Сообщение: " + record.message;

        try {
            String api = "https://api.telegram.org/bot" + config.botToken + "/sendMessage";
            String query = "chat_id=" + encode(config.adminChatId) + "&text=" + encode(text);
            URL url = new URL(api + "?" + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(resolveProxy());
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int code = connection.getResponseCode();
            if (code >= 300) {
                logger.warning("Telegram notify failed with code " + code);
            }
        } catch (IOException ex) {
            logger.warning("Telegram notify failed: " + ex.getMessage());
        }
    }

    private Proxy resolveProxy() {
        if (!config.proxy.enabled) {
            return Proxy.NO_PROXY;
        }
        Proxy.Type type = "HTTP".equalsIgnoreCase(config.proxy.type) ? Proxy.Type.HTTP : Proxy.Type.SOCKS;
        return new Proxy(type, new InetSocketAddress(config.proxy.host, config.proxy.port));
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
