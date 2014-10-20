package uk.me.candle.sharks.api;

public interface MoveMaker {
	
	void makeMove(Card card) throws InvalidMoveException;
	
}
