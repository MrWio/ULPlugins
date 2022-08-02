package net.runelite.client.plugins.iquesterfree.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.iquesterfree.Task;
import net.runelite.client.plugins.iquesterfree.iQuesterFreePlugin;
import net.runelite.client.plugins.iutils.game.ItemQuantity;
import net.unethicalite.api.Interactable;
//import net.unethicalite.api.items.Bank;
import net.unethicalite.api.game.*;
import net.unethicalite.api.items.*;
import net.unethicalite.api.*;
import net.unethicalite.api.movement.Movement;
import net.unethicalite.api.entities.*;
import net.unethicalite.api.items.GrandExchange;
import net.unethicalite.api.utils.CoordUtils;
import net.unethicalite.api.widgets.Dialog;
import net.runelite.api.Client;
import net.unethicalite.api.widgets.Widgets;

import static net.runelite.api.ItemID.RING_OF_DUELING8;
import static net.runelite.api.ItemID.CAMELOT_TELEPORT;
import static net.runelite.api.ItemID.KNIFE;
import static net.runelite.api.ItemID.CANDLE;
import static net.runelite.api.ItemID.TINDERBOX;
import static net.runelite.api.ItemID.VARROCK_TELEPORT;


import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BootsofLightness extends Task {



    @Override
    public boolean validate() {
        return iQuesterFreePlugin.taskConfig.BootsofLightness();
    }

    @Override
    public String getTaskDescription() {
        iQuesterFreePlugin.questName = "Get Boots of Lightness";
        return "Starting " + iQuesterFreePlugin.questName;
    }

    @Override
    public List<ItemQuantity> requiredItems() {
        return null;
    }

    @Override
    public void run() {
        log.info("Doing quest step: {} {}", iQuesterFreePlugin.questName, iQuesterFreePlugin.status2);
        while (!Inventory.contains("Boots of lightness")) {
            if (Inventory.contains(TINDERBOX) && Inventory.contains(CAMELOT_TELEPORT) && Inventory.contains(KNIFE) && Inventory.contains(CANDLE)){
                iQuesterFreePlugin.status = "Lighting candle";
            } else if (Inventory.contains(33)){
                iQuesterFreePlugin.status = "Going to temple";
            } else {iQuesterFreePlugin.status = "Buying items"; }
            if (iQuesterFreePlugin.status == "Buying items") {
                if (!Inventory.contains(VARROCK_TELEPORT)) {
                    GrandExchange.buy(VARROCK_TELEPORT, 2, 2000, true, false);
                    game.tick(2);
                    return;
                }
                if (!Inventory.contains(CAMELOT_TELEPORT)) {
                    GrandExchange.buy(CAMELOT_TELEPORT, 1, 2000, true, false);
                    game.tick(2);
                    return;
                }
                if (!Inventory.contains(KNIFE)) {
                    GrandExchange.buy(KNIFE, 1, 2000, true, false);
                    game.tick(2);
                    return;
                }
                if (!Inventory.contains(CANDLE)) {
                    GrandExchange.buy(CANDLE, 1, 2000, true, false);
                    game.tick(2);
                    return;
                }
                if (!Inventory.contains(TINDERBOX)) {
                    GrandExchange.buy(TINDERBOX, 1, 2000, true, false);
                    game.tick(2);
                    Widgets.get(465, 2, 11).interact("Close");
                    game.tick(2);
                    Dialog.close();
                    game.tick(2);
                }
            }
            else if (iQuesterFreePlugin.status == "Lighting candle"){
                if (!Inventory.contains(33)){
                    Inventory.getFirst(TINDERBOX).useOn(Inventory.getFirst(CANDLE));
                    game.tick(2);
                    return;
                }
            }
            else if (iQuesterFreePlugin.status == "Going to temple"){
                WorldPoint ladderLoc = new WorldPoint(2677,3404,0);
                WorldPoint StairLoc = new WorldPoint(2649,9804,0);
                WorldPoint WebLoc = new WorldPoint(2653,9764,0);

                if (Players.getLocal().getWorldLocation().equals(ladderLoc)) {
                    iQuesterFreePlugin.status2 = "Trying to go down ladder";
                    TileObjects.getNearest(o -> o.hasAction("Climb-down")).interact("Climb-down");
                    game.tick(2);
                    return;
                } else {
                    iQuesterFreePlugin.status2 = "Going to ladderLoc";
                    Movement.walkTo(ladderLoc);
                    game.tick(1);
                }
                if (Players.getLocal().getWorldLocation().equals(StairLoc)) {
                    iQuesterFreePlugin.status2 ="Climb-down Stairs";
                    TileObjects.getNearest(o -> o.hasAction("Climb-down")).interact("Climb-down");
                    game.tick(2);
                    return ;
                } else
                    iQuesterFreePlugin.status2 ="Walking to StairLoc";
                Movement.walkTo(StairLoc);
                game.tick(2);

                if (Players.getLocal().getWorldLocation().equals(WebLoc)) {
                    if (TileObjects.getNearest("Web") != null){
                        iQuesterFreePlugin.status2 ="Slashing Web";
                        TileObjects.getNearest("Web").interact("Slash");
                        game.tick(3);
                        return;
                    }
                    if (TileObjects.getNearest("Slashed web") != null){
                        iQuesterFreePlugin.status2 ="Looting Boots";
                        TileItems.getNearest("Boots of lightness").pickup();
                        game.tick(2);
                        return;
                    }
                    return;
                } else if (!Players.getLocal().getWorldLocation().equals(WebLoc)) {
                    iQuesterFreePlugin.status2 ="Walking to WebLoc";
                    Movement.walkTo(WebLoc);
                    game.tick(2);

                }
            }

        }

            }

   // }
    private int questProgress() {
        return game.varp(31);
    }
}
