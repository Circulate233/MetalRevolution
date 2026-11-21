package com.circulation.metal_revolution.client;

import java.util.ArrayList;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IStackPositioner;

public final class CookingTableOffsetPositioner implements IStackPositioner {

    @Override
    public ArrayList<PositionedStack> positionStacks(ArrayList<PositionedStack> ai) {
        if (ai.isEmpty()) return ai;
        var item = ai.get(0);
        item.relx = 19;
        item.rely = 36;
        for (int i = 0; i < 5 && i + 1 < ai.size(); ++i) {
            item = ai.get(i + 1);
            item.relx = 19 + i * 18;
            item.rely = 13;
        }
        return ai;
    }
}
