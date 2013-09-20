package test.java.com.graywolf336.jail;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.graywolf336.jail.JailMain;

import test.java.com.graywolf336.jail.util.TestInstanceCreator;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JailMain.class, PluginDescriptionFile.class })
public class TestJailStuff {
	private TestInstanceCreator creator;
	private JailMain main;

	@Before
	public void setUp() throws Exception {
		creator = new TestInstanceCreator();
		assertTrue(creator.setup());
		main = creator.getMain();
	}

	@After
	public void tearDown() throws Exception {
		creator.tearDown();
	}

	@Test
	public void testForJails() {
		assertNotNull("The JailManager is null.", main.getJailManager());
		assertNotNull("The HashSet for jails return is null.", main.getJailManager().getJails());
	}
	
	@Test
	public void testAddingJail() {		
		Command command = mock(Command.class);
		when(command.getName()).thenReturn("jailcreate");
		String[] args = { "testJail" };
		
		CommandSender sender = creator.getPlayerCommandSender();
		
		assertTrue(main.onCommand(sender, command, "jailcreate", args));
		verify(sender).sendMessage(ChatColor.AQUA + "----------Jail Zone Creation----------");
		verify(sender).sendMessage(ChatColor.GREEN + "First, you must select jail cuboid. Select the first point of the cuboid by right clicking on the block with your wooden sword. DO NOT FORGET TO MARK THE FLOOR AND CEILING TOO!");
		verify(sender).sendMessage(ChatColor.AQUA + "--------------------------------------");
	}
}
