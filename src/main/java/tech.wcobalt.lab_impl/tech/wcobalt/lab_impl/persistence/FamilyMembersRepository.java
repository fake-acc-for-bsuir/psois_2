package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface FamilyMembersRepository {
    FamilyMember loadFamilyMember(int id);

    List<FamilyMember> loadAllFamilyMembers();

    FamilyMember createFamilyMember(FamilyMember familyMember);

    void saveFamilyMember(FamilyMember familyMember);

    void removeFamilyMember(FamilyMember familyMember);

    FamilyMember loadFamilyMember(String nickname);
}
