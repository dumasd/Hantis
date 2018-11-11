package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.DefaultNameHandler;
import com.thinkerwolf.hantis.example.Blog;
import com.thinkerwolf.hantis.orm.TableEntity;
import org.junit.Test;

import java.util.Date;

public class OrmTest {

    @Test
    public void tableEntity() {
        TableEntity<?> tableEntity = new TableEntity<>(Blog.class, new DefaultNameHandler());
        Blog blog = new Blog();
        blog.setId(1000);
        blog.setTitle("title");
        blog.setContent("content");
        blog.setUserId(123);
        blog.setCreateTime(new Date());
        System.out.println(tableEntity.parseInsertSql(blog));

        System.out.println(tableEntity.parseDeleteSql(blog));

        System.out.println(tableEntity.parseUpdateSql(blog));

        System.out.println(tableEntity.parseSelectSql(blog));

    }


}
