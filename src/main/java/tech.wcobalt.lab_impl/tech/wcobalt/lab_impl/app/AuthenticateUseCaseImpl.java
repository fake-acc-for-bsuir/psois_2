package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.domain.Session;
import tech.wcobalt.lab_impl.persistence.FamilyMembersRepository;
import tech.wcobalt.lab_impl.persistence.SessionsRepository;

import java.util.Date;

public class AuthenticateUseCaseImpl implements AuthenticateUseCase {
    private SessionsRepository sessionsRepository;
    private FamilyMembersRepository familyMembersRepository;

    public AuthenticateUseCaseImpl(SessionsRepository sessionsRepository, FamilyMembersRepository familyMembersRepository) {
        this.sessionsRepository = sessionsRepository;
        this.familyMembersRepository = familyMembersRepository;
    }

    @Override
    public Session authenticateBySessionToken(String token) {
        return sessionsRepository.loadSession(token);
    }

    @Override
    public Session authenticateByPair(String nickname, String password) {
        FamilyMember familyMember = familyMembersRepository.loadFamilyMember(nickname);

        if (familyMember != null && familyMember.checkPassword(password)) {
            //create new session
            Session session = new Session(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30), familyMember.getId());

            sessionsRepository.createSession(session);

            return session;
        }

        return null;
    }
}
