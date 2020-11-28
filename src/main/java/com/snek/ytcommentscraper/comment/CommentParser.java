package com.snek.ytcommentscraper.comment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class CommentParser {

    public Comment getNeatComment(String commentHtml, boolean hasReplies){
        Document comDoc = Jsoup.parseBodyFragment(commentHtml);
        Element cb = comDoc.getElementById("body");

        Comment com = parseComment(cb);

        com.setProfilePicLink(cb.selectFirst("#author-thumbnail > a")
                .getElementById("img").attr("src"));

        com.setHearted(cb.getElementById("creator-heart").childrenSize() > 0);

        if (hasReplies){
            com.setReplies(getParsedReplies(comDoc.getElementById("loaded-replies")));
        }
        return com;
    }

    private Comment parseComment(Element mainBody){
        Element user = mainBody.getElementById("author-text");
        String userName = user.text();
        String userLink = user.attr("href");

        String content = mainBody.getElementById("content-text").html();
        String likes = mainBody.getElementById("vote-count-middle").text();

        Element top = mainBody.selectFirst("#header-author > yt-formatted-string > a");
        String timeAgo = top.text();
        String commentLink = top.attr("href");

        boolean isHearted = mainBody.getElementById("creator-heart").childrenSize() > 0;

        return new Comment(userName, userLink, content, commentLink, likes, timeAgo);
    }

    private ArrayList<Comment> getParsedReplies(Element replySection){
        Elements replyBodies = replySection.select("#body");
        ArrayList<Comment> parsedReplies = new ArrayList<>();

        for (Element replyBody : replyBodies){
            parsedReplies.add(parseComment(replyBody));
        }
        return parsedReplies;
    }


}
