package com.zeiban.binky;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GitCommitCommandExecutor implements CommandExecutor {
	private BinkyPlugin plugin;
	public GitCommitCommandExecutor(BinkyPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		Player player;
		if(sender instanceof Player) {
			player = (Player)sender;
			World world;
			if(args.length > 0) {
				world = plugin.getServer().getWorld(args[0]);
				if(world == null) {
					player.sendMessage("World \"" + args[0] + "\" doesn't exist");
					return true;
				}
			} else {
				world = player.getWorld();
			}
			String comment;
			if(args.length > 1) {
				comment = args[1];
			} else {
				comment = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss").format(new Date());
			}
			plugin.getServer().dispatchCommand(
	                new ConsoleCommandSender(plugin.getServer()), "save-off");
			plugin.getServer().dispatchCommand(
	                new ConsoleCommandSender(plugin.getServer()), "save-all");
			if(plugin.commit(sender, world, comment)) {
				player.sendMessage("World \"" + world.getName() + "\" has been commited with the following comment...");
				player.sendMessage("\"" + comment + "\"");
			} else {
				player.sendMessage("World " + world.getName() + " failed to commit");
			}
			plugin.getServer().dispatchCommand(
	                new ConsoleCommandSender(plugin.getServer()), "save-on");
			return true;
		}
		return false;
	}

}
