package uk.me.candle.sharks.api;

public interface Player {

	void makeMove(Robot me, GameState initialState, MoveMaker moveMaker);

}
