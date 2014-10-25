package uk.me.candle.sharks.ai;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import uk.me.candle.sharks.api.Card;
import uk.me.candle.sharks.api.GameState;
import uk.me.candle.sharks.api.InvalidMoveException;
import uk.me.candle.sharks.api.MoveMaker;
import uk.me.candle.sharks.api.Player;
import uk.me.candle.sharks.api.Robot;

public class RandomAi implements Player {
	private final Random rand;

	public RandomAi() {
		this(new Random());
	}

	RandomAi(Random rand) {
		this.rand = rand;
	}

	@Override
	public void makeMove(Robot me, GameState initialState, MoveMaker moveMaker) {
		List<Card> cards = Lists.newArrayList(me.getRemainingCards());
		try {
			moveMaker.makeMove(cards.get(rand.nextInt(cards.size())));
		} catch (InvalidMoveException ex) {
			// I picked one from the supplied list.
		}
	}

}
