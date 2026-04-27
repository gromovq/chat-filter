package it.gromov.chatfilter.config.section;

import eu.okaeri.configs.OkaeriConfig;

public class MessagesSection extends OkaeriConfig {
    public String blocked = "&cРеклама запрещена на сервере.";
    public String blockedGhostEcho = "&7[ghost] &f%message%";
    public String reloadDone = "&aКонфиг перезагружен.";
    public String noPermission = "&cНедостаточно прав.";
}
