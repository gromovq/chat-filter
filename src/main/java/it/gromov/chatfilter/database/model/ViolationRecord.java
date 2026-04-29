package it.gromov.chatfilter.database.model;

public class ViolationRecord {
    public long id;
    public String uuid;
    public String playerName;
    public String ip;
    public String message;
    public String reason;
    public long createdAt;
}
