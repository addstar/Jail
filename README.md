[Jail](http://dev.bukkit.org/bukkit-plugins/jail/)
====
This plugins adds Jail to your minecraft server. Admins can define several jails and then jail/unjail people or jail them on time basis. Plugin also offers wide variety of protections, so players won't escape out of your jail.

Bugs Fixed
===
* Not being able to delete jails, cell, or cells.
* Players being sent to the wrong Jail (dev versions, but fixed NPEs)
* Unable to jail certain players who were in other worlds
* Unable to jail offline players, something about NPEs and stack traces.
* Players inventory is now better managed, although if you shutdown the server you will lose players inventories who are not stored in chests

Features
===
* Jail online and offline players
* Wide variety of protections to prevent escaping or simply to create more interesting escape route on role play server.
* Protection penalties (increase jail sentence if player is not behaving well)
* Guards that will kill player if he tries to escape
* Data is stored in either SQLite or MySQL.
* Supports multiple worlds
* Jailing without commands via item
* Supports escaping for role play servers.
* Allows players to vote a player to be jailed
* Allows players to handcuff other players, stopping them from being able to move
* Throws custom events when a player is jailed
* - OnlinePlayerJailedEvent for when an online player is jailed