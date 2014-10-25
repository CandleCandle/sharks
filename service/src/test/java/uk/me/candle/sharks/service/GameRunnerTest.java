package uk.me.candle.sharks.service;

import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnit44Runner;
import uk.me.candle.sharks.api.GameState;
import uk.me.candle.sharks.api.Limb;
import uk.me.candle.sharks.api.Robot;
import uk.me.candle.sharks.api.Limb;
import uk.me.candle.sharks.api.RobotId;

@RunWith(MockitoJUnit44Runner.class)
public class GameRunnerTest {

	@Mock private RoundResolver roundResolver;

	
	@Test
	public void oneRobotWinsTheGame() {
		List<RobotId> robotIds = ImmutableList.of(new RobotId("a"));
		GameRunner undertest = new GameRunner(roundResolver, robotIds);

		Robot winner = undertest.play();

		assertThat(winner.getId(), is(new RobotId("a")));
		Set<Limb> limbs = EnumSet.allOf(Limb.class);
		assertThat(winner.getRemainingLimbs(), is(limbs));
	}

	@Test
	public void nearlyFinishedWithTwo() {
		List<RobotId> robotIds = ImmutableList.of(new RobotId("a"), new RobotId("b"));

		List<Robot> finalState = ImmutableList.of(
			new Robot.Builder().id(robotIds.get(0)).remainingLimbs(new Limb[]{Limb.ARM_L}).build(),
			new Robot.Builder().id(robotIds.get(1)).remainingLimbs(new Limb[]{Limb.ARM_L, Limb.ARM_R}).build()
		);
		when(roundResolver.round(any(GameState.class)))
				.thenReturn(
					new GameState.Builder().withRobots(finalState).build()
				);
		
		GameRunner undertest = new GameRunner(roundResolver, robotIds);
		
		Robot winner = undertest.play();

		verify(roundResolver, times(1)).round(any(GameState.class));
		assertThat(winner.getId(), is(new RobotId("b")));
		assertThat(winner.getRemainingLimbs(), is(EnumSet.of(Limb.ARM_L, Limb.ARM_R)));
		
	}
}
