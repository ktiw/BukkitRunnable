package kyrla;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetHomeCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public SetHomeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        // Обычно это: "homes.<UUID_игрока>" ключ
        String path = "homes." + player.getUniqueId();

        // записываем локацию в конфиг
        plugin.getConfig().set(path, player.getLocation());

        // Без этого данные пропадут при перезагрузке
        plugin.saveConfig();

        player.sendMessage(ChatColor.GREEN + "Точка дома установлена! " + ChatColor.GRAY );
        return true;
    }
}