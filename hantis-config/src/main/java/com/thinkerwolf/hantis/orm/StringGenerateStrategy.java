package com.thinkerwolf.hantis.orm;

import java.util.Random;

public class StringGenerateStrategy extends AbstractGenerateStrategy<String> {

	private Random random = new Random();

	private int length;

	public StringGenerateStrategy(int length) {
		this.length = length < 4 ? 4 : length;
	}

	@Override
	public String autoGenerate() {
		return getRandomString(length);
	}

	public String getRandomString(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(3);
			long result = 0;
			switch (number) {
			case 0:
				result = Math.round(Math.random() * 25 + 65);
				sb.append(String.valueOf((char) result));
				break;
			case 1:
				result = Math.round(Math.random() * 25 + 97);
				sb.append(String.valueOf((char) result));
				break;
			case 2:
				sb.append(String.valueOf(random.nextInt(10)));
				break;
			}

		}
		return sb.toString();
	}

	@Override
	protected void doCompareAndSet(String value) {
		// do nothing
	}
}
