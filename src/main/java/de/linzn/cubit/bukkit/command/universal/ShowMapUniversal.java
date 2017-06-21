package de.linzn.cubit.bukkit.command.universal;

import de.linzn.cubit.bukkit.command.ICommand;
import de.linzn.cubit.bukkit.plugin.CubitBukkitPlugin;
import de.linzn.cubit.internal.regionMgr.LandTypes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by spatium on 20.06.17.
 */
public class ShowMapUniversal implements ICommand {

	private CubitBukkitPlugin plugin;
	private String permNode;
	private LandTypes type;

	public ShowMapUniversal(CubitBukkitPlugin plugin, String permNode, LandTypes type) {
		this.plugin = plugin;
		this.permNode = permNode;
		this.type = type;
	}

	@Override
	public boolean runCmd(Command cmd, CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			/* This is not possible from the server console */
			sender.sendMessage(plugin.getYamlManager().getLanguage().noConsoleMode);
			return true;
		}
		if (this.plugin.getYamlManager().getSettings().landUseScoreboardMap) {

			/* Build and get all variables */
			Player player = (Player) sender;

			/* Permission Check */
			if (!player.hasPermission(this.permNode)) {
				sender.sendMessage(plugin.getYamlManager().getLanguage().errorNoPermission);
				return true;
			}

			try {
				plugin.getMapManager().toggleMap(player);
			} catch (Exception e) {
				e.printStackTrace();
				sender.sendMessage(ChatColor.RED + "No map available");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "This plugin part is not activated!");
		}

		return true;
	}
}