//Book warehouse interface
public interface IBookRepository {
  void save(Book book);
  Book findById(String id);
}

