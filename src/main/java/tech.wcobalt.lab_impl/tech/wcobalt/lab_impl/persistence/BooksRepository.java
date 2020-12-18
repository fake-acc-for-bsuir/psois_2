package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Book;
import tech.wcobalt.lab_impl.domain.BookFilter;
import tech.wcobalt.lab_impl.domain.Entry;

import java.util.List;

public interface BooksRepository {
    Book loadBook(int id);

    List<Book> loadAllBooks();

    Book createBook(Book book);

    void saveBook(Book book);

    void removeBook(Entry book);

    List<Book> loadBooksByFilter(BookFilter bookFilter);
}
