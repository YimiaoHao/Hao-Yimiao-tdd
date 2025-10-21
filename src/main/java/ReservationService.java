import java.util.List;

public class ReservationService {
  //新增的等待队列字段
  private final java.util.Map<String, java.util.Deque<String>> waitlist = new java.util.HashMap<>();

  private final IBookRepository bookRepo;
  private final IReservationRepository reservationRepo;

  public ReservationService(IBookRepository bookRepo, IReservationRepository reservationRepo) {
    this.bookRepo = bookRepo;
    this.reservationRepo = reservationRepo;
  }

  /**
   * Reserve a book for a user.
   * Throws IllegalArgumentException if book not found.
   * Throws IllegalStateException if no copies available or user already reserved.
   */
  public void reserve(String userId, String bookId) {
    reserveInternal(userId, bookId, false);
  }

    //新增的优先用户预约公开方法
  public void reservePriority(String userId, String bookId) {
    reserveInternal(userId, bookId, true);
  } 

  /**
   * Cancel an existing reservation for a user.
   * Throws IllegalArgumentException if no such reservation exists.
   */
  public void cancel(String userId, String bookId) {

    if (!reservationRepo.existsByUserAndBook(userId, bookId)) {
      throw new IllegalArgumentException("No such reservation");
    }

    Book book = bookRepo.findById(bookId);
    book.setCopiesAvailable(book.getCopiesAvailable() + 1);
    reservationRepo.delete(userId, bookId);

    allocateFromWaitlist(bookId);
  }

  // 新增的私有内核方法
  private void reserveInternal(String userId, String bookId, boolean priority) {
    // ① 检查是否重复预约（无论普通/优先用户）
    if (reservationRepo.existsByUserAndBook(userId, bookId)) {
        throw new IllegalStateException("Already reserved");
    }

    // ② 检查图书是否存在
    Book book = bookRepo.findById(bookId);
    if (book == null) {
        throw new IllegalArgumentException("Book not found");
    }

    // ③ 有库存时：直接预约（扣库存+保存记录）
    if (book.getCopiesAvailable() > 0) {
        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        reservationRepo.save(new Reservation(userId, bookId));
        return;
    }

    // ④ 无库存时：优先用户入队，普通用户抛异常
    if (priority) {
        // 为图书ID创建队列（如果不存在），并添加用户到队尾
        waitlist.computeIfAbsent(bookId, k -> new java.util.ArrayDeque<>()).addLast(userId);
    } else {
        throw new IllegalStateException("No copies available");
    }

  }

  // 新增：私有分配方法（放在这里，cancel方法之后）
  private void allocateFromWaitlist(String bookId) {
    // ① 检查该图书是否有等待队列，且队列不为空
    java.util.Deque<String> queue = waitlist.get(bookId);
    if (queue == null || queue.isEmpty()) {
        return; // 没有等待用户，直接返回
    }

    // ② 检查图书是否存在，且有可分配的库存（取消后库存应为1）
    Book book = bookRepo.findById(bookId);
    if (book == null || book.getCopiesAvailable() <= 0) {
        return; // 图书不存在或库存不足，无法分配
    }

    // ③ 从队列头部取第一个用户（FIFO）
    String nextUserId = queue.pollFirst();

    // ④ 为该用户完成预约（扣库存+保存记录）
    book.setCopiesAvailable(book.getCopiesAvailable() - 1);
    reservationRepo.save(new Reservation(nextUserId, bookId));

    // ⑤ 若队列空了，移除该图书的队列（节省内存）
    if (queue.isEmpty()) {
        waitlist.remove(bookId);
    }
  } 



  /** List all active reservations for a given user. */
  public List<Reservation> listReservations(String userId) {
    return reservationRepo.findByUser(userId);
  }

  /** List all reservations for a book. */
  public List<Reservation> listReservationsForBook(String bookId) {
    return reservationRepo.findByBook(bookId);
  }
}
