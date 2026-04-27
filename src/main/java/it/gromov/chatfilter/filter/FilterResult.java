package it.gromov.chatfilter.filter;

public class FilterResult {
    public static final FilterResult CLEAN = new FilterResult(false, "");

    private final boolean blocked;
    private final String reason;

    public FilterResult(boolean blocked, String reason) {
        this.blocked = blocked;
        this.reason = reason;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getReason() {
        return reason;
    }
}
