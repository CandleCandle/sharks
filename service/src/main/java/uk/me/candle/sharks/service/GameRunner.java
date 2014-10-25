package uk.me.candle.sharks.service;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import uk.me.candle.sharks.api.Card;
import uk.me.candle.sharks.api.GameState;
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
		List<Robot> robots = robotIds.stream().map(
			r -> new Robot.Builder()
					.id(r)
					.remainingLimbs(Robot.Limbs.values())
					.remainingCards(Card.cardsFor(robotIds.size()))
					.build()
		).collect(Collectors.toList());
				

		GameState currentState = new GameState.Builder().withRobots(robots).build();
		while (currentState.getRobots().size() > 1) {
			GameState result = resolver.round(currentState);
			List<Robot> resultingRobots = Lists.newLinkedList(result.getRobots());
			Robot losingRobot = resultingRobots.remove(0);
			losingRobot = new Robot.Builder()
					.from(losingRobot)
					.removeLimb(losingRobot.getRemainingLimbs().iterator().next())
					.build();
			if (!losingRobot.getRemainingLimbs().isEmpty()) {
				resultingRobots.add(0, losingRobot);
			}
			 currentState = new GameState.Builder().from(currentState).withRobots(resultingRobots).build();
		}
		

		return currentState.getRobots().get(0);
	}

}
