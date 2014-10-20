package uk.me.candle.sharks.service;

import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnit44Runner;
import uk.me.candle.sharks.api.Robot;
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
		Set<Robot.Limbs> limbs = EnumSet.allOf(Robot.Limbs.class);
		assertThat(winner.getRemainingLimbs(), is(limbs));
	}
	
}
