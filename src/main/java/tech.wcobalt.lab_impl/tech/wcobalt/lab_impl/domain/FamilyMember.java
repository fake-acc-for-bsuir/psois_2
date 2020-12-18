package tech.wcobalt.lab_impl.domain;

public class FamilyMember {
    private String nickname, passwordHash;
    private int id;
    private Rights rights;

    private String hash(String string) {
        return string;
    }

    public FamilyMember(String nickname, String password, int id, Rights rights) {
        this.nickname = nickname;
        this.id = id;
        this.rights = rights;

        setPassword(password);
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean checkPassword(String password) {
        return hash(password).equals(passwordHash);
    }

    public void setPassword(String password) {
        this.passwordHash = hash(password);
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Rights getRights() {
        return rights;
    }
}
