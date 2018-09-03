package com.minecolonies.blockout.util.itemstack;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
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
                        if (pItemStack1.getItemDamage() == pItemStack2.getItemDamage())
                        {
                            // Then sort on NBT
                            if (pItemStack1.hasTagCompound() && pItemStack2.hasTagCompound())
                            {
                                // Then sort on stack size
                                if (ItemStack.areItemStackTagsEqual(pItemStack1, pItemStack2))
                                {
                                    return (pItemStack1.getCount() - pItemStack2.getCount());
                                }
                                else
                                {
                                    return (pItemStack1.getTagCompound().hashCode() - pItemStack2.getTagCompound().hashCode());
                                }
                            }
                            else if (!(pItemStack1.hasTagCompound()) && pItemStack2.hasTagCompound())
                            {
                                return -1;
                            }
                            else if (pItemStack1.hasTagCompound() && !(pItemStack2.hasTagCompound()))
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
                            return (pItemStack1.getItemDamage() - pItemStack2.getItemDamage());
                        }
                    }
                    else
                    {
                        return pItemStack1.getItem().getUnlocalizedName(pItemStack1).compareToIgnoreCase(pItemStack2.getItem().getUnlocalizedName(pItemStack2));
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
        ItemStack tClonedItemStack = pItemStack.copy();
        tClonedItemStack.setCount(pStackSize);
        return tClonedItemStack;
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
                    if (itemStack1.getItemDamage() == itemStack2.getItemDamage())
                    {
                        // Then sort on NBT
                        if (itemStack1.hasTagCompound() && itemStack2.hasTagCompound())
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
     * @return The number of items that was successfully merged.
     */
    public static int mergeStacks(@NotNull ItemStack mergeSource, @NotNull ItemStack mergeTarget, boolean doMerge)
    {
        if (!canStacksMerge(mergeSource, mergeTarget))
        {
            return 0;
        }
        int mergeCount = Math.min(mergeTarget.getMaxStackSize() - mergeTarget.getCount(), mergeSource.getAnimationsToGo());
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

    /**
     * Determines whether the given ItemStack should be considered equivalent
     * for crafting purposes.
     *
     * @param base          The stack to compare to.
     * @param comparison    The stack to compare.
     * @param oreDictionary true to take the Forge OreDictionary into account.
     * @return true if comparison should be considered a crafting equivalent for
     * base.
     */
    public static boolean isCraftingEquivalent(@NotNull ItemStack base, @NotNull ItemStack comparison, boolean oreDictionary)
    {
        if (isMatchingItem(base, comparison, true, false))
        {
            return true;
        }
        if (oreDictionary)
        {
            int[] idBase = OreDictionary.getOreIDs(base);
            return isCraftingEquivalent(idBase, comparison);
        }

        return false;
    }

    /**
     * Compares item id, and optionally damage and NBT. Accepts wildcard damage.
     * Ignores damage entirely if the item doesn't have subtypes.
     *
     * @param a           ItemStack
     * @param b           ItemStack
     * @param matchDamage Whether to check the damage value of the items
     * @param matchNBT    Whether to check the NBT tags on the items
     * @return Whether the items match
     */
    public static boolean isMatchingItem(@NotNull final ItemStack a, @NotNull final ItemStack b, final boolean matchDamage, final boolean matchNBT)
    {
        if (a.isEmpty() || b.isEmpty())
        {
            return false;
        }
        if (a.getItem() != b.getItem())
        {
            return false;
        }
        if (matchDamage && a.getHasSubtypes())
        {
            if (!isWildcard(a) && !isWildcard(b))
            {
                if (a.getItemDamage() != b.getItemDamage())
                {
                    return false;
                }
            }
        }
        if (matchNBT)
        {
            return a.getTagCompound() == null || a.getTagCompound().equals(b.getTagCompound());
        }
        return true;
    }

    public static boolean isCraftingEquivalent(@NotNull int[] oreIDs, @NotNull ItemStack comparison)
    {
        if (oreIDs.length > 0)
        {
            for (int id : oreIDs)
            {
                for (ItemStack itemstack : OreDictionary.getOres(OreDictionary.getOreName(id)))
                {
                    if (comparison.getItem() == itemstack.getItem() && (itemstack.getItemDamage() == OreDictionary.WILDCARD_VALUE
                                                                          || comparison.getItemDamage() == itemstack.getItemDamage()))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWildcard(@NotNull ItemStack stack)
    {
        return isWildcard(stack.getItemDamage());
    }

    public static boolean isWildcard(int damage)
    {
        return damage == -1 || damage == OreDictionary.WILDCARD_VALUE;
    }

    /**
     * Compares item id, damage and NBT. Accepts wildcard damage. Ignores damage
     * entirely if the item doesn't have subtypes.
     *
     * @param base       The stack to compare to.
     * @param comparison The stack to compare.
     * @return true if id, damage and NBT match.
     */
    public static boolean isMatchingItem(@NotNull ItemStack base, @NotNull ItemStack comparison)
    {
        return isMatchingItem(base, comparison, true, true);
    }

    public static boolean isMatchingOreDict(@NotNull final ItemStack a, @NotNull final ItemStack b)
    {
        if (hasOreDictEntry(a) && hasOreDictEntry(b))
        {
            int[] idA = OreDictionary.getOreIDs(a);
            int[] idB = OreDictionary.getOreIDs(b);
            return Arrays.equals(idA, idB);
        }
        return false;
    }

    public static boolean hasOreDictEntry(@NotNull final ItemStack a)
    {
        int[] oreIDs = OreDictionary.getOreIDs(a);
        return oreIDs != null;
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
              pItemStack.getUnlocalizedName(),
              pItemStack.getItemDamage(),
              pItemStack.hasTagCompound() ? pItemStack.getTagCompound().toString() : "");
        }

        return "null";
    }
}