package net.runelite.client.plugins.iquesterfree.tasks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.iquesterfree.Task;
import net.runelite.client.plugins.iquesterfree.iQuesterFreePlugin;
import net.runelite.client.plugins.iutils.api.TeleportLocation;
import net.runelite.client.plugins.iutils.api.TeleportMethod;
import net.runelite.client.plugins.iutils.game.ItemQuantity;
import net.runelite.client.plugins.iutils.scene.Position;
import net.runelite.client.plugins.iutils.scene.RectangularArea;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.api.ItemID.STAMINA_POTION4;
import static net.runelite.client.plugins.iquesterfree.iQuesterFreePlugin.questName;
import static net.runelite.client.plugins.iquesterfree.iQuesterFreePlugin.taskConfig;

@Slf4j
public class RuneMystery extends Task {
    private static final RectangularArea lumbCastle2nd = new RectangularArea(3208,3219,3212,3223,1);
    private static final RectangularArea wizardBasement = new RectangularArea(3107, 9566, 3097, 9574, 0);
    private static final RectangularArea AuburyHut = new RectangularArea(3251,3400,3254,3401);

    @Override
    public boolean validate() {
        return iQuesterFreePlugin.taskConfig.RuneMystery() && questProgress() < 100;
    }

    @Override
    public String getTaskDescription() {
        questName = "Rune Mystery";
        return "Starting " + questName;
    }

    @Override
    public List<ItemQuantity> requiredItems() {
        List<ItemQuantity> items = new ArrayList<>();

        if (questProgress() == 0) {
            // items.add(new ItemQuantity(STAMINA_POTION4, 1));
            items.addAll(new TeleportMethod(game, TeleportLocation.WIZARD_TOWER, 2).getItems());
            items.addAll(new TeleportMethod(game, TeleportLocation.VARROCK_CENTRE, 3).getItems());
            items.addAll(new TeleportMethod(game, TeleportLocation.LUMBRIDGE, 2).getItems());

        }

        return items;
    }

    @Override
    public void run() {
        while (questProgress() < 7) {
            log.info("Doing quest step: {} {}", questName, questProgress());
            game.tick();
            switch (questProgress()) {
                case 0:
                    iQuesterFreePlugin.status = "Obtaining items";
                    //obtain(requiredItems());
                    iQuesterFreePlugin.status = "Talking to Duke";
                    chatNpc(lumbCastle2nd, "Duke Horacio", "Have you any quests for me?", "Yes.");
                    break;
                case 1:
                    iQuesterFreePlugin.status = "Talking to Sedridor";
                    chatNpc(wizardBasement, "Archmage Sedridor", "Okay, here you are.", "Go ahead.", "Yes, certainly");
                    break;
                case 3:
                    iQuesterFreePlugin.status = "Talking to Aubury";
                    chatNpc(AuburyHut, "Aubury", "I've been sent here with a package for you.");
                    break;
                    case 4:
                    //add double chat to aubry
                    break;
                case 5:
                    iQuesterFreePlugin.status = "Talking to Sedridor";
                    chatNpc(wizardBasement, "Archmage Sedridor", "I'd better get going.");

                    break;
                    case 6:
                    game.tick(4);
                     handleCompletion();
                    break;
            }
            if (questProgress() == 6) {
                game.tick(4);
                handleCompletion();
            }
        }
    }
    private int questProgress() {
        return game.varp(63);
    }
}
