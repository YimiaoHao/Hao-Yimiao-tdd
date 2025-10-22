import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ReservationServicePriorityTest {

    @Test
    void priorityUserEnqueuedWhenNoCopies() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        var waitlist = new MemoryWaitlistRepository();

        var service = new ReservationService(books, reservations, waitlist);

        books.save(new Book("b1", "X", 0)); // Out of stock

        service.reservePriority("uP", "b1"); // Expect to enter the waiting queue

        assertFalse(reservations.existsByUserAndBook("uP", "b1"));
        assertEquals(1, waitlist.size("b1"));
    }
}