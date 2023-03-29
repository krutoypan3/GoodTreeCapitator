package ru.oganesyan.artem.state

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object CheckState {

    /**
     * Проверяет материал на соответствие типу "Топор"
     * @param material - материал
     * @return true - если это топор, false - если не топор
     */
    fun isAxe(material: Material?) = AXE_LIST.contains(material)

    /**
     * Проверяет материал на соответствие типу "Бревно или гриб"
     * @param material - материал
     * @return true - если это бревно или гриб, false - если нет
     */
    fun isLogOrMushroom(material: Material?) = LOGS_AND_MUSHROOMS_BLOCKS.contains(material)

    /**
     * Проверить наличие блока в заданной локации
     * @param location - локация блока
     * @param block - блок
     * @return true - если заданный блок находится в заданной позиции, false - в других случаях
     */
    fun checkBlockOnLocation(block: Material?, location: Location?) = location?.block?.type == block && block != null

    /**
     * Проверить наличие > 1 прочности у предмета
     * @param item - предмет
     * @return true - если запас прочности предмета > 1, false - если равен единице, либо не имеет прочности
     */
    fun isMoreOneDurability(item: ItemStack?) = (item!!.type.maxDurability - item.durability) > 1

    /**
     * Массив с топорами
     */
    val AXE_LIST = arrayListOf(
        Material.DIAMOND_AXE,           // Алмазный
        Material.GOLDEN_AXE,            // Золотой
        Material.IRON_AXE,              // Железный
        Material.STONE_AXE,             // Каменный
        Material.NETHERITE_AXE,         // Незеритовый
        Material.WOODEN_AXE,            // Деревянный
    )

    /**
     * Массив с бревнами и грибами
     */
    val LOGS_AND_MUSHROOMS_BLOCKS = arrayListOf(
        Material.BIRCH_LOG,             // Береза
        Material.ACACIA_LOG,            // Акация
        Material.CHERRY_LOG,            // Вишня
        Material.OAK_LOG,               // Дуб
        Material.JUNGLE_LOG,            // Тропическое
        Material.DARK_OAK_LOG,          // Темное
        Material.MANGROVE_LOG,          // Мангровое
        Material.SPRUCE_LOG,            // Саванное
        Material.CRIMSON_FUNGUS,        // Кримзон (ад)
        Material.WARPED_FUNGUS,         // Искажение (ад)
        Material.RED_MUSHROOM_BLOCK,    // Красный гриб
        Material.BROWN_MUSHROOM_BLOCK,  // Коричневый гриб
        Material.MUSHROOM_STEM,         // Ножка гриба
    )
}
