package kyrla;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HomeCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public HomeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        // Есть ли дом проверка
        String path = "homes." + player.getUniqueId();

        // Если в конфиге нет записи для этого игрока
        if (!plugin.getConfig().contains(path)) {
            player.sendMessage(ChatColor.RED + "У тебя нет дома! Напиши /sethome");
            return true;
        }

        // Достаем сохраненную локацию из файла (Финальная точка назначения)
        Location homeLocation = plugin.getConfig().getLocation(path);

        // Точка где игрок стоит сейчас проверка
        Location startLocation = player.getLocation();

        player.sendMessage(ChatColor.YELLOW + "Замри! Телепортация через 3 секунды...");

        new BukkitRunnable() {
            int seconds = 3;

            @Override
            public void run() {
                if (!player.isOnline() || player.getLocation().distance(startLocation) > 1.0) {
                    player.sendMessage(ChatColor.RED + "Движение! Отмена.");
                    this.cancel();
                    return;
                }

                if (seconds <= 0) {
                    // ТЕЛЕПОРТИРУЕМ В ДОМ
                    player.teleport(homeLocation);

                    player.sendMessage(ChatColor.GREEN + "Успешно!");
                    player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                    this.cancel();
                    return;
                }

                player.sendMessage(ChatColor.YELLOW + "Телепортация через " + seconds + "...");
                seconds--;
            }
        }.runTaskTimer(plugin, 0L, 20L);

        return true;
    }
}