package uk.me.candle.sharks.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import uk.me.candle.sharks.ai.RandomAi;
import uk.me.candle.sharks.api.Robot;
import uk.me.candle.sharks.api.RobotId;

public class Main {




	public static void main(String[] args) {
		

		AiPlayer pa = new AiPlayer(new RandomAi());
		AiPlayer pb = new AiPlayer(new RandomAi());

		RobotId ra = new RobotId("a");
		RobotId rb = new RobotId("b");

		RoundResolver resolver = new RoundResolver(ImmutableMap.of(pa, ra, pb, rb));
		GameRunner runner = new GameRunner(resolver, ImmutableList.of(ra, rb));

		Robot winner = runner.play();

		System.out.println("The winner is " + winner);
		
	}
}
