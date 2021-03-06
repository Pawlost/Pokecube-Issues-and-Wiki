package thut.bling;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GemRecipe extends SpecialRecipe
{
    public static final ResourceLocation BLINGTAG = new ResourceLocation("thut_bling", "bling");
    public static final ResourceLocation GEMTAG   = new ResourceLocation("thut_bling", "gems");

    public static final ResourceLocation IDTAG = new ResourceLocation("thut_bling:apply_gem");

    public static final IRecipeSerializer<GemRecipe> SERIALIZER = new SpecialRecipeSerializer<>(GemRecipe::new);

    public static boolean is(final ResourceLocation tag, final Item item)
    {
        final boolean tagged = ItemTags.getCollection().getOrCreate(tag).contains(item);
        return tagged;
    }

    public GemRecipe(final ResourceLocation idIn)
    {
        super(idIn);
    }

    @Override
    public boolean matches(final CraftingInventory inv, final World worldIn)
    {
        ItemStack bling = ItemStack.EMPTY;
        ItemStack gem = ItemStack.EMPTY;
        int n = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            final ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                n++;
                if (GemRecipe.is(GemRecipe.BLINGTAG, stack.getItem())) bling = stack;
                if (GemRecipe.is(GemRecipe.GEMTAG, stack.getItem())) gem = stack;
            }
        }
        if (n > 2) return false;

        // This is a gem removal recipe
        if (n == 1) return bling.hasTag() && bling.getTag().contains("gemTag");

        // Otherwise is a gem addition recipe
        return !bling.isEmpty() && !gem.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(final CraftingInventory inv)
    {
        ItemStack bling = ItemStack.EMPTY;
        ItemStack gem = ItemStack.EMPTY;
        int n = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            final ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                n++;
                if (GemRecipe.is(GemRecipe.BLINGTAG, stack.getItem())) bling = stack;
                if (GemRecipe.is(GemRecipe.GEMTAG, stack.getItem())) gem = stack;
            }
        }
        final ItemStack newBling = bling.copy();

        // This is a gem removal recipe
        if (n == 1)
        {
            final CompoundNBT tag = newBling.getTag().getCompound("gemTag");
            return ItemStack.read(tag);
        }
        else
        {
            final CompoundNBT tag = gem.write(new CompoundNBT());
            if (!newBling.hasTag()) newBling.setTag(new CompoundNBT());
            newBling.getTag().put("gemTag", tag);
        }
        return newBling;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(final CraftingInventory inv)
    {
        final NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        ItemStack bling = ItemStack.EMPTY;
        ItemStack gem = ItemStack.EMPTY;
        int blingIndex = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            final ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                if (GemRecipe.is(GemRecipe.BLINGTAG, stack.getItem()))
                {
                    bling = stack;
                    blingIndex = i;
                }
                if (GemRecipe.is(GemRecipe.GEMTAG, stack.getItem())) gem = stack;
            }
        }
        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            final ItemStack item = inv.getStackInSlot(i);
            if (item.hasContainerItem()) nonnulllist.set(i, item.getContainerItem());
        }
        if (gem.isEmpty())
        {
            bling.removeChildTag("gemTag");
            nonnulllist.set(blingIndex, bling.copy());
        }
        return nonnulllist;
    }

    @Override
    public boolean canFit(final int width, final int height)
    {
        return width * height > 1;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return GemRecipe.SERIALIZER;
    }
}
