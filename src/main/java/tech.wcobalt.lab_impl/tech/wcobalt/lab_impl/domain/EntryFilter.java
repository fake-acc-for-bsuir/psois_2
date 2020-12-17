package tech.wcobalt.lab_impl.domain;

import java.util.Date;

public class EntryFilter {
    private boolean doSearchByTitle, doSearchByAddDate, doSearchByComment, doSearchByAuthor, doSearchByCategory,
            doSearchByCategoryRecursively, onlyFullTitleMatch, onlyFullCommentMatch, onlyFullAuthorMatch;
    private String titleValue, commentValue, authorValue;
    private Date addDateNotAfter, addDateNotBefore;
    private int categoryForSearch;

    public void setTitleFilter(String value, boolean onlyFullMatch) {
        doSearchByTitle = true;
        titleValue = value;
        onlyFullTitleMatch = onlyFullMatch;
    }

    public void setAddDateFilter(Date notBefore, Date notAfter) {
        doSearchByAddDate = true;
        addDateNotAfter = notAfter;
        addDateNotBefore = notBefore;
    }

    public void setCommentFilter(String value, boolean onlyFullMatch) {
        doSearchByComment = true;
        commentValue = value;
        onlyFullCommentMatch = onlyFullMatch;
    }

    public void setAuthorFilter(String value, boolean onlyFullMatch) {
        doSearchByAuthor = true;
        authorValue = value;
        onlyFullAuthorMatch = onlyFullMatch;
    }

    public void setCategoryFilter(int category, boolean doSearchRecursively) {
        doSearchByCategory = true;
        categoryForSearch = category;
        doSearchByCategoryRecursively = doSearchRecursively;
    }

    public boolean isDoSearchByTitle() {
        return doSearchByTitle;
    }

    public boolean isDoSearchByAddDate() {
        return doSearchByAddDate;
    }

    public boolean isDoSearchByComment() {
        return doSearchByComment;
    }

    public boolean isDoSearchByAuthor() {
        return doSearchByAuthor;
    }

    public boolean isDoSearchByCategory() {
        return doSearchByCategory;
    }

    public boolean isDoSearchByCategoryRecursively() {
        return doSearchByCategoryRecursively;
    }

    public boolean isOnlyFullTitleMatch() {
        return onlyFullTitleMatch;
    }

    public boolean isOnlyFullCommentMatch() {
        return onlyFullCommentMatch;
    }

    public boolean isOnlyFullAuthorMatch() {
        return onlyFullAuthorMatch;
    }

    public String getTitleValue() {
        return titleValue;
    }

    public String getCommentValue() {
        return commentValue;
    }

    public String getAuthorValue() {
        return authorValue;
    }

    public Date getAddDateNotAfter() {
        return addDateNotAfter;
    }

    public Date getAddDateNotBefore() {
        return addDateNotBefore;
    }

    public int getCategoryForSearch() {
        return categoryForSearch;
    }
}
