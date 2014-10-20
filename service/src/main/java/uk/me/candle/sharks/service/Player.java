package uk.me.candle.sharks.service;

import uk.me.candle.sharks.api.Card;
import uk.me.candle.sharks.api.GameState;

public interface Player {

	Card makeMove(GameState initialState);

}
