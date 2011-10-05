CURRENTLY UNSTABLE - NOT RECOMMENDED FOR LIVE BUKKIT SERVERS

#### Bukkit Plugin for Git World Backups ####

Ever notice that your world backups are getting huge even when you compress them? Why store a complete backup when you can use the power of Git to only store the changes made between backups. That is the aim of this project.

When a commit is performed manually though /git-commit or from the scheduler it will init the world directory as a Git repository if not already done and commit the current world state.

Right now if you to reset the world to a previous commit you need to shutdown the server and use your favorite Git client to reset the master to a previous commit.

Commands

/git-commit <world> <comment> Commits the specified world to the repository

/git-log <world> - List commits for a specific world

/git-reset <id> - *Disable Experimental* Will shutdown the server, reset to a previous commit, and then start the server.  