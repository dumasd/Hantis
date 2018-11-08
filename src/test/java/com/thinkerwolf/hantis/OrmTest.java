package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.DefaultNameHandler;
import com.thinkerwolf.hantis.example.Blog;
import com.thinkerwolf.hantis.orm.TableEntity;
import org.junit.Test;

public class OrmTest {

    @Test
    public void tableEntity() {
        TableEntity<?> tableEntity = new TableEntity<>(Blog.class, new DefaultNameHandler());


    }


}
