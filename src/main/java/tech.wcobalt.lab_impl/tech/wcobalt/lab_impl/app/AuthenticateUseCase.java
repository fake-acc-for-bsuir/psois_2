package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Session;

public interface AuthenticateUseCase {
    Session authenticateBySessionToken(String token);

    Session authenticateByPair(String nickname, String password);
}
