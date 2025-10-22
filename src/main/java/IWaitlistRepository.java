import java.util.List;

public interface IWaitlistRepository {
    void enqueue(String bookId, String userId, boolean priority);
    WaitlistEntry dequeueNext(String bookId);   //Dequeue first
    boolean remove(String bookId, String userId);   //Remove the user from the queue
    boolean exists(String bookId, String userId);   //Is it still in the queue?
    boolean isEmpty(String bookId);     //Is the queue empty?
    int size(String bookId);    //queue size
    List<WaitlistEntry> listByBook(String bookId);
}
