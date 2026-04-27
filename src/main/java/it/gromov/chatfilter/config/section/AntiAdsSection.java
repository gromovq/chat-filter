package it.gromov.chatfilter.config.section;

import eu.okaeri.configs.OkaeriConfig;

import java.util.Arrays;
import java.util.List;

public class AntiAdsSection extends OkaeriConfig {
    public boolean enabled = true;
    public boolean ghostMessage = true;
    public List<String> whitelist = Arrays.asList(
            "zertix.su",
            "zertix",
            "dataforest_net",
            "t.me/zertix",
            "t.me/dataforest_net",
            "discord.gg/zertix",
            "@dataforest_net",
            "@zertix"
    );
    public List<String> patterns = Arrays.asList(
            "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(:\\d{1,5})?\\b",
            "(?i)(?!.*(zertix|dataforest_net))\\b\\w+\\.(mc|pw|su|fun|net|ru|com|org|gg|pro|online|space|top|xyz|site|club|world|play|mine|craft|io|me|cc|ws|tk|ml|ga|cf|gq)\\b",
            "(?i)(?!.*(zertix|dataforest_net))discord\\.(gg|com/invite)/\\w+",
            "(?i)(?!.*(zertix|dataforest_net))t\\.me/(?!dataforest_net|zertix)\\w+",
            "(?i)(?!.*(zertix|dataforest_net))\\b(f[a4@]nt[i1!]m[e3]|v[i1!]m[e3]w[o0]rld|h[o0]lyw[o0]rld|hyp[i1!]x[e3]l)\\b"
    );
    public List<String> blockedWords = Arrays.asList(
            "открываем новый сервер",
            "новый вайп",
            "выдам донат",
            "заходи на",
            "лучший сервер",
            "funtime",
            "holyworld",
            "vimeworld",
            "hypixel",
            ".mc", ".pw", ".su", ".fun", ".gg", ".net", ".ru", ".com"
    );
}
