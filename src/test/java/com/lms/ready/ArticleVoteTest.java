package com.lms.ready;

import com.lms.ready.vote.ArticleVote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ArticleVoteTest {

    @Resource
    private ArticleVote articleVote;

    @Test
    public void testVote() {
    }
}
