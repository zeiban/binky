package com.zeiban.binky;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GitLogCommandExecutor implements CommandExecutor {
	private GitBackupPlugin plugin;
	public GitLogCommandExecutor(GitBackupPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		
		Player player;
		World world = null;
		String worldName = null;

		if(args.length > 0) {
			worldName = args[0];
		}
		if(sender instanceof Player) {
			player = (Player) sender;
			if(args.length == 0) {
				worldName = player.getWorld().getName();
			}
			
		} else {
			if(args.length == 0) {
				sender.sendMessage("Must specify a world to commit");
				return false;
			}
		}

		world = plugin.getServer().getWorld(worldName);
		
		if(world == null) {
			sender.sendMessage("World \"" + worldName + "\" does not exist");
			return false;
		}
		plugin.log(sender, world);
		return true;
	}

}
