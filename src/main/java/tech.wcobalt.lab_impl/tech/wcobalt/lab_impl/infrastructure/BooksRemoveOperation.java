package tech.wcobalt.lab_impl.infrastructure;

import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.persistence.BooksRepository;

public class BooksRemoveOperation implements RemoveOperation {
    private BooksRepository booksRepository;

    public BooksRemoveOperation(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public void removeEntry(Entry entry) {
        booksRepository.removeBook(entry);
    }
}
