[b][size=12pt]Would you like to have a way for your players to EARN their money? Would you like to be fair about it?
Then this is the plugin for you!

This plugin can keep track of various damage dealt to any Entity for a configurable time! I can track multiple sources of damage including environmental and player dealt and reward the player(s) involved accordingly! 
No more quarrels among players for sharing monetary rewards for killing a boss! This plugin will handle that for you! It will give each player money based on the %-age of the damage they dealt! No more Kill steal for the big bucks! No more freeloaders who hit once then sit on the sideline!

Your problems are solved!

You can customize the minimum AND the maximum reward or in some cases penalty for killing an entity!

Yes! This plugin can charge a penalty for killing someone's pet dog or cat or donkey horse mob thingy! All you have to do is set a Negative number in the config as a reward!

What are you waiting for? You know you need this plugin![/size][/b]

[center][color=red][size=18pt][b]Critical dependency:[/b][/size][/color][/center]
[size=14pt][b]Please download and install [url=http://www.project-rainbow.org/site/downloads/?sa=view;down=201]Api-Collection[/url] plugin. If you didn't do it, this plugin won't work.
[/b][/size]

[center][b][size=18pt]Commands:[/size][/b][/center]
This plugin doesn't have any commands.

[center][b][size=18pt]Permissions:[/size][/b][/center]
[b]damagehandler.reward.<entitytype>[/b]
For enabling getting rewards/punishments for killing entities.
[b]damagehandler.seereport.entity.<entitytype>[/b]
See reports about entity deaths.
[b]damagehandler.seereport.player[/b]
See reports about player deaths.
damagehandler.particularreport.entity.<entitytype>[/b]
See reports about entity deaths, if you are the entities particular killer.
[b]damagehandler.particularreport.player[/b]
See reports about player deaths, if you are the players particular killer.

[center][b][size=18pt]Configuration:[/size][/b][/center]
[b]config.yml[/b] in plugins_mods/DamageHandler folder:

[b]tracktime:[/b] the plugin handle damages from these time before entity death.
[b]rewards:[/b] for setting custom rewards/punishments for each kind of entity
[b]format:[/b]
entityname:<minimal reward> [maximal reward]


[b]lang.lng[/b] in FairDamageRewarder.jar file:
For modifying plugins language file.

[size=14pt]You can use my [b][url=http://www.project-rainbow.org/site/downloads/?sa=view;down=201]API-Collection[/url][/b]-s permission management for adding all the entity types to the players.[/size]

[center][b][size=18pt]Changelog:[/size][/b][/center]
[b]1.1.0[/b]
- Renamed to FairDamageRewarder, so you also need to rename the DamageHandler folder to FairDamageRewarder.
- Removed tracking not-damaged mobs and added player tracking
- Added concatenation for same type of damages (no more tons)
- Fixed last damage counting, damage value, not mobs remaining health bug
- Added some rounding for percentage and reward calculation
- Fixed money management bugs
[b]1.1.1[/b]
- Removed reports about mob deaths without player damage.
[b]1.2.0[/b]
- Added roundings for damages
- Added particular report see permissions
- Fixed player names not showing in reports
[b]1.2.1[/b]
- Fixed rewarding and rounding bugs

[center][url=http://www.project-rainbow.org/site/plugin-releases/damagehandler-1127/new/#new][b][color=brown][size=24pt]FORUM[/size][/color][/b][/url][/center]