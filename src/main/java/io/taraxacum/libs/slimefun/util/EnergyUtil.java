package io.taraxacum.libs.slimefun.util;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.taraxacum.common.util.JavaUtil;
import io.taraxacum.common.util.StringNumberUtil;
import io.taraxacum.finaltech.util.ConstantTableUtil;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.Objects;

public class EnergyUtil {
    @Nonnull
    public static String getCharge(@Nonnull Location location) {
        Config config = BlockStorage.getLocationInfo(location);
        if (config == null) {
            return "0";
        }
        SlimefunItem it = SlimefunItem.getById(config.getString(ConstantTableUtil.CONFIG_ID));
        if (it != null)
            if (it instanceof EnergyNetComponent)
                return String.valueOf(((EnergyNetComponent) it).getCharge(location));
        return getCharge(config);
    }

    @Nonnull
    public static String getCharge(@Nonnull Config config) {
        return Objects.requireNonNull(JavaUtil.getFirstNotNull(config.getString(ConstantTableUtil.CONFIG_CHARGE), StringNumberUtil.ZERO));
    }

    public static void setCharge(@Nonnull Location location, @Nonnull String energy) {
        Config config = BlockStorage.getLocationInfo(location);
        if (config == null) {
            return;
        }
        SlimefunItem it = SlimefunItem.getById(config.getString(ConstantTableUtil.CONFIG_ID));
        if (it != null)
            if (it instanceof EnergyNetComponent)
                ((EnergyNetComponent) it).setCharge(location, Integer.parseInt(energy));
            else
                setCharge(config, energy);
        else
            setCharge(config, energy);
    }

    public static void setCharge(@Nonnull Location location, int energy) {
        Config config = BlockStorage.getLocationInfo(location);
        if (config == null) {
            return;
        }
        SlimefunItem it = SlimefunItem.getById(config.getString(ConstantTableUtil.CONFIG_ID));
        if (it != null)
            if (it instanceof EnergyNetComponent)
                ((EnergyNetComponent) it).setCharge(location, energy);
            else
                setCharge(config, energy);
        else
            setCharge(config, energy);
    }

    public static void setCharge(@Nonnull Config config, @Nonnull String energy) {
        config.setValue(ConstantTableUtil.CONFIG_CHARGE, energy);
    }

    public static void setCharge(@Nonnull Config config, int energy) {
        config.setValue(ConstantTableUtil.CONFIG_CHARGE, String.valueOf(energy));
    }


}
