package com.thinkerwolf.hantis;

import org.junit.Test;

import com.thinkerwolf.hantis.common.DefaultNameHandler;

public class NameHandlerTest {
	
	@Test
	public void defaultHandler() {
		DefaultNameHandler handler = new DefaultNameHandler();
		System.out.println(handler.convertToPropertyName("player_id"));
		System.out.println(handler.convertToColumnName("playerId"));
	}
	
}
