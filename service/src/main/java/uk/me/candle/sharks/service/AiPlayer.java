package uk.me.candle.sharks.service;

import uk.me.candle.sharks.api.Card;
import uk.me.candle.sharks.api.GameState;
import uk.me.candle.sharks.api.InvalidMoveException;
import uk.me.candle.sharks.api.MoveMaker;
import uk.me.candle.sharks.api.Player;
import uk.me.candle.sharks.api.Robot;
import uk.me.candle.sharks.api.RobotId;

public class AiPlayer implements GamePlayer {

	private final Player wrapped;

	public AiPlayer(Player wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public Card makeMove(final RobotId robot, final GameState initialState) {
		Robot thisRobot = initialState.getRobots().stream().filter(r -> r.getId().equals(robot)).findFirst().get();
		MoveMakerImpl move = new MoveMakerImpl(thisRobot);
		
		wrapped.makeMove(thisRobot, initialState, move);
		
		return move.move;
	}

	private static class MoveMakerImpl implements MoveMaker {

		private final Robot robot;
		private Card move;

		public MoveMakerImpl(Robot robot) {
			this.robot = robot;
		}

		@Override
		public void makeMove(Card card) throws InvalidMoveException {
			if (robot.getRemainingCards().contains(card)) {
				move = card;
			} else {
				throw new InvalidMoveException(card);
			}
		}
	}
}
