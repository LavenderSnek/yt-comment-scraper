package com.snek.ytcommentscraper.comment;


import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public class CommentCollector {
    private final ChromeDriver driver = new ChromeDriver();
    private final CommentParser commentParser = new CommentParser();
    //all the methods here have an imaginary question mark at the end. as in 'i think that's what it does'
    public CommentCollector(String url) throws InterruptedException {
        driver.get(url);
        Thread.sleep(1000);
        driver.executeScript("document.querySelector(\"#merch-shelf\").scrollIntoView();");
        Thread.sleep(4000);
        scrollToLoad();
        waitForComment(10);
        //scrolls to top comment + removes the video
        driver.executeScript("document.querySelector(\"ytd-comment-thread-renderer\").scrollIntoView();"
                             + "document.querySelector(\"#ytd-player\").remove();");
    }

    public ArrayList<Comment> getNextNComments(int commentCount){
        ArrayList<Comment> comments = new ArrayList<>();
        for (int i = 0; i <= commentCount; i++) {
            try {
                comments.add(getComment());
            }catch (TimeoutException e){
                if (isEndOfComments()){
                    break;
                }else {
                    scrollToLoad();
                    comments.add(getComment());
                }
            }
        }
        return comments;
    }

    public Comment getComment(){
        waitForComment(5);

        Boolean hasReplies =
                (Boolean) driver.executeScript
                        ("const c = document.querySelector(\"ytd-comment-thread-renderer\");"
                         + "c.scrollIntoView();"
                         + "c.querySelector(\"#more\").click();"//click on read more
                         + "const r = c.querySelector(\"#more-replies\");"
                         + "if(r !== null){ r.click() }"//checks for replies, if yes clicks 1st expander
                         + "return r !== null;");

        if (hasReplies){
            try {
                loadReplies();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        String commentHtml = (String) driver.executeScript
                ("return document.querySelector(\"ytd-comment-thread-renderer\").innerHTML");

        driver.executeScript("document.querySelector(\"ytd-comment-thread-renderer\").remove()");

        return commentParser.getNeatComment(commentHtml,hasReplies);
    }

    private void loadReplies() throws InterruptedException {
        while (moreRepliesExist()){
            driver.executeScript
                    ("document.querySelector(\"ytd-comment-thread-renderer\")"
                     + ".querySelector(\"#continuation > yt-next-continuation > paper-button\").click();");

            Thread.sleep(1000);
        }

        /*//verify loading completion (i don't know why this hasn't ever actually caught any issues)
        while (true){
            var rc1 = driver.executeScript("document.querySelector(\"#loaded-replies\")");
            Thread.sleep(3000);
            var rc2 = driver.executeScript("document.querySelector(\"#loaded-replies\")");
            if (rc1 == rc2){break;}
            System.out.println("uwu");
        }*/

        //read more on all the replies
        driver.executeScript
                ("var s = document.querySelector(\"#loaded-replies\").querySelectorAll(\"paper-button#more\");"
                 + "for(let i=0;i < s.length;i++){s[i].click()};");

    }

    private void scrollToLoad(){
        driver.executeScript("window.scrollBy(0,250);window.scrollBy(0,-250)");
    }

    public boolean isEndOfComments(){
        return (boolean) driver.executeScript
                ("return document.querySelector(\"#continuations\").childElementCount < 1"
                 + "&& document.querySelector(\"ytd-comment-thread-renderer\") === null");
    }

    private boolean moreRepliesExist(){
        return (long) driver.executeScript
                ("return document.querySelector(\"ytd-comment-thread-renderer\")"
                 + ".querySelector(\"#continuation\").childElementCount;") > 0;
    }

    private void waitForComment(int seconds){
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.presenceOfElementLocated(By.tagName("ytd-comment-thread-renderer")));
    }
}
