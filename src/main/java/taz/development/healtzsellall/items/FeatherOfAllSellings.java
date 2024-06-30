package taz.development.healtzsellall.items;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.bgsoftware.wildchests.api.objects.chests.StorageChest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import taz.development.healtzsellall.items.obj.CustomItem;

import java.util.Arrays;
import java.util.List;

public class FeatherOfAllSellings extends CustomItem {
    @Override
    public String getName() {
        return "§bPena de todas as vendas";
    }

    @Override
    public Material getMaterial() {
        return Material.FEATHER;
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList("&2Teste de variavel", "132");
    }

    @Override
    public Boolean isStackable() {
        return false;
    }

    @Override
    public void handleLeftClick(Player player, ItemStack itemStack, PlayerInteractEvent event) {
    }

    @Override
    public void handleRightClick(Player player, ItemStack itemStack, PlayerInteractEvent event) {

    }

}
