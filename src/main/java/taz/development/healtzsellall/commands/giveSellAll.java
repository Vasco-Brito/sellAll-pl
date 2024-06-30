package taz.development.healtzsellall.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import taz.development.healtzsellall.Healtz_SellAll;
import taz.development.healtzsellall.items.obj.CustomItem;

public class giveSellAll implements CommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(giveSellAll.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmnd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (!player.hasPermission("tw.healtz.staff.giveSellAllItem")) {
            player.sendMessage("§cErro: §fNão tens permissão para isto");
            return false;
        }

        if (args.length == 0) {
            player.sendMessage("§cErro: §fEspecifique o ID do item.");
            return false;
        }

        CustomItem customItem = Healtz_SellAll.customItemMap.get(args[0].toLowerCase());

        if (customItem == null) {
            player.sendMessage("§cErro: §fItem não encontrado.");
            return false;
        }

        player.getInventory().addItem(customItem.getItem());
        player.sendMessage("§aSucesso: §fVocê recebeu o item!");

        return true;
    }
}
