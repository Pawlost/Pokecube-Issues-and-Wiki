package pokecube.core.interfaces.capabilities.impl;

import java.util.Random;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import pokecube.core.PokecubeCore;
import pokecube.core.handlers.events.SpawnHandler;
import pokecube.core.interfaces.IPokemobUseable;
import pokecube.core.interfaces.Nature;
import pokecube.core.interfaces.pokemob.ai.CombatStates;
import pokecube.core.interfaces.pokemob.ai.GeneralStates;
import pokecube.core.items.berries.ItemBerry;
import thut.api.item.ItemList;
import thut.api.maths.Vector3;
import thut.lib.ItemStackTools;

public abstract class PokemobHungry extends PokemobMoves
{
    public static final ResourceLocation LEPPABERRY = new ResourceLocation(PokecubeCore.MODID, "berry_leppa");

    @Override
    public void eat(final Object e)
    {
        int hungerValue = PokecubeCore.getConfig().pokemobLifeSpan / 4;
        ItemStack item = e instanceof ItemStack ? (ItemStack) e : ItemStack.EMPTY;
        if (e instanceof ItemEntity) item = ((ItemEntity) e).getItem();
        if (!item.isEmpty())
        {
            final IPokemobUseable usable = IPokemobUseable.getUsableFor(item);
            if (usable != null)
            {
                final ActionResult<ItemStack> result = usable.onUse(this, item, this.getEntity());
                if (result.getType() == ActionResultType.SUCCESS) ItemStackTools.addItemStackToInventory(result
                        .getResult(), this.getInventory(), 1);
            }
            if (ItemList.is(PokemobHungry.LEPPABERRY, item)) hungerValue *= 2;
            if (item.getItem() instanceof ItemBerry)
            {
                final ItemBerry berry = (ItemBerry) item.getItem();
                int weight = Nature.getBerryWeight(berry.type.index, this.getNature());
                final int current = this.getHappiness();
                final HappinessType type = HappinessType.BERRY;
                if (current < 100) weight *= type.low / 10f;
                else if (current < 200) weight *= type.mid / 10f;
                else weight *= type.high / 10f;
                this.addHappiness(weight);
            }
        }
        this.setHungerTime(this.getHungerTime() - hungerValue);
        this.hungerCooldown = 0;
        this.setCombatState(CombatStates.HUNTING, false);
        if (!this.getEntity().isAlive()) return;
        final float missingHp = this.getMaxHealth() - this.getHealth();
        final float toHeal = this.getHealth() + Math.max(1, missingHp * 0.25f);
        this.setHealth(Math.min(toHeal, this.getMaxHealth()));
        // Make wild pokemon level up naturally to their cap, to allow wild
        // hatches
        if (!this.getGeneralState(GeneralStates.TAMED))
        {
            final int exp = SpawnHandler.getSpawnXp(this.getEntity().getEntityWorld(), this.here.set(this.getEntity()),
                    this.getPokedexEntry());
            if (this.getExp() < exp)
            {
                final int n = new Random().nextInt(exp - this.getExp()) / 3 + 1;
                this.setExp(this.getExp() + n, true);
            }
        }
    }

    @Override
    public boolean eatsBerries()
    {
        return this.getPokedexEntry().foods[5];
    }

    @Override
    public boolean filterFeeder()
    {
        return this.getPokedexEntry().foods[6];
    }

    @Override
    public int getFlavourAmount(final int index)
    {
        return this.dataSync().get(this.params.FLAVOURS[index]);
    }

    @Override
    public int getHungerCooldown()
    {
        return this.hungerCooldown;
    }

    @Override
    public int getHungerTime()
    {
        return this.dataSync().get(this.params.HUNGERDW);
    }

    @Override
    public Vector3 getMobSizes()
    {
        return this.sizes.set(this.getPokedexEntry().width, this.getPokedexEntry().height, this
                .getPokedexEntry().length).scalarMult(this.getSize());
    }

    /** @return does this pokemon hunt for food */
    @Override
    public boolean isCarnivore()
    {
        return this.getPokedexEntry().hasPrey();
    }

    @Override
    public boolean isElectrotroph()
    {
        return this.getPokedexEntry().foods[2];
    }

    /** @return Does this pokemon eat grass */
    @Override
    public boolean isHerbivore()
    {
        return this.getPokedexEntry().foods[3];
    }

    @Override
    public boolean isLithotroph()
    {
        return this.getPokedexEntry().foods[1];
    }

    @Override
    public boolean isPhototroph()
    {
        return this.getPokedexEntry().foods[0];
    }

    @Override
    public boolean neverHungry()
    {
        return this.getPokedexEntry().foods[4];
    }

    @Override
    public void noEat(final Object e)
    {
        if (e != null) this.addHappiness(-10);
    }

    @Override
    public void setFlavourAmount(final int index, final int amount)
    {
        this.dataSync().set(this.params.FLAVOURS[index], amount);
    }

    @Override
    public void setHungerCooldown(final int hungerCooldown)
    {
        this.hungerCooldown = hungerCooldown;
    }

    @Override
    public void setHungerTime(final int hungerTime)
    {
        this.dataSync().set(this.params.HUNGERDW, hungerTime);
    }
}
