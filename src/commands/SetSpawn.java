package commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SetSpawn {

	public static void cmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)){
			System.out.println("general.nospawnconsole");
		}
		Player p = (Player)sender;
		
		if(!p.hasPermission("spawn.setspawn")){
			p.sendMessage("Du hast keine Rechte!");
		}
		
		File ordner = new File("plugins/SetSpawn");
		File file = new File("plugins/SetSpawn/config.yml");
		if(!ordner.exists()){
			ordner.mkdir();
		}
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException e){
				p.sendMessage("Die Datei konnte nicht erstellt werden");
			}
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Location loc = p.getLocation();
		
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		double yaw = loc.getYaw();
		double pitch = loc.getPitch();
		String worldname = loc.getWorld().getName();
		
		cfg.set("X", x);
		cfg.set("Y", y);
		cfg.set("Z", z);
		cfg.set("Yaw", yaw);
		cfg.set("Pitch", pitch);
		cfg.set("Worldname", worldname);
		
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		p.sendMessage("Spawn wurde gesetzt");
		
	}
	
}