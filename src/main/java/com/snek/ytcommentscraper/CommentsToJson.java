package com.snek.ytcommentscraper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snek.ytcommentscraper.comment.CommentCollector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CommentsToJson {
    private Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private int commentsPerFile = 2000;
    private int maxFiles = 50;

    public CommentsToJson(){ }

    public CommentsToJson(Gson gson){
        this.gson = gson;
    }

    public void setCommentsPerFile(int commentsPerFile) {
        this.commentsPerFile = commentsPerFile;
    }

    public void setMaxFiles(int maxFiles) {
        this.maxFiles = maxFiles;
    }

    public void writeCommentsToFile(String linkToVid, String title, String pathToFolder) throws InterruptedException, IOException {
        if (pathToFolder.isBlank()){
            pathToFolder = "";
        } else {
            pathToFolder += "/";
        }
        verifyFolder(pathToFolder);
        int fileCount = 0;
        int commentCount = 0;

        String path = pathToFolder + title;
        var cc = new CommentCollector(linkToVid);

        while (!cc.isEndOfComments() && fileCount <= maxFiles){
            fileCount++;
            String content = gson.toJson(cc.getNextNComments(commentsPerFile));
            write(path + fileCount + ".json", content);
            System.out.println(fileCount);
        }
    }

    private void verifyFolder(String pathToFolder){
        File file = new File(pathToFolder);

        if (!file.exists()){
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        } else if (file.isDirectory()){
            return;
        }else if (file.isFile()){
            throw new IllegalArgumentException("invalidFolder");
        }
    }

    private void write(String pathName, String content) throws IOException {
        FileWriter writer = new FileWriter(pathName);
        writer.write(content);
        writer.close();
    }
}
