package net.runelite.client.plugins.iquesterfree.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.iquesterfree.Task;
import net.runelite.client.plugins.iquesterfree.iQuesterFreePlugin;
import net.runelite.client.plugins.iutils.api.TeleportLocation;
import net.runelite.client.plugins.iutils.api.TeleportMethod;
import net.runelite.client.plugins.iutils.game.ItemQuantity;
import net.runelite.client.plugins.iutils.scene.RectangularArea;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.api.ItemID.BLACK_BEAD;
import static net.runelite.api.ItemID.WHITE_BEAD;
import static net.runelite.api.ItemID.RED_BEAD;
import static net.runelite.api.ItemID.YELLOW_BEAD;
import static net.runelite.client.plugins.iquesterfree.iQuesterFreePlugin.questName;
import static net.runelite.client.plugins.iquesterfree.iQuesterFreePlugin.taskConfig;

@Slf4j
public class ImpCatcher extends Task {
    private static final RectangularArea Mizgog = new RectangularArea(3105, 3162, 3103, 3165, 2);
    
    @Override
    public boolean validate() {
        return taskConfig.ImpCatcher() && questProgress() < 100;
    }

    @Override
    public String getTaskDescription() {
        questName = "Imp Catcher";
        return "Starting " + questName;
    }

    @Override
    public List<ItemQuantity> requiredItems() {
        List<ItemQuantity> items = new ArrayList<>();

        if (questProgress() == 0) {
            items.add(new ItemQuantity(BLACK_BEAD, 1));
            items.add(new ItemQuantity(WHITE_BEAD, 1));
            items.add(new ItemQuantity(RED_BEAD, 1));
            items.add(new ItemQuantity(YELLOW_BEAD, 1));
            items.addAll(new TeleportMethod(game, TeleportLocation.VARROCK_CENTRE, 2).getItems());
            items.addAll(new TeleportMethod(game, TeleportLocation.WIZARD_TOWER, 2).getItems());

        }

        return items;
    }

    @Override
    public void run() {
        while (questProgress() < 1) {
            log.info("Doing quest step: {} {}", questName, questProgress());
            game.tick();
            switch (questProgress()) {
                case 0:
                    iQuesterFreePlugin.status = "Obtaining items";
                    obtain(requiredItems());
                    iQuesterFreePlugin.status = "Talking to Mizgog";
                    chatNpc(Mizgog, "Wizard Mizgog", "Give me a quest please.", "Yes.");
                    break;
                case 1:
                    break;
            }
            if (questProgress() == 1) {
                handleCompletion();
            }
        }
    }
    private int questProgress() {
        return game.varp(160);
    }
}