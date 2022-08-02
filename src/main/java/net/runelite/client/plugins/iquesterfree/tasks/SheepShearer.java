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

import static net.runelite.api.ItemID.BALL_OF_WOOL;
import static net.runelite.client.plugins.iquesterfree.iQuesterFreePlugin.questName;
import static net.runelite.client.plugins.iquesterfree.iQuesterFreePlugin.taskConfig;

@Slf4j
public class SheepShearer extends Task {
    private static final RectangularArea FRED = new RectangularArea(3191, 3271, 3188, 3274);

    @Override
    public boolean validate() {
        return taskConfig.SheepShearer() && questProgress() < 100;
    }

    @Override
    public String getTaskDescription() {
        questName = "Sheep Shearer";
        return "Starting " + questName;
    }

    @Override
    public List<ItemQuantity> requiredItems() {
        List<ItemQuantity> items = new ArrayList<>();

        if (questProgress() == 0) {
            items.add(new ItemQuantity(BALL_OF_WOOL, 20));
            items.addAll(new TeleportMethod(game, TeleportLocation.VARROCK_CENTRE, 2).getItems());
            items.addAll(new TeleportMethod(game, TeleportLocation.LUMBRIDGE, 1).getItems());

        }

        return items;
    }

    @Override
    public void run() {
        while (questProgress() < 21) {
            log.info("Doing quest step: {} {}", questName, questProgress());
            game.tick();
            switch (questProgress()) {
                case 0:
                    iQuesterFreePlugin.status = "Obtaining items";
                    obtain(requiredItems());
                    iQuesterFreePlugin.status = "Talking to Fred";
                    chatNpc(FRED, "Fred the Farmer", "I'm looking for a quest.", "Yes.");
                    break;
                case 21:
                    handleCompletion();
                    break;

            }
            if (questProgress() == 100) {
                handleCompletion();
            }
        }
    }
    private int questProgress() {
        return game.varp(179);
    }
    }
