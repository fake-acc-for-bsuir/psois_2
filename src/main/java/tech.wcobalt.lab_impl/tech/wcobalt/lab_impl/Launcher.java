package tech.wcobalt.lab_impl;

import tech.wcobalt.lab_impl.app.*;
import tech.wcobalt.lab_impl.domain.Book;
import tech.wcobalt.lab_impl.domain.CDAudio;
import tech.wcobalt.lab_impl.domain.CDVideo;
import tech.wcobalt.lab_impl.domain.Category;
import tech.wcobalt.lab_impl.infrastructure.*;
import tech.wcobalt.lab_impl.persistence.*;
import tech.wcobalt.lab_impl.ui.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Launcher {
    public static void main(String... args) {
        SessionsRepository sessionsRepository = new SessionsRepositoryImpl(new ArrayList<>());
        FamilyMembersRepository familyMembersRepository = new FamilyMembersRepositoryImpl(new ArrayList<>());
        CategoriesRepository categoriesRepository = new CategoriesRepositoryImpl(new ArrayList<>());
        categoriesRepository.createCategory(new Category(3, 10000, "asdad"));
        EntriesRepository entriesRepository = new EntriesRepositoryImpl(categoriesRepository, new ArrayList<>());
        CommentsRepository commentsRepository = new CommentsRepositoryImpl(new ArrayList<>());

        BooksRepository booksRepository = new BooksRepositoryImpl(categoriesRepository, entriesRepository, new ArrayList<>());
        CDAudiosRepository cdAudiosRepository = new CDAudiosRepositoryImpl(categoriesRepository, entriesRepository, new ArrayList<>());
        CDVideosRepository cdVideosRepository = new CDVideosRepositoryImpl(categoriesRepository, entriesRepository, new ArrayList<>());

        RemoveOperation booksRemoveOperation = new BooksRemoveOperation(booksRepository);
        RemoveOperation cdAudiosRemoveOperation = new CDAudiosRemoveOperation(cdAudiosRepository);
        RemoveOperation cdVideosRemoveOperation = new CDVideosRemoveOperation(cdVideosRepository);

        RemoveOperationsManager removeOperationsManager = new RemoveOperationsManager();
        removeOperationsManager.addRemoveOperation(Book.TYPE_ID, booksRemoveOperation);
        removeOperationsManager.addRemoveOperation(CDAudio.TYPE_ID, cdAudiosRemoveOperation);
        removeOperationsManager.addRemoveOperation(CDVideo.TYPE_ID, cdVideosRemoveOperation);

        SessionSerializer sessionSerializer = new SessionSerializerImpl();

        AuthenticateUseCase authenticateUseCase = new AuthenticateUseCaseImpl(sessionsRepository, familyMembersRepository);
        CategoriesCRUDUseCase categoriesCRUDUseCase = new CategoriesCRUDUseCaseImpl(categoriesRepository);
        EntriesCRUDUseCase entriesCRUDUseCase = new EntriesCRUDUseCaseImpl(removeOperationsManager, entriesRepository);
        BooksCRUDUseCase booksCRUDUseCase = new BooksCRUDUseCaseImpl(entriesRepository, booksRepository);
        CDAudiosCRUDUseCase cdAudiosCRUDUseCase = new CDAudiosCRUDUseCaseImpl(entriesRepository, cdAudiosRepository);
        CDVideosCRUDUseCase cdVideosCRUDUseCase = new CDVideosCRUDUseCaseImpl(entriesRepository, cdVideosRepository);
        ChangeMyPasswordUseCase changeMyPasswordUseCase = new ChangeMyPasswordUseCaseImpl(familyMembersRepository);
        CommentsCRUDUseCase commentsCRUDUseCase = new CommentsCRUDUseCaseImpl(commentsRepository);
        CreateFirstFamilyAdministratorUseCase createFirstFamilyAdministratorUseCase = new CreateFirstFamilyAdministratorUseCaseImpl(familyMembersRepository);
        FamilyMembersCRUDUseCase familyMembersCRUDUseCase = new FamilyMembersCRUDUseCaseImpl(familyMembersRepository);
        SaveLoadSessionsUseCase saveLoadSessionsUseCase = new SaveLoadSessionsUseCaseImpl(sessionSerializer);
        ViewCategoryUseCase viewCategoryUseCase = new ViewCategoryUseCaseImpl(categoriesRepository, entriesRepository);

        UI ui = new UI(authenticateUseCase, categoriesCRUDUseCase, entriesCRUDUseCase, booksCRUDUseCase,
                cdAudiosCRUDUseCase, cdVideosCRUDUseCase, changeMyPasswordUseCase, commentsCRUDUseCase,
                createFirstFamilyAdministratorUseCase, familyMembersCRUDUseCase, saveLoadSessionsUseCase,
                viewCategoryUseCase);

        ui.startTheSystem();
    }
}
