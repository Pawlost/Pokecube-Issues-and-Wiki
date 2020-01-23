package pokecube.core.world.gen.feature.scattered;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import pokecube.core.PokecubeCore;
import pokecube.core.utils.PokecubeSerializer;

public class PokecentreFeature extends ConfigStructure
{
    public static final Structure<NoFeatureConfig> START_BUILDING = new PokecentreFeature();

    public PokecentreFeature()
    {
        super(new ResourceLocation(PokecubeCore.MODID, "start_pokecentre"));
        this.setStructure(new ResourceLocation("pokecube", "scattered/start_pokecentre"));
    }

    @Override
    public boolean hasStartAt(final ChunkGenerator<?> chunkGen, final Random rand, final int chunkPosX,
            final int chunkPosZ)
    {
        if (PokecubeSerializer.getInstance().customData.contains("start_pokecentre")) return false;
        final BlockPos spawn = PokecubeCore.proxy.getServerWorld().getSpawnPoint();
        final BlockPos here = new BlockPos(chunkPosX * 16 + 8, spawn.getY(), chunkPosZ * 16 + 8);
        final boolean inRange = here.withinDistance(spawn, 16);
        if (inRange) PokecubeSerializer.getInstance().customData.putBoolean("start_pokecentre", true);
        return inRange;
    }
}