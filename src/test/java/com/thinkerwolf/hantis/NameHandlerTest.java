package com.thinkerwolf.hantis;

import com.thinkerwolf.hantis.common.DefaultNameHandler;


import org.junit.Test;

public class NameHandlerTest {

    @Test
    public void defaultHandler() {
        DefaultNameHandler handler = new DefaultNameHandler();
        System.out.println(handler.convertToPropertyName("player_id"));
        System.out.println(handler.convertToColumnName("playerId"));
        
       // System.out.println(List.class == );
    }

}
