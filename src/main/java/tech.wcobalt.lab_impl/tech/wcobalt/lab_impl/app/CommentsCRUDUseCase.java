package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Comment;
import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface CommentsCRUDUseCase {
    Comment createComment(FamilyMember author, String content, int entry) throws RightsViolationException;

    void removeComment(FamilyMember whoPerforms, int comment) throws RightsViolationException;

    List<Comment> loadCommentsByEntry(FamilyMember whoPerforms, int entry) throws RightsViolationException;
}
