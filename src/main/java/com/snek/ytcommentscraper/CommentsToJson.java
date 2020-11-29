package com.snek.ytcommentscraper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snek.ytcommentscraper.comment.Comment;
import com.snek.ytcommentscraper.comment.CommentCollector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CommentsToJson {
    private Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private int commentsPerFile = 6000;

    public CommentsToJson(){ }

    public CommentsToJson(Gson gson){
        this.gson = gson;
    }

    public void setCommentsPerFile(int commentsPerFile) {
        this.commentsPerFile = commentsPerFile;
    }

    public void writeCommentsToFile(String linkToVid, String title, boolean ignoreErrors) throws InterruptedException, IOException {
        int fileCount = 0;
        var cc = new CommentCollector(linkToVid);

        while (!cc.isEndOfComments()){
            fileCount++;
            ArrayList<Comment> comments = cc.getNextNComments(commentsPerFile, ignoreErrors);

            if (comments.isEmpty()){
                System.out.println("\n\n\n\nSomething went pretty wrong but the file was written anyway");
                System.exit(1);
            }

            String content = gson.toJson(comments);
            write(title + fileCount + ".json", content);
            System.out.println(fileCount);
        }
    }

    private void write(String pathName, String content) throws IOException {
        FileWriter writer = new FileWriter(pathName);
        writer.write(content);
        writer.close();
    }
}
