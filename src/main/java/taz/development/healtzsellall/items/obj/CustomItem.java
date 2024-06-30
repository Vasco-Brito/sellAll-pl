package taz.development.healtzsellall.items.obj;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import taz.development.healtzsellall.Healtz_SellAll;
import taz.development.healtzsellall.utils.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class CustomItem {

    public abstract String getName();

    public abstract Material getMaterial();

    public abstract List<String> getLore();

    public abstract Boolean isStackable();

    public abstract void handleLeftClick(Player player, ItemStack itemStack, PlayerInteractEvent event);

    public abstract void handleRightClick(Player player, ItemStack itemStack, PlayerInteractEvent event);

    public String getId() {
        return getClass().getSimpleName();
    }

    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(getMaterial(), 1);

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container =  itemMeta.getPersistentDataContainer();

        itemMeta.setDisplayName(Common.colorize(getName()));
        List<String> lore = new ArrayList<>();
        getLore().forEach(l -> lore.add(Common.colorize(l)));
        itemMeta.setLore(lore);

        container.set(Healtz_SellAll.specialItemKey, PersistentDataType.STRING, getId());
        if (isStackable()) {
            container.set(Healtz_SellAll.specialItemKeyId, PersistentDataType.STRING, UUID.randomUUID().toString());
        } else {
            container.set(Healtz_SellAll.specialItemKeyId, PersistentDataType.STRING, "none");
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}