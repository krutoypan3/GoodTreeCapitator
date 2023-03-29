package ru.oganesyan.artem.listener

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import ru.oganesyan.artem.state.CheckState

class TreeListener: Listener {
    private var itemInMainHand: ItemStack? = null
    private var brokenBlock: Block? = null

    @EventHandler
    fun onBreak(blockBreakEvent: BlockBreakEvent) {
        brokenBlock = blockBreakEvent.block
        val player = blockBreakEvent.player
        //        float speed = broken_block.getBreakSpeed(player); // Скорость ломания блока игроком
        itemInMainHand = player.inventory.itemInMainHand
        if (CheckState.isAxe(itemInMainHand?.type) && !player.isSneaking) {
            if (CheckState.isLogOrMushroom(blockBreakEvent.block.type)) {
                cutTree(brokenBlock!!.location, blockBreakEvent.block.type)
            }
        }
    }


    private fun cutTree(location: Location, logType: Material) {
        for (x_change in -1..1) { // Эта хрень проверяет все рядом стоящие блоки вокруг сломанного блока
            for (y_change in -1..1) { // И срубает их
                for (z_change in -1..1) {
                    val currentLocation = Location(location.world, location.x + x_change, location.y + y_change, location.z + z_change)
                    if (CheckState.checkBlockOnLocation(
                            block = logType,
                            location = currentLocation,
                        )) {
                        if (CheckState.isMoreOneDurability(item = itemInMainHand)) {
                            val drop = ItemStack(logType, 1) //установка итема для дропа
                            currentLocation.block.type = Material.AIR //замена блока (который разрушали) воздухом
                            currentLocation.world!!.dropItem(brokenBlock!!.location, drop) //имитация дропа итема по координатам разрушеного блока
                            itemInMainHand!!.durability = (itemInMainHand!!.durability + 1).toShort() // +1 потому что эта хрень возвращает не прочность! а потеряное значение прочности
                            cutTree(currentLocation, logType)
                        } else {
                            break
                        }
                    }
                }
            }
        }
    }
}