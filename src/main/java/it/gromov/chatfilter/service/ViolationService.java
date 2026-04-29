package it.gromov.chatfilter.service;

import it.gromov.chatfilter.bootstrap.ChatFilterPlugin;
import it.gromov.chatfilter.database.DatabaseManager;
import it.gromov.chatfilter.database.dao.ViolationDao;
import it.gromov.chatfilter.database.model.StatsSnapshot;
import it.gromov.chatfilter.database.model.ViolationRecord;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class ViolationService {
    private final ChatFilterPlugin plugin;
    private final ViolationDao dao;

    public ViolationService(ChatFilterPlugin plugin, DatabaseManager manager) {
        this.plugin = plugin;
        this.dao = new ViolationDao(manager);
    }

    public void record(Player player, String message, String reason) {
        ViolationRecord record = new ViolationRecord();
        record.uuid = player.getUniqueId().toString();
        record.playerName = player.getName();
        record.ip = player.getAddress() == null ? "unknown" : player.getAddress().getAddress().getHostAddress();
        record.message = message;
        record.reason = reason;
        record.createdAt = Instant.now().getEpochSecond();
        dao.insert(record);
        plugin.getTelegramNotifier().notifyViolation(record);
    }

    public StatsSnapshot summary() {
        long dayStart = LocalDate.now(ZoneOffset.UTC).atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        long weekStart = LocalDate.now(ZoneOffset.UTC).minusDays(6).atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        return dao.getStats(dayStart, weekStart);
    }

    public int violationsByUuid(String uuid) {
        return dao.countByUuid(uuid);
    }

    public int countByPlayer(String player) {
        return dao.countByPlayer(player);
    }

    public List<ViolationRecord> byPlayer(String player, int limit, int offset) {
        return dao.getByPlayer(player, limit, offset);
    }
}
