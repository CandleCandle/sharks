package uk.me.candle.sharks.service;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import uk.me.candle.sharks.api.GameState;
import uk.me.candle.sharks.api.Robot;
import uk.me.candle.sharks.api.Limb;
import uk.me.candle.sharks.api.RobotId;

@RunWith(Parameterized.class)
public class GameRunnerTest {

	private static final RobotId[] IDS = new RobotId[]{new RobotId("a"), new RobotId("b")};

	@Mock
	private RoundResolver roundResolver;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	private final List<RobotId> players;
	private final List<GameState> states;
	private final Set<Limb> remainingLimbsOfWinner;
	private final RobotId winner;

	public GameRunnerTest(List<RobotId> players, List<GameState> states, Set<Limb> remainingLimbsOfWinner, RobotId winner) {
		this.players = players;
		this.states = states;
		this.remainingLimbsOfWinner = remainingLimbsOfWinner;
		this.winner = winner;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return ImmutableList.of(
				new Object[]{
					ImmutableList.of(IDS[0]),
					ImmutableList.<GameState>of(),
					EnumSet.allOf(Limb.class),
					IDS[0]
				},
				new Object[]{
					ImmutableList.of(IDS[0], IDS[1]),
					ImmutableList.<GameState>of(new GameState.Builder().withRobots(ImmutableList.of(
											new Robot.Builder().id(IDS[0]).remainingLimbs(new Limb[]{Limb.ARM_L}).build(),
											new Robot.Builder().id(IDS[1]).remainingLimbs(new Limb[]{Limb.ARM_L, Limb.ARM_R}).build()
									)).build()),
					EnumSet.of(Limb.ARM_L, Limb.ARM_R),
					IDS[1]
				},
				new Object[]{
					ImmutableList.of(IDS[0], IDS[1]),
					ImmutableList.<GameState>of(
							new GameState.Builder().withRobots(ImmutableList.of(
											new Robot.Builder().id(IDS[0]).remainingLimbs(new Limb[]{Limb.ARM_L, Limb.ARM_R}).build(),
											new Robot.Builder().id(IDS[1]).remainingLimbs(new Limb[]{Limb.ARM_L, Limb.ARM_R}).build()
									)).build(),
							new GameState.Builder().withRobots(ImmutableList.of(
											new Robot.Builder().id(IDS[0]).remainingLimbs(new Limb[]{Limb.ARM_L}).build(),
											new Robot.Builder().id(IDS[1]).remainingLimbs(new Limb[]{Limb.ARM_L, Limb.ARM_R}).build()
									)).build()
					),
					EnumSet.of(Limb.ARM_L, Limb.ARM_R),
					IDS[1]
				}
		);
	}

	@Test
	public void roundsPlayed() {
		GameRunner undertest = new GameRunner(roundResolver, players);
		if (states.size() == 1) {
			when(roundResolver.round(any(GameState.class))).thenReturn(states.get(0));
		} else if (states.size() > 1) {
			when(roundResolver.round(any(GameState.class))).thenReturn(states.get(0), states.subList(1, states.size()).toArray(new GameState[]{}));
		}

		Robot resultWinner = undertest.play();

		verify(roundResolver, times(states.size())).round(any(GameState.class));
		assertThat(resultWinner.getId(), is(winner));
		assertThat(resultWinner.getRemainingLimbs(), is(remainingLimbsOfWinner));
	}

}
