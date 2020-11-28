package com.snek.ytcommentscraper;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        String h = "usage: ytcs [title] [VideoLink]";

        if ( args.length < 2 || !args[1].contains("youtu") || args[0].isBlank()){
            System.err.println("invalid args\n" + h);
            System.exit(64);
        } else {
            var cj = new CommentsToJson();
            cj.writeCommentsToFile(args[1],args[0],"");
        }
    }
}
