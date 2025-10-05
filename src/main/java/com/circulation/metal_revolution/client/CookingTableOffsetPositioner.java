package com.circulation.metal_revolution.client;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IStackPositioner;

import java.util.ArrayList;

public class CookingTableOffsetPositioner implements IStackPositioner {

    @Override
    public ArrayList<PositionedStack> positionStacks(ArrayList<PositionedStack> ai) {
        var items = ai.toArray(new PositionedStack[0]);
        var item = items[0];
        item.relx = 19;
        item.rely = 36;
        for (int i = 0; i < 5; ++i) {
            item = items[i + 1];
            item.relx = 19 + i * 18;
            item.rely = 13;
        }
        return ai;
    }
}
