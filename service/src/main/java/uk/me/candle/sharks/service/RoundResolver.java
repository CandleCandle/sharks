package uk.me.candle.sharks.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import uk.me.candle.sharks.api.Card;
import uk.me.candle.sharks.api.GameState;
import uk.me.candle.sharks.api.Robot;
import uk.me.candle.sharks.api.RobotId;

public class RoundResolver {


	private final BiMap<GamePlayer, RobotId> playerRobots;

	public RoundResolver(Map<GamePlayer, RobotId> playerRobots) {
		this.playerRobots = HashBiMap.create(playerRobots);
	}

	GameState round(GameState initial) {

		List<IndividualState> intermediates = initial.getRobots().stream()
				.map(r -> new IndividualState(r, playerRobots.inverse().get(r.getId()),r.getId()))
				.collect(Collectors.toList());

		intermediates.stream().forEach(i -> i.move(initial));

		Multimap<Card, IndividualState> resolution = ArrayListMultimap.create();
		intermediates.stream().forEach(e -> resolution.put(e.getMove(), e));

		List<Card> sortedCards = Lists.newArrayList(Card.values());
		Collections.sort(sortedCards, Card.Comparators.PRIMARY);

		List<IndividualState> result = Lists.newLinkedList(intermediates);
		// given the cards in order, smallest first, move the robots to the
		//  end of the list IF they are the only robot that played that card.
		for (Card card : sortedCards) {
			Collection<IndividualState> players = resolution.get(card);
			if (players.size() == 1) {
				RobotId playerRobotId = players.iterator().next().getInitialRobot().getId();
				int index = 0;
				for (int i = 0; i < result.size(); ++i) {
					if (result.get(i).getInitialRobot().getId().equals(playerRobotId)) index = i;
				}
				IndividualState moving = result.remove(index);
				result.add(moving);
			}
		}

		List<Robot> finalResult = result.stream().map(IndividualState::getFinalRobot).collect(Collectors.toList());

		return new GameState.Builder().withRobots(finalResult).build();
	}

	class IndividualState {
		private final GamePlayer gamePlayer;
		private final RobotId robotId;
		private final Robot initialRobot;
		private Robot finalRobot;
		private Card move;

		public IndividualState(Robot initialRobot, GamePlayer gamePlayer, RobotId robotId) {
			this.gamePlayer = gamePlayer;
			this.robotId = robotId;
			this.initialRobot = initialRobot;
		}

		void move(GameState gameState) {
			move = gamePlayer.makeMove(robotId, gameState);
			finalRobot = new Robot.Builder().from(initialRobot).removeCard(move).build();
		}

		public Card getMove() {
			return move;
		}

		public Robot getInitialRobot() {
			return initialRobot;
		}

		public Robot getFinalRobot() {
			return finalRobot;
		}
	}

}
