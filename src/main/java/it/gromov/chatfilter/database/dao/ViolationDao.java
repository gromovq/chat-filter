package it.gromov.chatfilter.database.dao;

import it.gromov.chatfilter.database.DatabaseManager;
import it.gromov.chatfilter.database.model.StatsSnapshot;
import it.gromov.chatfilter.database.model.ViolationRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViolationDao {
    private final DatabaseManager databaseManager;

    public ViolationDao(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void insert(ViolationRecord record) {
        String sql = "INSERT INTO violations(uuid, player_name, ip, message, reason, created_at) VALUES (?,?,?,?,?,?)";
        try (Connection c = databaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, record.uuid);
            ps.setString(2, record.playerName);
            ps.setString(3, record.ip);
            ps.setString(4, record.message);
            ps.setString(5, record.reason);
            ps.setLong(6, record.createdAt);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public StatsSnapshot getStats(long dayStart, long weekStart) {
        StatsSnapshot snapshot = new StatsSnapshot();
        snapshot.uniqueToday = count("COUNT(DISTINCT uuid)", "created_at >= ?", dayStart);
        snapshot.uniqueWeek = count("COUNT(DISTINCT uuid)", "created_at >= ?", weekStart);
        snapshot.uniqueTotal = count("COUNT(DISTINCT uuid)", "1=1", null);
        snapshot.violationsToday = count("COUNT(*)", "created_at >= ?", dayStart);
        snapshot.violationsWeek = count("COUNT(*)", "created_at >= ?", weekStart);
        snapshot.violationsTotal = count("COUNT(*)", "1=1", null);
        return snapshot;
    }

    public int countByUuid(String uuid) {
        return count("COUNT(*)", "uuid = ?", uuid);
    }

    public List<ViolationRecord> getByPlayer(String player, int limit, int offset) {
        String sql = "SELECT * FROM violations WHERE LOWER(player_name)=LOWER(?) ORDER BY id DESC LIMIT ? OFFSET ?";
        List<ViolationRecord> list = new ArrayList<>();
        try (Connection c = databaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, player);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(read(rs));
                }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return list;
    }

    public int countByPlayer(String player) {
        return count("COUNT(*)", "LOWER(player_name)=LOWER(?)", player);
    }

    private int count(String agg, String where, Object param) {
        String sql = "SELECT " + agg + " FROM violations WHERE " + where;
        try (Connection c = databaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (param != null) {
                if (param instanceof Long) ps.setLong(1, (Long) param);
                else ps.setString(1, String.valueOf(param));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private ViolationRecord read(ResultSet rs) throws SQLException {
        ViolationRecord r = new ViolationRecord();
        r.id = rs.getLong("id");
        r.uuid = rs.getString("uuid");
        r.playerName = rs.getString("player_name");
        r.ip = rs.getString("ip");
        r.message = rs.getString("message");
        r.reason = rs.getString("reason");
        r.createdAt = rs.getLong("created_at");
        return r;
    }
}
