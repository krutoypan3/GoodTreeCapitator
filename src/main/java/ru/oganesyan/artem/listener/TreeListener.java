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

import java.util.*;
import java.util.stream.Collectors;

public class TreeListener implements Listener {
    private ItemStack itemInMainHand;
    private HashSet<Location> treeBlocks = new HashSet<>();

    @EventHandler
    void onBreak(BlockBreakEvent blockBreakEvent) {
        Block brokenBlock = blockBreakEvent.getBlock();
        Player player= blockBreakEvent.getPlayer();
        //        float speed = broken_block.getBreakSpeed(player); // Скорость ломания блока игроком
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (CheckState.isAxe(itemInMainHand.getType()) && !player.isSneaking()) {
            if (CheckState.isLogOrMushroom(blockBreakEvent.getBlock().getType())) {
                this.itemInMainHand = itemInMainHand;

                treeBlocks = new HashSet<>();
                calculateTreeLocation(brokenBlock.getLocation(), blockBreakEvent.getBlock().getType());

                List<Location> sortedTree = treeBlocks.stream()
                        .sorted(Comparator.comparing(Location::getY).reversed())
                        .toList();

                cutTree(sortedTree, blockBreakEvent.getBlock().getType());
            }
        }
    }

    /**
     * Собрать в СЕТ все координаты дерева по координате одного блока дерева
     */
    private void calculateTreeLocation(Location location, Material logType) {
        for (int x_change = -1; x_change <= 1; x_change++) {
            for (int y_change = -1; y_change <= 1; y_change++) {
                for (int z_change = -1; z_change <= 1; z_change++) {
                    Location currentLocation = new Location(location.getWorld(), location.getX()+x_change, location.getY()+y_change, location.getZ()+z_change);
                    if (CheckState.checkBlockOnLocation(logType, currentLocation) && !treeBlocks.contains(currentLocation)) {
                        treeBlocks.add(currentLocation);
                        calculateTreeLocation(currentLocation, logType);
                    }
                }
            }
        }
    }

    private void cutTree(List<Location> treeBlocks, Material logType) {
        for (Location currentLocation: treeBlocks) {
            if (CheckState.isMoreFiveDurability(itemInMainHand)) {
                ItemStack drop = new ItemStack(logType, 1); //установка итема для дропа
                currentLocation.getBlock().setType(Material.AIR); //замена блока (который разрушали) воздухом
                Objects.requireNonNull(currentLocation.getWorld()).dropItem(currentLocation, drop); //имитация дропа итема по координатам разрушеного блока
                itemInMainHand.setDurability((short) (itemInMainHand.getDurability() + 1));  // +1 потому что эта хрень возвращает не прочность! а потеряное значение прочности
            }
            else {
                break;
            }
        }
    }
}
