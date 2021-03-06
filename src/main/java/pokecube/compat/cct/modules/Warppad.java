package pokecube.compat.cct.modules;

import dan200.computercraft.api.lua.LuaException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import pokecube.adventures.blocks.warppad.WarppadTile;
import thut.api.entity.ThutTeleporter.TeleDest;

public class Warppad extends BasePeripheral<WarppadTile>
{
    public static class Provider
    {
        private final WarppadTile tile;

        public Provider(final WarppadTile tile)
        {
            this.tile = tile;
        }

        public float[] getDest()
        {
            final TeleDest dest = this.tile.getDest();

            return new float[] { dest.loc.getPos().getX(), dest.loc.getPos().getY(), dest.loc.getPos().getZ() };
        }

        public boolean setDest(final int x, final int y, final int z) throws LuaException
        {
            final TeleDest dest = this.tile.getDest();
            dest.setPos(GlobalPos.of(this.tile.getWorld().getDimension().getType(), new BlockPos(x, y, z)));
            return true;
        }
    }

    private final Provider provider;

    public Warppad(final WarppadTile tile)
    {
        super(tile, "warppad");
        this.provider = new Provider(tile);
    }

    @Override
    public Object getTarget()
    {
        return this.provider;
    }

}
