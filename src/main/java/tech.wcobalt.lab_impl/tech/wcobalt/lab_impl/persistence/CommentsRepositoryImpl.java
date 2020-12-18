package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsRepositoryImpl implements CommentsRepository {
    private List<Comment> comments;
    private int nextId = 10000;

    public CommentsRepositoryImpl(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public List<Comment> loadAllCommentsOfEntry(int entry) {
        List<Comment> list = new ArrayList<>();

        for (Comment comment : comments) {
            if (comment.getEntry() == entry)
                list.add(copyComment(comment));
        }

        return list;
    }

    @Override
    public Comment createComment(Comment comment) {
        comment.setId(nextId++);

        comments.add(copyComment(comment));

        return copyComment(comment);
    }

    @Override
    public void saveComment(Comment comment) {
        removeComment(comment);

        comments.add(copyComment(comment));
    }

    @Override
    public void removeComment(Comment comment) {
        comments.removeIf(c -> c.getId() == comment.getId());
    }

    private Comment copyComment(Comment comment) {
        return new Comment(comment.getId(), comment.getEntry(), comment.getAuthor(), comment.getContent());
    }
}
