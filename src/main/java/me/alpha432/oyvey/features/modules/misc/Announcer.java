package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.manager.FileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Announcer extends Module {

    private final Setting<Boolean> join = this.register(new Setting<Boolean>("Join", true));
    private final Setting<Boolean> leave = this.register(new Setting<Boolean>("Leave", true));
    private final Setting<Boolean> eat = this.register(new Setting<Boolean>("Eat", true));
    private final Setting<Boolean> walk = this.register(new Setting<Boolean>("Walk", true));
    private final Setting<Boolean> mine = this.register(new Setting<Boolean>("Mine", true));
    private final Setting<Boolean> place = this.register(new Setting<Boolean>("Place", true));
    private final Setting<Boolean> totem = this.register(new Setting<Boolean>("TotemPop", true));
    private final Setting<Boolean> random = this.register(new Setting<Boolean>("Random", true));
    private final Setting<Boolean> greentext = this.register(new Setting<Boolean>("Greentext", false));
    private final Setting<Boolean> loadFiles = this.register(new Setting<Boolean>("LoadFiles", false));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("SendDelay", 40));
    private final Setting<Integer> mindistance = this.register(new Setting<Integer>("Min Distance", 10, 1, 100));
    private static final String directory = "oyvey/announcer/";
    private Map<Action, ArrayList<String>> loadedMessages = new HashMap<Action, ArrayList<String>>();
    private Map<Action, Message> queue = new HashMap<Action, Message>();

    public Announcer() {
        super("Announcer", "How to get muted quick.", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onLoad() {
        this.loadMessages();
    }

    @Override
    public void onEnable() {
        this.loadMessages();
    }

    @Override
    public void onUpdate() {
        if (this.loadFiles.getValue().booleanValue()) {
            this.loadMessages();
            Command.sendMessage("<Announcer> Loaded messages.");
            this.loadFiles.setValue(false);
        }
    }

    public void loadMessages() {
        HashMap<Action, ArrayList<String>> newLoadedMessages = new HashMap<Action, ArrayList<String>>();
        for (Action action : Action.values()) {
            String fileName = directory + action.getName() + ".txt";
            List<String> fileInput = FileManager.readTextFileAllLines(fileName);
            Iterator<String> i = fileInput.iterator();
            ArrayList<String> msgs = new ArrayList<String>();
            while (i.hasNext()) {
                String string = i.next();
                if (string.replaceAll("\\s", "").isEmpty()) continue;
                msgs.add(string);
            }
            if (msgs.isEmpty()) {
                msgs.add(action.getStandartMessage());
            }
            newLoadedMessages.put(action, msgs);
        }
        this.loadedMessages = newLoadedMessages;
    }

    private String getMessage(Action action, int number, String info) {
        return "";
    }

    private Action getRandomAction() {
        Random rnd = new Random();
        int index = rnd.nextInt(7);
        int i = 0;
        for (Action action : Action.values()) {
            if (i == index) {
                return action;
            }
            ++i;
        }
        return Action.WALK;
    }


    public static enum Action {
        JOIN("Join", "Vitej _!"),
        LEAVE("Leave", "Cus _!"),
        EAT("Eat", "Prave jsem snedl % _ pomoci Guguhacku!"),
        WALK("Walk", "Prave jsem uletel % blocku jako ptacek pomoci Guguhacku!"),
        MINE("Mine", "Prave jsem vytezil % _ pomoci Guguhacku!"),
        PLACE("Place", "Prave jsem polozil % _ pomoci Guguhacku!"),
        TOTEM("Totem", "_ prave popnul % totemu pomoci Guguhacku!");

        private final String name;
        private final String standartMessage;

        private Action(String name, String standartMessage) {
            this.name = name;
            this.standartMessage = standartMessage;
        }

        public String getName() {
            return this.name;
        }

        public String getStandartMessage() {
            return this.standartMessage;
        }
    }

    public static class Message {
        public final Action action;
        public final String name;
        public final int amount;

        public Message(Action action, String name, int amount) {
            this.action = action;
            this.name = name;
            this.amount = amount;
        }
    }
}
