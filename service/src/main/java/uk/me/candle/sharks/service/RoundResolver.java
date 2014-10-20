package uk.me.candle.sharks.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import uk.me.candle.sharks.api.Card;
import uk.me.candle.sharks.api.GameState;
import uk.me.candle.sharks.api.Robot;
import uk.me.candle.sharks.api.RobotId;

public class RoundResolver {


	Map<Player, RobotId> playerRobots;


	GameState round(GameState initial) {

		Map<Player, Card> moves = Maps.newHashMap();
		// collect moves from each player.
		playerRobots.keySet().stream().forEach(player -> moves.put(player, player.makeMove(initial)));

		Multimap<Card, Player> resolution = ArrayListMultimap.create();
		moves.entrySet().stream().forEach(e -> resolution.put(e.getValue(), e.getKey()));

		List<Card> sortedCards = Lists.newArrayList(Card.values());
		Collections.sort(sortedCards, Card.Comparators.PRIMARY);


		List<Robot> result = Lists.newLinkedList(initial.getRobots());
		// given the cards in order, smallest first, move the robots to the
		//  end of the list IF they are the only robot that played that card.
		for (Card card : sortedCards) {
			Collection<Player> players = resolution.get(card);
			if (players.size() == 1) {
				RobotId playerRobotId = playerRobots.get(players.iterator().next());
				int index = 0;
				for (int i = 0; i < result.size(); ++i) {
					if (result.get(i).getId().equals(playerRobotId)) index = i;
				}
				Robot moving = result.remove(index);
				result.add(moving);
			}
		}


		GameState finalState = new GameState.Builder().withRobots(result).build();
		
		return finalState;
	}

}
