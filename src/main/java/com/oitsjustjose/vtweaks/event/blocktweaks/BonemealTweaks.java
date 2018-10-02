package com.oitsjustjose.vtweaks.event.blocktweaks;

import com.oitsjustjose.vtweaks.util.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class BonemealTweaks
{
    @SubscribeEvent
    public void registerTweak(RightClickBlock event)
    {
        // Checks if feature is enabled
        if (!ModConfig.blockTweaks.enableBonemealTweak)
        {
            return;
        }

        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();
        Block testFor = world.getBlockState(event.getPos()).getBlock();
        Random rand = world.rand;

        if (!player.getHeldItemMainhand().isEmpty())
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if (heldItem.getItemDamage() == 15 && heldItem.getItem() == Items.DYE)
            {
                if (testFor == Blocks.CACTUS || testFor == Blocks.REEDS)
                {
                    if (world.getBlockState(event.getPos().down(2)).getBlock() != testFor
                            && world.isAirBlock(event.getPos().up()))
                    {
                        player.swingArm(EnumHand.MAIN_HAND);
                        for (int i = 0; i < 8; i++)
                        {
                            spawnParticles(world, rand, testFor, event.getPos());
                        }
                        if (!world.isRemote)
                        {
                            if (!player.capabilities.isCreativeMode)
                            {
                                heldItem.setCount(heldItem.getCount() - 1);
                            }
                            world.setBlockState(event.getPos().up(), world.getBlockState(event.getPos()), 2);
                        }
                    }
                }
            }
        }
    }

    private void spawnParticles(World world, Random rand, Block testFor, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        double d0 = rand.nextGaussian() * 0.02D;
        double d1 = rand.nextGaussian() * 0.02D;
        double d2 = rand.nextGaussian() * 0.02D;
        world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double) ((float) pos.getX() + rand.nextFloat()),
                (double) pos.getY() + (double) rand.nextFloat() * state.getBoundingBox(world, pos).maxY,
                (double) ((float) pos.getZ() + rand.nextFloat()), d0, d1, d2);
    }
}