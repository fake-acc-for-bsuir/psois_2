package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Comment;
import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.domain.Rights;
import tech.wcobalt.lab_impl.persistence.CommentsRepository;

import java.util.List;

public class CommentsCRUDUseCaseImpl implements CommentsCRUDUseCase {
    private CommentsRepository commentsRepository;

    public CommentsCRUDUseCaseImpl(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    @Override
    public Comment createComment(FamilyMember author, String content, int entry) throws RightsViolationException {
        Comment comment = new Comment(0, entry, author.getId(), content);

        return commentsRepository.createComment(comment);
    }

    @Override
    public void removeComment(FamilyMember whoPerforms, int comment) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            commentsRepository.removeComment(new Comment(comment, 0, 0, ""));
        } else
            throw new RightsViolationException("Non administrator shall remove no comments");
    }

    @Override
    public List<Comment> loadCommentsByEntry(FamilyMember whoPerforms, int entry) throws RightsViolationException {
        return commentsRepository.loadAllCommentsOfEntry(entry);
    }
}
