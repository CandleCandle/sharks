package uk.me.candle.sharks.service;

import static com.google.common.collect.ImmutableList.of;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import uk.me.candle.sharks.api.Card;
import uk.me.candle.sharks.api.GameState;
import uk.me.candle.sharks.api.Limb;
import uk.me.candle.sharks.api.Robot;
import uk.me.candle.sharks.api.RobotId;

@RunWith(Parameterized.class)
public class RoundResolverTest {

	private static final RobotId[] ID = new RobotId[]{new RobotId("a"), new RobotId("b"), new RobotId("c")};
	private static final GamePlayer[] PL = new GamePlayer[]{Mockito.mock(GamePlayer.class), Mockito.mock(GamePlayer.class), Mockito.mock(GamePlayer.class)};

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{ of(new MoveState(PL[0], ID[0], Card.ONE), new MoveState(PL[1], ID[1], Card.TWO)), of(ID[0], ID[1]) },
			{ of(new MoveState(PL[0], ID[0], Card.TWO), new MoveState(PL[1], ID[1], Card.ONE)), of(ID[1], ID[0]) },
			{ of(new MoveState(PL[0], ID[0], Card.TWO), new MoveState(PL[1], ID[1], Card.TWO)), of(ID[0], ID[1]) },
			{ of(new MoveState(PL[1], ID[1], Card.TWO), new MoveState(PL[0], ID[0], Card.TWO)), of(ID[1], ID[0]) },
			{ of(
					new MoveState(PL[0], ID[0], Card.TWO),
					new MoveState(PL[1], ID[1], Card.TWO),
					new MoveState(PL[2], ID[2], Card.THREE)
			), of(ID[0], ID[1], ID[2]) },
			{ of(
					new MoveState(PL[0], ID[0], Card.TWO),
					new MoveState(PL[1], ID[1], Card.TWO),
					new MoveState(PL[2], ID[2], Card.ONE)
			), of(ID[0], ID[1], ID[2]) }
		});
	}
	
	private final List<MoveState> initialAndMoves;
	private final List<RobotId> result;

	public static class MoveState {
		GamePlayer player;
		RobotId id;
		Card move;

		public MoveState(GamePlayer player, RobotId id, Card move) {
			this.player = player;
			this.id = id;
			this.move = move;
		}
	}

	public RoundResolverTest(List<MoveState> initialAndMoves, List<RobotId> result) {
		this.initialAndMoves = initialAndMoves;
		this.result = result;
	}

	@Test
	public void testSomeMethod() {
		GameState initialState = new GameState.Builder()
				.withRobots(initialAndMoves.stream()
						.map(r -> new Robot.Builder().id(r.id)
								.remainingCards(Card.values())
								.remainingLimbs(Limb.values())
								.build()
						)
						.collect(Collectors.toList()))
				.build();

		for (MoveState s : initialAndMoves) {
			when(s.player.makeMove(any(RobotId.class), eq(initialState))).thenReturn(s.move);
		}

		RoundResolver undertest = new RoundResolver(initialAndMoves.stream()
				.collect(Collectors.toMap(e -> e.player, e -> e.id))
		);

		GameState resultState = undertest.round(initialState);

		List<RobotId> resultOrdering = resultState.getRobots().stream().map(Robot::getId).collect(Collectors.toList());
		assertThat(resultOrdering, is(result));

		List<Set<Card>> cardSets = resultState.getRobots().stream().map(Robot::getRemainingCards).collect(Collectors.toList());
		for (int i = 0; i < result.size(); ++i) {
			RobotId id = result.get(i);
			MoveState state = forId(id);
			final Set<Card> cards = cardSets.get(i);
			final Card move = state.move;
			assertFalse("expecting cards from player " + i + " to have the card " + move + " removed, set was: " + cards, cards.contains(move));
		}
	}

	private MoveState forId(RobotId p) {
		for (MoveState s :  initialAndMoves) {
			if (s.id.equals(p)) return s;
		}
		throw new IllegalArgumentException();
	}

}