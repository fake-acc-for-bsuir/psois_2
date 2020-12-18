package tech.wcobalt.lab_impl.domain;

import java.util.Date;
import java.util.Random;

public class Session {
    private String token;
    private Date sessionIsValidUntil;
    private int familyMember;
    private static final int TOKEN_SIZE = 32;

    private static String generateToken(long seed) {
        Random random = new Random(seed);

        String token = "";

        for (int i = 0; i < TOKEN_SIZE; ++i)
            token += (char)('a' + (Math.abs(random.nextInt()) % 6));

        return token;
    }

    public Session(Date sessionIsValidUntil, int familyMember) {
        this(generateToken(System.currentTimeMillis()), sessionIsValidUntil, familyMember);
    }

    public Session(String token, Date sessionIsValidUntil, int familyMember) {
        this.token = token;
        this.sessionIsValidUntil = sessionIsValidUntil;
        this.familyMember = familyMember;
    }

    public String getToken() {
        return token;
    }

    public Date getSessionIsValidUntil() {
        return sessionIsValidUntil;
    }

    public boolean isSessionValidNow() {
        return sessionIsValidUntil.before(new Date());
    }

    public int getFamilyMember() {
        return familyMember;
    }
}
