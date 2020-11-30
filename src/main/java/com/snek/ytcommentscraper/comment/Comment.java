package com.snek.ytcommentscraper.comment;

import java.util.ArrayList;

public class Comment {
    private final String username;
    private final String userLink;
    private String profilePicLink;
    private final String contentHtml;
    private final String commentLink;
    private final String likes;
    private final String timeAgo;
    private boolean isHearted;
    private ArrayList<Comment> replies = null;

    public Comment
            (String username, String userLink, String contentHtml,
             String commentLink, String likes, String timeAgo, boolean isHearted) {
        this.username = username;
        this.userLink = userLink;
        this.contentHtml = contentHtml;
        this.commentLink = commentLink;
        this.likes = likes;
        this.timeAgo = timeAgo;
        this.isHearted = isHearted;
    }

    public void setProfilePicLink(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    public void setReplies(ArrayList<Comment> replies) {
        this.replies = replies;
    }
}
