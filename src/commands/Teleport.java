package commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Teleport {
	
	@SuppressWarnings("unused")
	public static void cmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		Player player = null;
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "[MultiPluxPlugin] No use for console. Player only.");
		}

		if (sender.hasPermission("multiplux.teleport")) {
			if (args.length == 0) {
				sender.sendMessage("[MultiPlux] Du musst jemanden angeben!");
			}
			if (args.length == 1) {
				String Name = args[0];
				if (Bukkit.getPlayerExact(Name) != null) {
					Player target = (Player) Bukkit.getPlayerExact(Name);
					((Player) sender).teleport(target);
					sender.sendMessage("[MultiPlux] Erfolgreich zu " + target.getDisplayName() + " teleportiert!");
				} else {
					sender.sendMessage("[MultiPlux] " + args[0] + " ist nicht online!");
				}
			} else if (args.length >= 2) {
				sender.sendMessage("[MultiPlux] Zu viele Argumente!");
			}
		}
	}
}