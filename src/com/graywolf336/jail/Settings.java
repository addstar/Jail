package com.graywolf336.jail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.graywolf336.jail.beans.Jail;

public class Settings {
	private Jail jail;

	public Settings(Jail zone) {
		jail = zone;
	}

	public Integer getInt(Setting setting) {
		Object property;
		property = jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getString());
		if (property == null)
			property = getGlobalProperty(setting);
		return (Integer) property;
	}

	public Double getDouble(Setting setting) {
		Object property;
		property = jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getString());
		if (property == null)
			property = getGlobalProperty(setting);
		if (!(property instanceof Double)) property = Double.parseDouble(property.toString());
		return (Double) property;
	}

	public String getString(Setting setting) {
		Object property;
		property = jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getString());
		if (property == null)
			property = getGlobalProperty(setting);
		return (String) property;
	}

	public Boolean getBoolean(Setting setting) {
		Object property;
		property = jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getString());
		if (property == null)
			property = getGlobalProperty(setting);
		return (Boolean) property;
	}

	public List<?> getList(Setting setting) {
		Object property;
		property = jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getString());
		if (property == null)
			property = getGlobalProperty(setting);

		return (List<?>) property;
	}

	public void setProperty(Setting setting, Object object) {
		jail.getPlugin().getJailIO().getJailsConfig().get(jail.getName() + "." + setting.getString(), object);
		try {
			jail.getPlugin().getJailIO().getJailsConfig().save(new File("plugins" + File.separator + "Jail","jails.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object getGlobalProperty(Setting setting) {
		Object property = jail.getPlugin().getJailIO().getGlobalConfig().get(setting.getString());
		if (property == null) {
			property = setting.getDefault();
		}

		return property;
	}

	public Boolean getGlobalBoolean(Setting setting) {
		return 	(Boolean) getGlobalProperty(setting);
	}

	public Integer getGlobalInt(Setting setting) {
		return 	(Integer) getGlobalProperty(setting);
	}

	public String getGlobalString(Setting setting) {
		return 	(String) getGlobalProperty(setting);
	}

	public List<?> getGlobalList(Setting setting) {
		return 	(List<?>) getGlobalProperty(setting);
	}
}
