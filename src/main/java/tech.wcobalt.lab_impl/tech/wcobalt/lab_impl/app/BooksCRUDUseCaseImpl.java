package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Book;
import tech.wcobalt.lab_impl.domain.BookFilter;
import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.domain.Rights;
import tech.wcobalt.lab_impl.persistence.BooksRepository;
import tech.wcobalt.lab_impl.persistence.EntriesRepository;

import java.util.List;

public class BooksCRUDUseCaseImpl implements BooksCRUDUseCase {
    private EntriesRepository entriesRepository;
    private BooksRepository booksRepository;

    public BooksCRUDUseCaseImpl(EntriesRepository entriesRepository, BooksRepository booksRepository) {
        this.entriesRepository = entriesRepository;
        this.booksRepository = booksRepository;
    }

    @Override
    public Book createBook(FamilyMember whoPerforms, Book book) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            return booksRepository.createBook(book);
        } else
            throw new RightsViolationException("Non administrator shall create no books");
    }

    @Override
    public void saveBook(FamilyMember whoPerforms, Book book) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            booksRepository.saveBook(book);
        } else
            throw new RightsViolationException("Non administrator shall change no books");
    }

    @Override
    public Book loadBook(FamilyMember whoPerforms, int book) throws RightsViolationException {
        return booksRepository.loadBook(book);
    }

    @Override
    public List<Book> loadBooks(FamilyMember whoPerforms, BookFilter bookFilter) throws RightsViolationException {
        return booksRepository.loadBooksByFilter(bookFilter);
    }
}
