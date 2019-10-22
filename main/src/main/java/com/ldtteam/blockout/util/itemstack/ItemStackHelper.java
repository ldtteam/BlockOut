package com.ldtteam.blockout.util.itemstack;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public final class ItemStackHelper
{

    @Nullable
    public static Comparator<ItemStack> COMPARATOR = new Comparator<ItemStack>()
    {
        public int compare(@NotNull ItemStack pItemStack1, @NotNull ItemStack pItemStack2)
        {
            if (!pItemStack1.isEmpty() && !pItemStack2.isEmpty())
            {
                // Sort on itemID
                if (Item.getIdFromItem(pItemStack1.getItem()) - Item.getIdFromItem(pItemStack2.getItem()) == 0)
                {
                    // Sort on item
                    if (pItemStack1.getItem() == pItemStack2.getItem())
                    {
                        // Then sort on meta
                        if (pItemStack1.getDamage() == pItemStack2.getDamage())
                        {
                            // Then sort on NBT
                            if (pItemStack1.hasTag() && pItemStack2.hasTag())
                            {
                                // Then sort on stack size
                                if (ItemStack.areItemStackTagsEqual(pItemStack1, pItemStack2))
                                {
                                    return (pItemStack1.getCount() - pItemStack2.getCount());
                                }
                                else
                                {
                                    return (pItemStack1.write(new CompoundNBT()).hashCode() - pItemStack2.write(new CompoundNBT()).hashCode());
                                }
                            }
                            else if (!(pItemStack1.hasTag()) && pItemStack2.hasTag())
                            {
                                return -1;
                            }
                            else if (pItemStack1.hasTag() && !(pItemStack2.hasTag()))
                            {
                                return 1;
                            }
                            else
                            {
                                return (pItemStack1.getCount() - pItemStack2.getCount());
                            }
                        }
                        else
                        {
                            return (pItemStack1.getDamage() - pItemStack2.getDamage());
                        }
                    }
                    else
                    {
                        return pItemStack1.getItem().getTranslationKey(pItemStack1).compareToIgnoreCase(pItemStack2.getItem().getTranslationKey(pItemStack2));
                    }
                }
                else
                {
                    return Item.getIdFromItem(pItemStack1.getItem()) - Item.getIdFromItem(pItemStack2.getItem());
                }
            }
            else if (!pItemStack1.isEmpty())
            {
                return -1;
            }
            else if (!pItemStack2.isEmpty())
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    };

    private ItemStackHelper()
    {
        throw new IllegalArgumentException("Utility Class");
    }

    @NotNull
    public static ItemStack cloneItemStack(@NotNull ItemStack pItemStack, int pStackSize)
    {
        ItemStack clonedStack = pItemStack.copy();
        clonedStack.setCount(pStackSize);
        return clonedStack;
    }

    public static boolean equals(@NotNull ItemStack pItemStack1, @NotNull ItemStack pItemStack2)
    {
        return (COMPARATOR.compare(pItemStack1, pItemStack2) == 0);
    }

    public static boolean equalsIgnoreStackSize(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2)
    {
        if (!itemStack1.isEmpty() && !itemStack2.isEmpty())
        {
            // Sort on itemID
            if (Item.getIdFromItem(itemStack1.getItem()) - Item.getIdFromItem(itemStack2.getItem()) == 0)
            {
                // Sort on item
                if (itemStack1.getItem() == itemStack2.getItem())
                {
                    // Then sort on meta
                    if (itemStack1.getDamage() == itemStack2.getDamage())
                    {
                        // Then sort on NBT
                        if (itemStack1.hasTag() && itemStack2.hasTag())
                        {
                            // Then sort on stack size
                            return ItemStack.areItemStackTagsEqual(itemStack1, itemStack2);
                        }
                        else
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Merges mergeSource into mergeTarget
     *
     * @param mergeSource - The stack to merge into mergeTarget, this stack is not
     *                    modified
     * @param mergeTarget - The target merge, this stack is modified if doMerge is set
     * @param doMerge     - To actually do the merge
     * @return The number of item that was successfully merged.
     */
    public static int mergeStacks(@NotNull ItemStack mergeSource, @NotNull ItemStack mergeTarget, boolean doMerge)
    {
        if (!canStacksMerge(mergeSource, mergeTarget))
        {
            return 0;
        }
        int mergeCount = Math.min(mergeTarget.getMaxStackSize() - mergeTarget.getCount(), mergeSource.getCount());
        if (mergeCount < 1)
        {
            return 0;
        }
        if (doMerge)
        {
            mergeTarget.grow(mergeCount);
        }
        return mergeCount;
    }

    /**
     * Checks if two ItemStacks are identical enough to be merged
     *
     * @param stack1 - The first stack
     * @param stack2 - The second stack
     * @return true if stacks can be merged, false otherwise
     */
    public static boolean canStacksMerge(@NotNull ItemStack stack1, @NotNull ItemStack stack2)
    {
        if (stack1.isEmpty() || stack2.isEmpty())
        {
            return false;
        }
        if (!stack1.isItemEqual(stack2))
        {
            return false;
        }
        return ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    public static int compare(@NotNull ItemStack pItemStack1, @NotNull ItemStack pItemStack2)
    {
        return COMPARATOR.compare(pItemStack1, pItemStack2);
    }

    public static String toString(@NotNull ItemStack pItemStack)
    {
        if (!pItemStack.isEmpty())
        {
            return String.format("%sxitemStack[%s@%s][%s]",
              pItemStack.getCount(),
              pItemStack.getTranslationKey(),
              pItemStack.getDamage(),
              pItemStack.hasTag() ? pItemStack.write(new CompoundNBT()).toString() : "");
        }

        return "null";
    }
}