import java.util.*;
import java.util.UUID;

public class MemoryWaitlistRepository implements IWaitlistRepository {
    private final Map<String, Deque<WaitlistEntry>> q = new HashMap<>();

    @Override
    public void enqueue(String bookId, String userId, boolean priority) {
        if (exists(bookId, userId)) return;
        q.computeIfAbsent(bookId, k -> new ArrayDeque<>())
        .addLast(new WaitlistEntry(UUID.randomUUID().toString(), bookId, userId, priority, System.nanoTime()));
    }

    @Override
    public WaitlistEntry dequeueNext(String bookId) {
        Deque<WaitlistEntry> d = q.get(bookId);
        if (d == null) return null;
        WaitlistEntry e = d.pollFirst();
        if (d.isEmpty()) q.remove(bookId);
        return e;
    }

    @Override
    public boolean remove(String bookId, String userId) {
        Deque<WaitlistEntry> d = q.get(bookId);
        if (d == null) return false;
        boolean ok = d.removeIf(w -> w.getUserId().equals(userId));
        if (d.isEmpty()) q.remove(bookId);
        return ok;
    }

    @Override public boolean exists(String bookId, String userId) {
        Deque<WaitlistEntry> d = q.get(bookId);
        return d != null && d.stream().anyMatch(w -> w.getUserId().equals(userId));
    }
    @Override public boolean isEmpty(String bookId) { return size(bookId) == 0; }
    @Override public int size(String bookId) {
        Deque<WaitlistEntry> d = q.get(bookId);
        return d == null ? 0 : d.size();
    }
    @Override public List<WaitlistEntry> listByBook(String bookId) {
        Deque<WaitlistEntry> d = q.get(bookId);
        return d == null ? List.of() : List.copyOf(d);
    }
}
