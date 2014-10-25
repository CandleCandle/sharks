package uk.me.candle.sharks.service;

import static com.google.common.collect.ImmutableList.of;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
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
	private static final Player[] PL = new Player[]{Mockito.mock(Player.class), Mockito.mock(Player.class), Mockito.mock(Player.class)};

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{ of(PL[0], PL[1]), of(Card.ONE, Card.TWO), of(ID[0], ID[1]), of(ID[0], ID[1]) },
			{ of(PL[0], PL[1]), of(Card.TWO, Card.ONE), of(ID[0], ID[1]), of(ID[1], ID[0]) },
			{ of(PL[0], PL[1]), of(Card.TWO, Card.TWO), of(ID[0], ID[1]), of(ID[0], ID[1]) },
			{ of(PL[0], PL[1]), of(Card.TWO, Card.TWO), of(ID[1], ID[0]), of(ID[1], ID[0]) },
			{
					of(PL[0],	PL[1],	PL[2]),
					of(Card.TWO, Card.TWO, Card.THREE),
					of(ID[0], ID[2], ID[1]),
					of(ID[0], ID[1], ID[2])
			},
			{
					of(PL[0],	PL[1],	PL[2]),
					of(Card.TWO, Card.TWO, Card.ONE),
					of(ID[0], ID[2], ID[1]),
					of(ID[0], ID[1], ID[2])
			}
		}

		);
	}

	private final List<Player> players;
	private final List<Card> moveSelection;
	private final List<RobotId> initial;
	private final List<RobotId> result;

	public RoundResolverTest(List<Player> players, List<Card> moveSelection, List<RobotId> initial, List<RobotId> result) {
		this.moveSelection = moveSelection;
		this.players = players;
		this.initial = initial;
		this.result = result;
	}

	@Test
	public void testSomeMethod() {
		GameState initialState = new GameState.Builder()
				.withRobots(initial.stream()
						.map(r -> new Robot.Builder().id(r)
								.remainingCards(Card.values())
								.remainingLimbs(Limb.values())
								.build()
						)
						.collect(Collectors.toList()))
				.build();

		for (int i = 0; i < players.size(); ++i) {
			when(players.get(i).makeMove(initialState)).thenReturn(moveSelection.get(i));
		}

		RoundResolver undertest = new RoundResolver(players.stream()
				.collect(Collectors.toMap(e -> e, e -> forPlayer(e)))
		);

		GameState resultState = undertest.round(initialState);

		List<RobotId> resultOrdering = resultState.getRobots().stream().map(Robot::getId).collect(Collectors.toList());
		assertThat(resultOrdering, is(result));
	}

	private static RobotId forPlayer(Player p) {
		for (int i = 0; i < PL.length; ++i) {
			if (PL[i] == p) return ID[i];
		}
		throw new IllegalArgumentException();
	}

}