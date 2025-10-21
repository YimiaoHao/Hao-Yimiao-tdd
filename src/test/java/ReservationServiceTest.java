import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ReservationServiceTest {
    @Test
    void reserve_success_decrements_and_saves() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();

        books.save(new Book("b1", "Clean Code", 2));

        var service = new ReservationService(books, reservations);

        service.reserve("u1", "b1");

        assertEquals(1, books.findById("b1").getCopiesAvailable());
        assertTrue(reservations.existsByUserAndBook("u1", "b1"));
    }

    @Test
    void reserve_sameUserSameBook_twice_throws() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();

        books.save(new Book("b1", "Clean Code", 2));

        var service = new ReservationService(books, reservations);

        service.reserve("u1", "b1"); 

        assertThrows(IllegalStateException.class, () -> service.reserve("u1", "b1")); 
    }

    @Test
    void reserve_noCopies_throws() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();

        books.save(new Book("b1", "DDD", 0));

        var service = new ReservationService(books, reservations);

        assertThrows(IllegalStateException.class, () -> service.reserve("u1", "b1"));
    }
    
    @Test
    void reserve_bookNotFound_throws() {
        var books = new MemoryBookRepository(); 
        var reservations = new MemoryReservationRepository();

        var service = new ReservationService(books, reservations);

        assertThrows(IllegalArgumentException.class, () -> service.reserve("u1", "bX"));
    }

    @Test
    void cancel_existingReservation_increments_and_deletes() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        books.save(new Book("b1", "Clean Code", 2));
        var service = new ReservationService(books, reservations);

        service.reserve("u1", "b1");
        service.cancel("u1", "b1");

        assertEquals(2, books.findById("b1").getCopiesAvailable());
        assertFalse(reservations.existsByUserAndBook("u1", "b1"));
    }

    @Test
    void cancel_nonExisting_throws() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        books.save(new Book("b1", "Clean Code", 1));
        var service = new ReservationService(books, reservations);

        assertThrows(IllegalArgumentException.class, () -> service.cancel("u1", "b1"));
    }

    @Test
    void listReservations_returnsUserReservations() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        books.save(new Book("b1", "A", 5));
        books.save(new Book("b2", "B", 5));
        var service = new ReservationService(books, reservations);

        service.reserve("u1", "b1");
        service.reserve("u1", "b2");
        service.reserve("u2", "b1"); 

        var list = service.listReservations("u1");
        assertEquals(2, list.size());
    }

    @Test
    void listReservationsForBook_returnsEmptyWhenNone() {
        var s = new ReservationService(new MemoryBookRepository(), new MemoryReservationRepository());
        var list = s.listReservationsForBook("nope");
        assertTrue(list.isEmpty());
    }

    //boundary testing
    @Test
    void reserve_lastCopy_makesZero() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        books.save(new Book("b1", "X", 1));
        var service = new ReservationService(books, reservations);

        service.reserve("u1", "b1");

        assertEquals(0, books.findById("b1").getCopiesAvailable());
        assertTrue(reservations.existsByUserAndBook("u1","b1"));
    }

    @Test
    void reserve_untilDepleted_thenNextThrows() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        books.save(new Book("b1", "X", 2));
        var service = new ReservationService(books, reservations);

        service.reserve("u1", "b1");
        service.reserve("u2", "b1");

        assertEquals(0, books.findById("b1").getCopiesAvailable());
        assertThrows(IllegalStateException.class, () -> service.reserve("u3", "b1"));
    }

    @Test
    void cancel_restoresFromZeroToOne() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        books.save(new Book("b1", "X", 1));
        var service = new ReservationService(books, reservations);

        service.reserve("u1", "b1");
        service.cancel("u1", "b1");

        assertEquals(1, books.findById("b1").getCopiesAvailable());
        assertFalse(reservations.existsByUserAndBook("u1","b1"));
    }

    @Test
    void listReservations_emptyWhenNone() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        books.save(new Book("b1", "X", 1));
        var service = new ReservationService(books, reservations);

        var list = service.listReservations("nobody");
        assertTrue(list.isEmpty());
    }

    @Test
        void listReservationsForBook_returnsAllForThatBook() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        books.save(new Book("b1", "X", 2));
        var service = new ReservationService(books, reservations);

        service.reserve("u1", "b1");
        service.reserve("u2", "b1");

        var list = service.listReservationsForBook("b1");
        assertEquals(2, list.size());
        }







}