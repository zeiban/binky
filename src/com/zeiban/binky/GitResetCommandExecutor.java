package com.zeiban.binky;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GitResetCommandExecutor implements CommandExecutor {
	private BinkyPlugin plugin;
	public GitResetCommandExecutor(BinkyPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		//if(args.length < 2) {
		//	sender.sendMessage("You must specify a world and a commit ID to reset");
		//	return false;
		//}
		World world = ((Player)sender).getWorld();
		int id = Integer.parseInt(args[0]);
		plugin.getServer().dispatchCommand(
                new ConsoleCommandSender(plugin.getServer()), "save-off");

		plugin.reset(sender, world, id);
		return true;
	}

}
