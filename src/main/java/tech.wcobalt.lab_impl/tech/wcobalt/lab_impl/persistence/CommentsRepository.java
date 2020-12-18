package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Comment;

import java.util.List;

public interface CommentsRepository {
    List<Comment> loadAllCommentsOfEntry(int entry);

    Comment createComment(Comment comment);

    void saveComment(Comment comment);

    void removeComment(Comment comment);
}
