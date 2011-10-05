package com.zeiban.binky;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.revwalk.RevCommit;

public class GitBackupPlugin extends JavaPlugin {
	private Logger logger = Logger.getLogger("Minecraft");
	
	@Override
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
	}
	@Override
	public void onEnable() {
		getCommand("git-commit").setExecutor(new GitCommitCommandExecutor(this));
		getCommand("git-log").setExecutor(new GitLogCommandExecutor(this));
//		getCommand("git-reset").setExecutor(new GitResetCommandExecutor(this));
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				List<World> worlds = getServer().getWorlds();
				for(World world : worlds) {
					GitBackupPlugin.this.commit(null, world, new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss").format(new Date()));
				}
			}}, 60*60*30, 60*60*30);
	}
	public Git init(World world) {
		Git git;
		File dir = new File(world.getName());
		try{
			git = Git.open(dir);
		} catch (Exception e) {
			git = Git.init().setDirectory(dir).call();
		}
		return git;
	}
	public boolean commit(CommandSender sender, World world, String comment) {
		Git git = init(world);
		try{
			git.add().addFilepattern(".").call();
			Status status = git.status().call();
			if(status.getAdded().size() > 0 || status.getChanged().size() > 0) {
				git.commit().setMessage(comment).call();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public boolean log(CommandSender sender, World world) {
		Git git = init(world);

		Iterable<RevCommit> commits;
		try {
			commits = git.log().call();
			Iterator<RevCommit> iter = commits.iterator();
			int i=0;
			while(iter.hasNext()) {
				RevCommit commit = iter.next();
				sender.sendMessage(i + ": " + commit.getName() + " " + commit.getFullMessage());
				i++;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to get repository logs", e);
			return false;
		}

		return true;
	}
	public boolean reset(CommandSender sender, World world, int id) {
		Git git = init(world);
		try {
			Iterable<RevCommit> commits;
			commits = git.log().call();
			Iterator<RevCommit> iter = commits.iterator();
			int i=0;
			while(iter.hasNext()) {
				RevCommit commit = iter.next();
				if(i == id) {
					getServer().dispatchCommand(
			                new ConsoleCommandSender(getServer()), "save-off");
					sender.sendMessage("Resetting world \"" + world.getName() + "\" to "+ commit.getFullMessage());
					this.getServer().unloadWorld(world, false);;
					git.reset().setMode(ResetType.HARD).setRef(commit.getName()).call();
					this.getServer().shutdown();
					return true;
				}
				i++;
			}
			sender.sendMessage("The reset ID + \"" + id + "\" is not valid");
			return false;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to reset world \"" + world.getName() + "\"", e);
			return false;
		}

	}
}
