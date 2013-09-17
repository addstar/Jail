package test.com.graywolf336.jail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;

import com.graywolf336.jail.steps.JailCreationSteps;

@RunWith(PowerMockRunner.class)
public class JailCreationStepsTests {

	
	@Test
	public void testStartStepping() {
		
		Player mockPlayer = mock(Player.class);
		JailCreationSteps jcs = new JailCreationSteps();
		
		jcs.startStepping(mockPlayer);
		
		verify(mockPlayer).sendMessage(ChatColor.AQUA + "----------Jail Zone Creation----------");
	}
}
