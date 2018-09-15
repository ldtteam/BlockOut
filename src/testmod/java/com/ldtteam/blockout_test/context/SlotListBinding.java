package com.ldtteam.blockout_test.context;

public class SlotListBinding
{

    private final Integer slotIndex;

    public SlotListBinding(final Integer slotIndex) {this.slotIndex = slotIndex;}

    public String getSlotIndexString()
    {
        return slotIndex.toString();
    }

    public Integer getSlotIndex()
    {
        return slotIndex;
    }
}
