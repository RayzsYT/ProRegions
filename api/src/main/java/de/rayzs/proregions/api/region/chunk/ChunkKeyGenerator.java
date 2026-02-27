package de.rayzs.proregions.api.region.chunk;

public class ChunkKeyGenerator {

    /**
     * This beautiful peace of code here (which I actually found on the SpigotMC forum)
     * generates a key for a chunk, much faster than the from Bukkit introduced getChunkKey
     * method. Pretty useful to find regions near a certain chunk, without using the chunk itself,
     * and instead just this key right here.
     *
     * Where I got this code from: https://www.spigotmc.org/threads/identify-chunks.488632/
     *
     * @param x x coordinate of location. (NOT CHUNK X! LOCATION X!)
     * @param z z coordinate of location. (NOT CHUNK X! LOCATION X!)
     * @return Returns a unique key for a chunk.
     */
    public static long getChunkKey(final int x, final int z) {
        return (z >> 4) ^ ((long) (x >> 4) << 32);
    }
}
