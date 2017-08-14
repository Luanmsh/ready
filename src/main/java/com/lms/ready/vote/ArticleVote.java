package com.lms.ready.vote;

import com.lms.ready.constant.VoteConstants;
import com.lms.ready.util.DateUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>文件描述: 文章投票</p>
 *
 * @Author luanmousheng
 * @Date 17/8/13 下午4:05
*/
@Component
public class ArticleVote {

    @Resource
    private JedisPool jedisPool;

    /**
     * 文章投票
     * @param userId 用户id
     * @param articleId 文章id
     * @return true=投票成功，false=投票失败
     */
    public boolean vote(Long userId, Long articleId) {
        Jedis jedis = jedisPool.getResource();
        try {
            Double nowSeconds = DateUtils.currentTimeSeconds();
            Double postTime = jedis.zscore("time:", "article:" + articleId);
            if (Double.compare(postTime + VoteConstants.ONE_WEEK_IN_SECONDS, nowSeconds) < 0) {
                //文章发布时间超过一个星期，不能投票
                return false;
            }
            long addRes = jedis.sadd("voted:" + articleId, "user:" + userId);
            if (addRes == 1) {
                //投票成功，增加评分
                jedis.zincrby("score:", VoteConstants.VOTE_SCORE, "article:" + articleId);
                //文章资料的投票数加1
                jedis.hincrBy("article:" + articleId, "votes", 1);
                return true;
            }
            return false;
        } finally {
            returnJedis(jedis);
        }
    }

    /**
     * 发布文章
     * @param userId
     * @param title
     * @param link
     * @return 文章id
     */
    public Long postArticle(Long userId, String title, String link) {
        Jedis jedis = jedisPool.getResource();
        try {
            //文章id递增
            long articleId = jedis.incr("article:");
            String votedKey = "voted:" + articleId;
            //投票记录
            jedis.sadd(votedKey, "user:" + userId);
            jedis.expire(votedKey, VoteConstants.ONE_WEEK_IN_SECONDS);
            String articleKey = "article:" + articleId;
            //文章信息放在哈希表
            Map<String, String> articleMap = new HashMap();
            double now = DateUtils.currentTimeSeconds();
            articleMap.put("title", title);
            articleMap.put("link", link);
            articleMap.put("poster", "user:" + userId);
            articleMap.put("time", String.valueOf(now));
            articleMap.put("votes", "1");
            jedis.hmset(articleKey, articleMap);
            //根据评分排序
            jedis.zadd("score", now + VoteConstants.VOTE_SCORE, articleKey);
            //根据发布时间排序
            jedis.zadd("time:", now, articleKey);
            return articleId;
        } finally {
            returnJedis(jedis);
        }
    }

    /**
     * 取评分最高或者最新发布的文章
     * @param page
     * @param order "score:"或者"time:"
     * @return 文章列表，每个文章都是一个哈希表
     */
    public List<Map<String, String>> getArticles(Integer page, String order) {
        Jedis jedis = jedisPool.getResource();
        try {
            List<Map<String, String>> res = new ArrayList();
            if (!"score:".equals(order) && !"time:".equals(order) || page == null || page < 1) {
                return res;
            }
            int start = (page - 1) * VoteConstants.ARTICLES_PER_PAGE;
            int end = start + VoteConstants.ARTICLES_PER_PAGE - 1;
            Set<String> idSets = jedis.zrevrange(order, start, end);
            Iterator<String> it = idSets.iterator();
            while (it.hasNext()) {
                String id = it.next();
                //将该文章信息都取出来
                Map<String, String> articleMap = jedis.hgetAll(id);
                articleMap.put("id", id);
                res.add(articleMap);
            }
            return res;
        } finally {
            returnJedis(jedis);
        }
    }

    private void returnJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
