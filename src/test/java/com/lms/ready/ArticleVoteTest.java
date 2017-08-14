package com.lms.ready;

import com.lms.ready.vote.ArticleVote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ArticleVoteTest {

    @Resource
    private ArticleVote articleVote;

    @Test
    public void testPostArticle() {
        Long userId = 12001L;
        String title = "redis in action";
        String link = "redis.io";
        for (int i = 1; i <= 10; i++) {
            Long articleId = articleVote.postArticle(userId++, title + i, link + i);
            System.out.println(articleId);
        }
    }

    @Test
    public void testVote() {
        Long userId = 12006L;
        Long articleId = 5L;
        boolean voted = articleVote.vote(userId, articleId);
        System.out.println(voted);
    }

    @Test
    public void testGetArticlesByScore() {
        int page = 1;
        String order = "score:";
        List<Map<String, String>> articles = articleVote.getArticles(page, order);
        for (Map<String, String> article : articles) {
            System.out.println(article);
        }
    }

    @Test
    public void testGetArticlesByPostTime() {
        int page = 1;
        String order = "time:";
        List<Map<String, String>> articles = articleVote.getArticles(page, order);
        for (Map<String, String> article : articles) {
            System.out.println(article);
        }
    }

    @Test
    public void testAddToGroups() {
        Long articleId = 7L;
        List<String> groups = new ArrayList();
        groups.add("programming");
        //groups.add("database");
        articleVote.addToGroups(articleId, groups);
    }

    @Test
    public void testRemoveFromGroup() {
        Long articleId = 7L;
        List<String> groups = new ArrayList();
        groups.add("programming");
        articleVote.removeFromGroups(articleId, groups);
    }

    @Test
    public void testGetGroupArticles() {
        String group = "programming";
        int page = 1;
        String order = "score:";
        List<Map<String, String>> articles = articleVote.getGroupArticles(group, page, order);
        for (Map<String, String> article : articles) {
            System.out.println(article);
        }
    }


}
