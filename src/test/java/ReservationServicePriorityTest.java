import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ReservationServicePriorityTest {
    //Implement priority user waiting queue and automatic allocation functions
    @Test
    void priority_waits_when_no_copies_and_gets_on_cancel() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();

        books.save(new Book("b1", "X", 1));

        var service = new ReservationService(books, reservations);

        //Ordinary users occupy the only copy first
        service.reserve("u1", "b1");

        assertEquals(0, books.findById("b1").getCopiesAvailable());

        //When priority users make a reservation, there is already no stock. However, no error is thrown at this time, but there will not be a reservation record immediately, but it will be in the waiting queue.
        service.reservePriority("p1", "b1");

        assertFalse(reservations.existsByUserAndBook("p1", "b1"));
        assertEquals(0, books.findById("b1").getCopiesAvailable());

        //Canceling the reservation of an ordinary user will automatically assign this volume to the priority user.
        //(Inventory +1 and immediately -1, still 0)
        service.cancel("u1", "b1");

        assertTrue(reservations.existsByUserAndBook("p1", "b1"));
        assertFalse(reservations.existsByUserAndBook("u1", "b1"));
        assertEquals(0, books.findById("b1").getCopiesAvailable());
    }

    /*
    When implementing the waiting queue in Step 1,
    Already naturally used ArrayDeque + addLast/pollFirst,
    This itself is FIFO
    So when you write the "FIFO" test, it can be run directly.
    */
    @Test
    void priority_waitlist_is_FIFO() {
        var books = new MemoryBookRepository();
        var res   = new MemoryReservationRepository();
        books.save(new Book("b1", "X", 2));
        var s = new ReservationService(books, res);

        s.reserve("u1", "b1");
        s.reserve("u2", "b1");

        s.reservePriority("p1", "b1");
        s.reservePriority("p2", "b1");

        s.cancel("u1", "b1");
        assertTrue(res.existsByUserAndBook("p1", "b1"));
        assertFalse(res.existsByUserAndBook("p2", "b1"));

        s.cancel("u2", "b1");
        assertTrue(res.existsByUserAndBook("p2", "b1"));
    } 



    
}
