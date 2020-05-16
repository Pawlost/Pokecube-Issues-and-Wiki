package pokecube.legends.conditions;

import net.minecraft.entity.Entity;
import pokecube.core.database.Database;
import pokecube.core.database.PokedexEntry;
import pokecube.core.database.stats.CaptureStats;
import pokecube.core.interfaces.IPokemob;

public class Eternatus extends Condition
{
    @Override
    public boolean canCapture(final Entity trainer, final IPokemob pokemon)
    {
        if (!super.canCapture(trainer, pokemon)) return false;
        final boolean dialga = CaptureStats.getTotalNumberOfPokemobCaughtBy(trainer.getUniqueID(), Database.getEntry(
                "zacian")) > 0;
        final boolean palkia = CaptureStats.getTotalNumberOfPokemobCaughtBy(trainer.getUniqueID(), Database.getEntry(
                "zamazenta")) > 0;

        final String name = "Zacian, Zamazenta";

        if (dialga && palkia) return true;
        if (pokemon != null && !trainer.getEntityWorld().isRemote)
        {
            this.sendNoTrust(trainer);
            this.sendLegendExtra(trainer, name);
        }
        return false;
    }

    @Override
    public PokedexEntry getEntry()
    {
        return Database.getEntry("eternatus");
    }

}
