package uk.me.candle.sharks.service;

import java.util.List;
import uk.me.candle.sharks.api.Card;
import uk.me.candle.sharks.api.Robot;
import uk.me.candle.sharks.api.RobotId;

public class GameRunner {

	private final RoundResolver resolver;
	private final List<RobotId> robotIds;

	public GameRunner(RoundResolver resolver, List<RobotId> robotIds) {
		this.resolver = resolver;
		this.robotIds = robotIds;
	}

	Robot play() {
		Robot robot = new Robot.Builder()
				.id(robotIds.get(0))
				.remainingCards(Card.values())
				.remainingLimbs(Robot.Limbs.values())
				.build();
		return robot;
	}

}
