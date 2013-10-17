package test.java.com.graywolf336.jail;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.Setting;

import test.java.com.graywolf336.jail.util.TestInstanceCreator;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JailMain.class, PluginDescriptionFile.class })
public class TestJailStuff {
	private TestInstanceCreator creator;
	private JailMain main;

	@Before
	public void setUp() throws Exception {
		creator = new TestInstanceCreator();
		creator.setup();
		main = creator.getMain();
	}

	@After
	public void tearDown() throws Exception {
		creator.tearDown();
	}

	@Test
	public void testForJails() {
		assertNotNull("The JailMain is null.", main);
		assertNotNull("The JailIO instance is null.", main.getJailIO());
		assertNotNull("The GlobalConfig is null.", main.getJailIO().getGlobalConfig());
		assertNotNull("The JailsConfig is null.", main.getJailIO().getJailsConfig());
		assertNotNull("The JailManager is null.", main.getJailManager());
		assertNotNull("The HashSet for jails return is null.", main.getJailManager().getJails());
		assertThat(main, is(main.getJailManager().getPlugin()));
	}
	
	@Test
	public void testTheSettings() {
		YamlConfiguration global = main.getJailIO().getGlobalConfig();
		
		for (Setting s : Setting.values()) {
			assertThat(s.getDefault(), is(global.get(s.getPath())));
		}
	}
}
