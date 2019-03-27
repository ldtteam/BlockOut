package com.ldtteam.blockout.util.itemstack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public final class ItemStackHelper
{

    @Nullable
    public static Comparator<IItemStack> COMPARATOR = new Comparator<IItemStack>()
    {
        public int compare(@NotNull IItemStack pItemStack1, @NotNull IItemStack pItemStack2)
        {
            if (!pItemStack1.isEmpty() && !pItemStack2.isEmpty())
            {
                // Sort on itemID
                if (IItem.getIdFromItem(pItemStack1.getItem()) - IItem.getIdFromItem(pItemStack2.getItem()) == 0)
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
                                if (IItemStack.areItemStackTagsEqual(pItemStack1, pItemStack2))
                                {
                                    return (pItemStack1.getCount() - pItemStack2.getCount());
                                }
                                else
                                {
                                    return (pItemStack1.write().hashCode() - pItemStack2.write().hashCode());
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
                        return pItemStack1.getItem().getTranslationKey(pItemStack1).compareToIgnoreCase(pItemStack2.getItem().getTranslationKey(pItemStack2));
                    }
                }
                else
                {
                    return IItem.getIdFromItem(pItemStack1.getItem()) - IItem.getIdFromItem(pItemStack2.getItem());
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
    public static IItemStack cloneItemStack(@NotNull IItemStack pItemStack, int pStackSize)
    {
        IItemStack tClonedItemStack = pItemStack.copy();
        tClonedItemStack.setCount(pStackSize);
        return tClonedItemStack;
    }

    public static boolean equals(@NotNull IItemStack pItemStack1, @NotNull IItemStack pItemStack2)
    {
        return (COMPARATOR.compare(pItemStack1, pItemStack2) == 0);
    }

    public static boolean equalsIgnoreStackSize(@NotNull IItemStack itemStack1, @NotNull IItemStack itemStack2)
    {
        if (!itemStack1.isEmpty() && !itemStack2.isEmpty())
        {
            // Sort on itemID
            if (IItem.getIdFromItem(itemStack1.getItem()) - IItem.getIdFromItem(itemStack2.getItem()) == 0)
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
                            return IItemStack.areItemStackTagsEqual(itemStack1, itemStack2);
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
    public static int mergeStacks(@NotNull IItemStack mergeSource, @NotNull IItemStack mergeTarget, boolean doMerge)
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
    public static boolean canStacksMerge(@NotNull IItemStack stack1, @NotNull IItemStack stack2)
    {
        if (stack1.isEmpty() || stack2.isEmpty())
        {
            return false;
        }
        if (!stack1.isItemEqual(stack2))
        {
            return false;
        }
        return IItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    public static int compare(@NotNull IItemStack pItemStack1, @NotNull IItemStack pItemStack2)
    {
        return COMPARATOR.compare(pItemStack1, pItemStack2);
    }

    public static String toString(@NotNull IItemStack pItemStack)
    {
        if (!pItemStack.isEmpty())
        {
            return String.format("%sxitemStack[%s@%s][%s]",
              pItemStack.getCount(),
              pItemStack.getTranslationKey(pItemStack),
              pItemStack.getItemDamage(),
              pItemStack.hasTagCompound() ? pItemStack.write().toString() : "");
        }

        return "null";
    }
}