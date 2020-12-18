package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Book;
import tech.wcobalt.lab_impl.domain.BookFilter;
import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface BooksCRUDUseCase {
    Book createBook(FamilyMember whoPerforms, Book book) throws RightsViolationException;

    void saveBook(FamilyMember whoPerforms, Book book) throws RightsViolationException;

    Book loadBook(FamilyMember whoPerforms, int book) throws RightsViolationException;

    List<Book> loadBooks(FamilyMember whoPerforms, BookFilter bookFilter) throws RightsViolationException;
}
