package de.kekshaus.cubit.land.api.database.yamlAPI;

import java.util.UUID;

import org.bukkit.World;

import de.kekshaus.cubit.land.Landplugin;
import de.kekshaus.cubit.land.api.database.sqlAPI.handler.OfferData;

public class YamlManager {

	private Landplugin plugin;

	public YamlManager(Landplugin plugin) {
		this.plugin = plugin;
	}

	public boolean link() {
		return false;
	}

	public long getTimeStamp(UUID uuid) {
		return 0;
	}

	public OfferData getOfferData(String regionID, World world) {
		return null;
	}

	public boolean isOffered(String regionID, World world) {
		return false;
	}

	public boolean setOfferData(OfferData data) {
		return true;
	}

	public boolean removeOfferData(String regionID, World world) {
		return true;
	}

}