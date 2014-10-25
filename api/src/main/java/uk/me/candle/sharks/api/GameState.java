package uk.me.candle.sharks.api;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

public class GameState {

	private final List<Robot> robots;

	private GameState(List<Robot> robots) {
		this.robots = robots;
	}

	/**
	 * @return a list of the robots in order, index 0 will be eaten first.
	 */
	public List<Robot> getRobots() {
		return ImmutableList.copyOf(robots);
	}

	public Robot findRobot(RobotId robotId) {
		return robots.stream().filter(r -> r.getId().equals(robotId)).findFirst().get();
	}

	public static class Builder {
		private List<Robot> robots;

		public Builder withRobots(final List<Robot> robots) {
			this.robots = new ArrayList<>();
			this.robots.addAll(robots);
			return this;
		}

		public Builder from(GameState from) {
			return this; // TODO copy the parameter's fields into this builder.
		}

		public GameState build() {
			return new GameState(robots);
		}
	}
}
