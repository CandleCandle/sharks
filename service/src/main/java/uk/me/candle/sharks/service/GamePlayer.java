package uk.me.candle.sharks.service;

import uk.me.candle.sharks.api.Card;
import uk.me.candle.sharks.api.GameState;
import uk.me.candle.sharks.api.RobotId;

public interface GamePlayer {

	Card makeMove(RobotId robot, GameState initialState);

}
