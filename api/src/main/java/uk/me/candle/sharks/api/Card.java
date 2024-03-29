package uk.me.candle.sharks.api;

import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.Set;

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

	public static Set<Card> cardsFor(int robotCount) {
		return Sets.newHashSet(values()); // TODO find out the rules for the number of cards when there are fewer than 6 players.
	}
}
