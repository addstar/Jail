package com.graywolf336.jail.beans;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.graywolf336.jail.Setting;

public class Settings {
	private Jail jail;

	public Settings(Jail zone) {
		jail = zone;
	}

	public Integer getInt(Setting setting) {
		Object property;
		property = jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getPath());
		if (property == null)
			property = jail.getPlugin().getJailIO().getGlobalProperty(setting);
		return (Integer) property;
	}

	public Double getDouble(Setting setting) {
		Object property;
		property = jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getPath());
		if (property == null)
			property = jail.getPlugin().getJailIO().getGlobalProperty(setting);
		if (!(property instanceof Double)) property = Double.parseDouble(property.toString());
		return (Double) property;
	}

	public String getString(Setting setting) {
		Object property;
		property = jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getPath());
		if (property == null)
			property = jail.getPlugin().getJailIO().getGlobalProperty(setting);
		return (String) property;
	}

	public Boolean getBoolean(Setting setting) {
		Object property;
		property = jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getPath());
		if (property == null)
			property = jail.getPlugin().getJailIO().getGlobalProperty(setting);
		return (Boolean) property;
	}

	public List<?> getList(Setting setting) {
		Object property;
		property = jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getPath());
		if (property == null)
			property = jail.getPlugin().getJailIO().getGlobalProperty(setting);

		return (List<?>) property;
	}

	public void setProperty(Setting setting, Object object) {
		jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getPath(), object);
		try {
			jail.getPlugin().getJailIO().getJailsConfig().save(new File("plugins" + File.separator + "Jail","jails.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
