package it.gromov.chatfilter.config.section;

import eu.okaeri.configs.OkaeriConfig;

public class DatabaseSection extends OkaeriConfig {
    public String type = "SQLITE";
    public Sqlite sqlite = new Sqlite();
    public Mysql mysql = new Mysql();

    public static class Sqlite extends OkaeriConfig {
        public String file = "data.db";
    }

    public static class Mysql extends OkaeriConfig {
        public String host = "127.0.0.1";
        public int port = 3306;
        public String database = "chatfilter";
        public String username = "root";
        public String password = "password";
        public boolean useSsl = false;
    }
}
