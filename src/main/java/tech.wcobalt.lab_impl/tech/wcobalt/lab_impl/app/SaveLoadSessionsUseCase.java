package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Session;

public interface SaveLoadSessionsUseCase {
    Session loadSession();

    void saveSession(Session session);

    void removeSession();
}
