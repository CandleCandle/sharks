package uk.me.candle.sharks.api;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Set;
import java.util.EnumSet;


public class Robot {

	private final RobotId id;

	private final Set<Limbs> remainingLimbs;
	private final Set<Card> remainingCards;

	public static enum Limbs { ARM_L, ARM_R, LEG_R, LEG_L; }

	private Robot(RobotId id, Set<Limbs> limbs, Set<Card> cards) {
		this.id = id;
		this.remainingLimbs = limbs;
		this.remainingCards = cards;
	}

	public RobotId getId() {
		return id;
	}

	public Set<Limbs> getRemainingLimbs() {
		return Sets.immutableEnumSet(remainingLimbs);
	}

	public Set<Card> getRemainingCards() {
		return Sets.immutableEnumSet(remainingCards);
	}
	
	@Override
	public String toString() {
		return "Robot{" + "id=" + id + ", remainingLimbs=" + remainingLimbs + ", remainingCards=" + remainingCards + "} [" + super.toString() + "]";
	}

	public static class Builder {
		private Set<Limbs> limbs = EnumSet.noneOf(Limbs.class);
		private Set<Card> cards = EnumSet.noneOf(Card.class);
		private RobotId id;

		public Builder from(Robot robot) {
			this.id = robot.getId();
			this.cards = robot.getRemainingCards();
			this.limbs = robot.getRemainingLimbs();
			return this;
		}

		public Builder id(RobotId id) {
			this.id = id;
			return this;
		}

		public Builder remainingLimbs(Set<Limbs> limbs) {
			this.limbs = limbs;
			return this;
		}
		
		public Builder remainingLimbs(Limbs[] limbs) {
			this.limbs = EnumSet.noneOf(Limbs.class);
			this.limbs.addAll(Arrays.asList(limbs));
			return this;
		}

		public Builder remainingCards(Set<Card> cards) {
			this.cards = cards;
			return this;
		}

		public Builder remainingCards(Card[] cards) {
			this.cards = EnumSet.noneOf(Card.class);
			this.cards.addAll(Arrays.asList(cards));
			return this;
		}
		
		public Robot build() {
			return new Robot(id, limbs, cards);
		}
	}
}
