package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Book;
import tech.wcobalt.lab_impl.domain.BookFilter;
import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.domain.EntryFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BooksRepositoryImpl implements BooksRepository {
    private CategoriesRepository categoriesRepository;
    private EntriesRepository entriesRepository;
    private List<Book> books;

    public BooksRepositoryImpl(CategoriesRepository categoriesRepository, EntriesRepository entriesRepository,
                               List<Book> books) {
        this.categoriesRepository = categoriesRepository;
        this.entriesRepository = entriesRepository;
        this.books = books;
    }

    private Book loadBook(Book book) {
        Book copy = copyBook(book);

        copyEntryAttributesToBook(entriesRepository.loadEntry(book.getId()), copy);

        return copy;
    }

    @Override
    public Book loadBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return loadBook(book);
            }
        }

        return null;
    }

    @Override
    public List<Book> loadAllBooks() {
        List<Book> copy = new ArrayList<>();
        
        for (Book book : books)
            copy.add(loadBook(book));
        
        return copy;
    }

    @Override
    public Book createBook(Book book) {
        Entry entry = new Entry(new Date(), book.getComment(), book.getAuthor(), -1, book.getCategory(), book.getTypeId(), book.getTitle());
        entry = entriesRepository.createEntry(entry);

        Book copy = copyBook(book);
        copy.setId(entry.getId());

        books.add(copy);

        return copyBook(copy);
    }

    @Override
    public void saveBook(Book book) {
        removeBook(book);

        books.add(copyBook(book));

        Entry bookEntry = entriesRepository.loadEntry(book.getId());

        bookEntry.setCategory(book.getCategory());
        bookEntry.setComment(book.getComment());
        bookEntry.setTitle(book.getTitle());

        entriesRepository.saveEntry(bookEntry);
    }

    @Override
    public void removeBook(Entry book) {
        entriesRepository.removeEntry(book);

        books.removeIf(b -> b.getId() == book.getId());
    }

    @Override
    public List<Book> loadBooksByFilter(BookFilter bookFilter) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (checkEntryFilter(book, bookFilter) &&
                    checkTextFilterMatch(book.getPublisher(), bookFilter.getPublisherValue(), bookFilter.isOnlyFullPublisherMatch(), bookFilter.isDoSearchByPublisher()) &&
                    checkDate(book.getPublishingYear(), bookFilter.getPublishingYearNotAfter(), bookFilter.getPublishingYearNotBefore(), bookFilter.isDoSearchByPublishingYear()))
                result.add(copyBook(book));
        }

        return result;
    }

    private Book copyBook(Book book) {
        return new Book(new Date(book.getAddDate().getTime()), book.getComment(), book.getAuthor(), book.getId(),
                book.getCategory(), book.getTitle(), new Date(book.getPublishingYear().getTime()),
                book.getPublisher(), book.getCoverUrl());
    }

    private void copyEntryAttributesToBook(Entry entry, Book book) {
        book.setCategory(entry.getCategory());
        book.setComment(entry.getComment());
        book.setId(entry.getId());
        book.setTitle(entry.getTitle());
    }

    //i really don't care
    boolean checkEntryFilter(Entry entry, EntryFilter entryFilter) {
        return checkDate(entry.getAddDate(), entryFilter.getAddDateNotAfter(), entryFilter.getAddDateNotBefore(), entryFilter.isDoSearchByAddDate()) &&
                checkTextFilterMatch(entry.getAuthor(), entryFilter.getAuthorValue(), entryFilter.isOnlyFullAuthorMatch(), entryFilter.isDoSearchByAuthor()) &&
                checkTextFilterMatch(entry.getComment(), entryFilter.getCommentValue(), entryFilter.isOnlyFullCommentMatch(), entryFilter.isDoSearchByComment()) &&
                checkTextFilterMatch(entry.getTitle(), entryFilter.getTitleValue(), entryFilter.isOnlyFullTitleMatch(), entryFilter.isDoSearchByTitle()) &&
                (!entryFilter.isDoSearchByCategory()
                        || categoriesRepository.loadChildrenCategories(entryFilter.getCategoryForSearch(),
                        entryFilter.isDoSearchByCategoryRecursively()).stream().anyMatch(c -> entry.getCategory() == c.getId()));
    }

    boolean checkTextFilterMatch(String original, String value, boolean onlyFullMatch, boolean doSearch) {
        return !doSearch || ((onlyFullMatch && original.contains(value.toLowerCase())) || (!onlyFullMatch && original.toLowerCase().equals(value.toLowerCase())));
    }

    boolean checkDate(Date original, Date notAfter, Date notBefore, boolean doSearch) {
        return !doSearch || (original.before(notAfter) && original.after(notBefore));
    }
}
