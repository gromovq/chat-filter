package it.gromov.chatfilter.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.gromov.chatfilter.config.section.DatabaseSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private final JavaPlugin plugin;
    private final DatabaseSection config;
    private HikariDataSource source;

    public DatabaseManager(JavaPlugin plugin, DatabaseSection config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void init() {
        HikariConfig hikari = new HikariConfig();
        String type = config.type.toUpperCase();
        if ("MYSQL".equals(type)) {
            String jdbc = "jdbc:mysql://" + config.mysql.host + ":" + config.mysql.port + "/" + config.mysql.database + "?useSSL=" + config.mysql.useSsl + "&characterEncoding=utf8";
            hikari.setJdbcUrl(jdbc);
            hikari.setUsername(config.mysql.username);
            hikari.setPassword(config.mysql.password);
            hikari.setMaximumPoolSize(10);
        } else {
            File file = new File(plugin.getDataFolder(), config.sqlite.file);
            hikari.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());
            hikari.setMaximumPoolSize(1);
        }
        hikari.setPoolName("zchatfilter-db");
        this.source = new HikariDataSource(hikari);

        try (Connection connection = getConnection(); Statement st = connection.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS violations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "uuid VARCHAR(36) NOT NULL," +
                    "player_name VARCHAR(16) NOT NULL," +
                    "ip VARCHAR(64)," +
                    "message TEXT NOT NULL," +
                    "reason VARCHAR(128) NOT NULL," +
                    "created_at BIGINT NOT NULL)");
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_violations_uuid ON violations(uuid)");
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_violations_created_at ON violations(created_at)");
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to initialize database", ex);
        }
    }

    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    public void close() {
        if (source != null) {
            source.close();
        }
    }
}
