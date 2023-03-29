package ru.oganesyan.artem.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import ru.oganesyan.artem.state.CheckState;

import java.util.Objects;

public class TreeListener implements Listener {
    private ItemStack itemInMainHand;

    @EventHandler
    void onBreak(BlockBreakEvent blockBreakEvent) {
        Block brokenBlock = blockBreakEvent.getBlock();
        Player player= blockBreakEvent.getPlayer();
        //        float speed = broken_block.getBreakSpeed(player); // Скорость ломания блока игроком
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (CheckState.isAxe(itemInMainHand.getType()) && !player.isSneaking()) {
            if (CheckState.isLogOrMushroom(blockBreakEvent.getBlock().getType())) {
                this.itemInMainHand = itemInMainHand;
                cutTree(brokenBlock.getLocation(), blockBreakEvent.getBlock().getType());
            }
        }
    }


    private void cutTree(Location location, Material logType) {
        for (int x_change = -1; x_change <= 1; x_change++){ // Эта хрень проверяет все рядом стоящие блоки вокруг сломанного блока
            for (int y_change = -1; y_change <= 1; y_change++){ // И срубает их
                for (int z_change = -1; z_change <= 1; z_change++){
                    Location currentLocation = new Location(location.getWorld(), location.getX()+x_change, location.getY()+y_change, location.getZ()+z_change);
                    if (CheckState.checkBlockOnLocation(logType,currentLocation)) {
                        if (CheckState.isMoreOneDurability(itemInMainHand)) {
                            ItemStack drop = new ItemStack(logType, 1); //установка итема для дропа
                            currentLocation.getBlock().setType(Material.AIR); //замена блока (который разрушали) воздухом
                            Objects.requireNonNull(currentLocation.getWorld()).dropItem(location, drop); //имитация дропа итема по координатам разрушеного блока
                            itemInMainHand.setDurability((short) (itemInMainHand.getDurability() + 1));  // +1 потому что эта хрень возвращает не прочность! а потеряное значение прочности
                            cutTree(currentLocation, logType);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }
}
