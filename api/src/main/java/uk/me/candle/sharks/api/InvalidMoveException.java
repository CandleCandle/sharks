package uk.me.candle.sharks.api;

class InvalidMoveException extends Exception {
	
	private final Card failure;

	public InvalidMoveException(Card failure) {
		this.failure = failure;
	}

	@Override
	public String getMessage() {
		return "Failed to play the card " + failure;
	}
}
