package uk.me.candle.sharks.api;

import java.util.Set;
import java.util.EnumSet;


public class Robot {

	private final RobotId id;

	private final Set<Limbs> remainingLimbs = EnumSet.allOf(Limbs.class);
	private final Set<Card> remainingCards = EnumSet.allOf(Card.class);

	static enum Limbs { ARM_L, ARM_R, LEG_R, LEG_L; }

	public Robot(RobotId id) {
		this.id = id;
	}

	public RobotId getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Robot{" + "id=" + id + ", remainingLimbs=" + remainingLimbs + ", remainingCards=" + remainingCards + "} [" + super.toString() + "]";
	}
}
