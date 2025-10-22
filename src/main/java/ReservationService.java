import java.util.List;

public class ReservationService {
private final IBookRepository bookRepo;
private final IReservationRepository reservationRepo;
// New waiting queue warehouse
private final IWaitlistRepository waitlistRepo;

public ReservationService(IBookRepository bookRepo, IReservationRepository reservationRepo) {
    this(bookRepo, reservationRepo, null);
}

// New three-parameter structure
public ReservationService(IBookRepository bookRepo,
                            IReservationRepository reservationRepo,
                            IWaitlistRepository waitlistRepo) {
    this.bookRepo = bookRepo;
    this.reservationRepo = reservationRepo;
    this.waitlistRepo = waitlistRepo;
}

/**
  * Reserve a book for a user.
  * Throws IllegalArgumentException if book not found.
  * Throws IllegalStateException if no copies available or user already reserved.
  */
public void reserve(String userId, String bookId) {

    if (reservationRepo.existsByUserAndBook(userId, bookId)) {
        throw new IllegalStateException("Already reserved");
    }

    Book book = bookRepo.findById(bookId);

    if (book == null) {
        throw new IllegalArgumentException("Book not found");
    }

    if (book.getCopiesAvailable() <= 0) {
        throw new IllegalStateException("No copies available");
    }

    // If the user is already in the waiting queue for the book, clear the waiting entries

    if (waitlistRepo != null && waitlistRepo.exists(bookId, userId)) {
        waitlistRepo.remove(bookId, userId);
    }

    book.setCopiesAvailable(book.getCopiesAvailable() - 1);
    reservationRepo.save(new Reservation(userId, bookId));
}

/*
    Cancel an existing reservation for a user.
    Throws IllegalArgumentException if no such reservation exists.
    
    Should any books be queued for collection, they shall be issued directly to the first user in the queue; consequently, the stock level shall not increase.
    Otherwise, the original rule of adding one to the inventory shall still apply.
  */

public void cancel(String userId, String bookId) {

    if (!reservationRepo.existsByUserAndBook(userId, bookId)) {
        throw new IllegalArgumentException("No such reservation");
    }

    // Delete the original reservation first
    reservationRepo.delete(userId, bookId);

    if (waitlistRepo != null && !waitlistRepo.isEmpty(bookId)) {
        WaitlistEntry next = waitlistRepo.dequeueNext(bookId);
        if (next != null) {
            reservationRepo.save(new Reservation(next.getUserId(), bookId));
            return; // Return directly, keeping copies at 0
        }
    }

    Book book = bookRepo.findById(bookId);
    book.setCopiesAvailable(book.getCopiesAvailable() + 1);
}

/** List all active reservations for a given user. */
public List<Reservation> listReservations(String userId) {
    return reservationRepo.findByUser(userId);
}

/** List all reservations for a book. */
public List<Reservation> listReservationsForBook(String bookId) {
    return reservationRepo.findByBook(bookId);
}

/*  New priority reservation
    Only the behavior of going from out of stock to entering the waiting queue is implemented；
*/

public void reservePriority(String userId, String bookId) {
    Book book = bookRepo.findById(bookId);
    if (book == null) {
        throw new IllegalArgumentException("Book not found");
    }
    if (reservationRepo.existsByUserAndBook(userId, bookId)) {
        throw new IllegalStateException("Already reserved");
    }

    if (book.getCopiesAvailable() > 0) {
        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        reservationRepo.save(new Reservation(userId, bookId));
        return;
    }

    if (waitlistRepo == null) {
        throw new IllegalStateException("No copies available");
    }
    if (waitlistRepo.exists(bookId, userId)) {
        throw new IllegalStateException("Already in waitlist");
    }
    waitlistRepo.enqueue(bookId, userId, true);
}
}