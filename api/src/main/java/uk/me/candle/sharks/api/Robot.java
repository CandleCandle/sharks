package uk.me.candle.sharks.api;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Set;
import java.util.EnumSet;


public class Robot {

	private final RobotId id;

	private final Set<Limb> remainingLimbs;
	private final Set<Card> remainingCards;


	private Robot(RobotId id, Set<Limb> limbs, Set<Card> cards) {
		this.id = id;
		this.remainingLimbs = limbs;
		this.remainingCards = cards;
	}

	public RobotId getId() {
		return id;
	}

	public Set<Limb> getRemainingLimbs() {
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
		private Set<Limb> limbs = EnumSet.noneOf(Limb.class);
		private Set<Card> cards = EnumSet.noneOf(Card.class);
		private RobotId id;

		public Builder from(Robot robot) {
			this.id = robot.getId();
			this.cards = Sets.newEnumSet(robot.getRemainingCards(), Card.class);
			this.limbs = Sets.newEnumSet(robot.getRemainingLimbs(), Limb.class);
			return this;
		}

		public Builder id(RobotId id) {
			this.id = id;
			return this;
		}

		public Builder remainingLimbs(Set<Limb> limbs) {
			this.limbs = Sets.newEnumSet(limbs, Limb.class);
			return this;
		}
		
		public Builder remainingLimbs(Limb[] limbs) {
			this.limbs = EnumSet.noneOf(Limb.class);
			this.limbs.addAll(Arrays.asList(limbs));
			return this;
		}
		
		public Builder removeLimb(Limb limb) {
			limbs.remove(limb);
			return this;
		}

		public Builder removeCard(Card card) {
			cards.remove(card);
			return this;
		}

		public Builder remainingCards(Set<Card> cards) {
			this.cards = Sets.newEnumSet(cards, Card.class);
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
