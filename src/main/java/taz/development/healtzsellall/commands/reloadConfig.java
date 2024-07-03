package taz.development.healtzsellall.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import taz.development.healtzsellall.Healtz_SellAll;

public class reloadConfig implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("tw.healtz.staff.reloadConfig")) {
                Healtz_SellAll.getInstance().reloadPluginConfig();
                player.sendMessage("§aConfiguração recarregada com sucesso!");
            } else {
                player.sendMessage("§cVocê não tem permissão para executar este comando.");
            }
        } else {
            Healtz_SellAll.getInstance().reloadPluginConfig();
            sender.sendMessage("Configuração recarregada com sucesso!");
        }
        return true;
    }
}
