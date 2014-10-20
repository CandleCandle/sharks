package uk.me.candle.sharks.api;

import java.util.Comparator;

public enum Card {
	ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT;

	public static enum Comparators implements Comparator<Card> {
		PRIMARY() {
			@Override
			public int compare(Card o1, Card o2) {
				return o1.ordinal() - o2.ordinal();
			}
		}
	}
}
