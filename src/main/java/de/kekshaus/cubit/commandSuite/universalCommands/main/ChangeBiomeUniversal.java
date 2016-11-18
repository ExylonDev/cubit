package de.kekshaus.cubit.commandSuite.universalCommands.main;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kekshaus.cubit.api.regionAPI.region.LandTypes;
import de.kekshaus.cubit.api.regionAPI.region.RegionData;
import de.kekshaus.cubit.commandSuite.ILandCmd;
import de.kekshaus.cubit.plugin.Landplugin;

public class ChangeBiomeUniversal implements ILandCmd {

	private Landplugin plugin;
	private String permNode;
	private LandTypes type;
	private boolean isAdmin;

	public ChangeBiomeUniversal(Landplugin plugin, String permNode, LandTypes type, boolean isAdmin) {
		this.plugin = plugin;
		this.isAdmin = isAdmin;

		this.permNode = permNode;
		this.type = type;
	}

	@Override
	public boolean runCmd(final Command cmd, final CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			/* This is not possible from the server console */
			sender.sendMessage(plugin.getYamlManager().getLanguage().noConsoleMode);
			return true;
		}

		/* Build and get all variables */
		Player player = (Player) sender;

		/* Permission Check */
		if (!player.hasPermission(this.permNode)) {
			sender.sendMessage(plugin.getYamlManager().getLanguage().errorNoPermission);
			return true;
		}

		if (args.length < 2) {
			sender.sendMessage(plugin.getYamlManager().getLanguage().wrongArguments.replace("{usage}",
					"/" + cmd.getLabel() + " " + args[0].toLowerCase() + " [Biome]"));
			return true;
		}

		final Location loc = player.getLocation();

		if (!checkBiome(args[1].toUpperCase())) {
			sender.sendMessage(
					plugin.getYamlManager().getLanguage().notABiome.replace("{biome}", args[1].toUpperCase()));
			return true;
		}

		Biome biome = Biome.valueOf(args[1].toUpperCase());
		final Chunk chunk = loc.getChunk();
		RegionData regionData = plugin.getLandManager().praseRegionData(loc.getWorld(), chunk.getX(), chunk.getZ());

		/*
		 * Check if the player has permissions for this land or hat landadmin
		 * permissions
		 */
		if (!plugin.getLandManager().isLand(loc.getWorld(), chunk.getX(), chunk.getZ())) {
			sender.sendMessage(plugin.getYamlManager().getLanguage().errorNoLandFound);
			return true;
		}

		if (regionData.getLandType() != type && type != LandTypes.NOTYPE) {
			sender.sendMessage(
					plugin.getYamlManager().getLanguage().errorNoValidLandFound.replace("{type}", type.toString()));
			return true;
		}

		if (!plugin.getLandManager().hasLandPermission(regionData, player.getUniqueId()) && this.isAdmin == false) {
			sender.sendMessage(plugin.getYamlManager().getLanguage().errorNoLandPermission.replace("{regionID}",
					regionData.getRegionName()));
			return true;
		}

		if (!plugin.getBlockManager().changeBiomeChunk(chunk, biome)) {
			/* If this task failed! This should never happen */
			sender.sendMessage(plugin.getYamlManager().getLanguage().errorInTask.replace("{error}", "SET-BIOME"));
			plugin.getLogger()
					.warning(plugin.getYamlManager().getLanguage().errorInTask.replace("{error}", "SET-BIOME"));
			return true;
		}

		sender.sendMessage(plugin.getYamlManager().getLanguage().changedBiome
				.replace("{regionID}", regionData.getRegionName()).replace("{biome}", biome.name().toUpperCase()));

		return true;
	}

	private boolean checkBiome(String biomename) {
		for (Biome biome : Biome.values()) {
			if (biome.name().equals(biomename)) {
				return true;
			}
		}
		return false;
	}

}