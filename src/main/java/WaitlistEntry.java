public final class WaitlistEntry {
    private final String id;
    private final String bookId;
    private final String userId;
    private final boolean priority;
    private final long enqueuedAt;

    public WaitlistEntry(String id, String bookId, String userId, boolean priority, long enqueuedAt) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.priority = priority;
        this.enqueuedAt = enqueuedAt;
    }

    public String getId() { return id; }
    public String getBookId() { return bookId; }
    public String getUserId() { return userId; }
    public boolean isPriority() { return priority; }
    public long getEnqueuedAt() { return enqueuedAt; }
}
