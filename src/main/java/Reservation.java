//Domain class

public class Reservation {
  private final String userId;
  private final String bookId;

  public Reservation(String userId, String bookId) {
    this.userId = userId;
    this.bookId = bookId;
  }

  public String getUserId() { return userId; }
  public String getBookId() { return bookId; }
}
