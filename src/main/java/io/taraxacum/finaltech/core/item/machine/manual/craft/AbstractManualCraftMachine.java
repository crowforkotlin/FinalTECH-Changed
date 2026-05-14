package io.taraxacum.finaltech.core.item.machine.manual.craft;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.taraxacum.finaltech.core.interfaces.RecipeItem;
import io.taraxacum.finaltech.core.item.machine.manual.AbstractManualMachine;
import io.taraxacum.finaltech.core.menu.manual.AbstractManualMachineMenu;
import io.taraxacum.finaltech.core.menu.manual.ManualCraftMachineMenu;
import io.taraxacum.finaltech.util.ConfigUtil;
import io.taraxacum.libs.slimefun.util.EnergyUtil;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Final_ROOT
 * @since 1.0
 */
public abstract class AbstractManualCraftMachine extends AbstractManualMachine implements RecipeItem, EnergyNetComponent {
    private final Map<Location, Integer> locationCountMap = new HashMap<>();
    private int countThreshold = ConfigUtil.getOrDefaultItemSetting(Slimefun.getTickerTask().getTickRate() * 2, this, "threshold");
    private int leftClickAmount = ConfigUtil.getOrDefaultItemSetting(1, this, "left-click-amount");
    private int rightClickAmount = ConfigUtil.getOrDefaultItemSetting(16, this, "right-click-amount");
    private int leftShiftClickAmount = ConfigUtil.getOrDefaultItemSetting(576, this, "left-shift-click-amount");
    private int rightShiftClickAmount = ConfigUtil.getOrDefaultItemSetting(2304, this, "right-shift-click-amount");

    private int capacity = ConfigUtil.getOrDefaultItemSetting(18432, this, "capacity");
    private int charge = ConfigUtil.getOrDefaultItemSetting(1, this, "charge");
    private int consume = ConfigUtil.getOrDefaultItemSetting(1, this, "consume");

    public AbstractManualCraftMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    protected BlockPlaceHandler onBlockPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {
                // TODO remove this
                Location location = blockPlaceEvent.getBlock().getLocation();
                BlockStorage.addBlockInfo(location, ManualCraftMachineMenu.KEY, "0");
                AbstractManualCraftMachine.this.setCharge(location, 0);
            }
        };
    }

    @Nonnull
    @Override
    protected AbstractManualMachineMenu newMachineMenu() {
        // TODO more beautiful code
        return new ManualCraftMachineMenu(this);
    }

    @Override
    protected void tick(@Nonnull Block block, @Nonnull SlimefunItem slimefunItem, @Nonnull Config config) {
        Location location = block.getLocation();
        int charge = this.getCharge(location);

        int intCharge = charge + this.charge;
        if (intCharge > this.capacity / 2) {
            intCharge /= 2;
        }

        this.setCharge(location, Math.min(intCharge, this.capacity));

        BlockMenu blockMenu = BlockStorage.getInventory(block);
        Inventory inv = blockMenu.toInventory();
        ManualCraftMachineMenu menu = (ManualCraftMachineMenu) this.getMachineMenu();

        if (blockMenu.hasViewer()) {
            if (!menu.verifyCount(location)) {
                return;
            }


            menu.updateInventory(inv, location);
        }
    }

    @Override
    protected void uniqueTick() {
        super.uniqueTick();
        this.locationCountMap.clear();
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getCharge(@Nonnull Location location) {
        return Integer.parseInt(EnergyUtil.getCharge(BlockStorage.getLocationInfo(location)));
    }

    @Override
    public void setCharge(@Nonnull Location location, int charge) {
        EnergyUtil.setCharge(BlockStorage.getLocationInfo(location), Math.max(0, Math.min(charge, this.capacity)));
    }

    public Map<Location, Integer> getLocationCountMap() {
        return locationCountMap;
    }

    public int getConsume() {
        return consume;
    }

    public int getCountThreshold() {
        return countThreshold;
    }

    public int getLeftClickAmount() {
        return leftClickAmount;
    }

    public int getRightClickAmount() {
        return rightClickAmount;
    }

    public int getLeftShiftClickAmount() {
        return leftShiftClickAmount;
    }

    public int getRightShiftClickAmount() {
        return rightShiftClickAmount;
    }
}
