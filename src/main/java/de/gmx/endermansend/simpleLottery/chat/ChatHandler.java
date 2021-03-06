package de.gmx.endermansend.simpleLottery.chat;

import de.gmx.endermansend.simpleLottery.config.ConfigHandler;
import de.gmx.endermansend.simpleLottery.game.RoundInterface;
import de.gmx.endermansend.simpleLottery.main.SimpleLottery;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Should be used for chat interaction to keep message formatting consistent. Defines some basic I/O-methods. More
 * specific methods are defined in a separate message class.
 */
public class ChatHandler {

    public Broadcast broadcast;

    public Log log;

    public Send send;

    public SendHelp sendHelp;

    public ConfigHandler config;

    protected String pluginTag;

    protected String errorTag;

    protected String listTag;

    protected HashMap<String, String> messages;

    protected HashMap<String, String> adminMessages;

    protected Logger logger;

    public ChatHandler(SimpleLottery main) {

        logger = main.getLogger();
        config = main.getConfigHandler();
        messages = config.get.messages();
        adminMessages = new HashMap<String, String>();

        InitializeChat.init(this);

        broadcast = new Broadcast(this);
        log = new Log(this);
        send = new Send(this);
        sendHelp = new SendHelp(this);

    }

    protected void broadcastMessage(String msg) {
        Bukkit.broadcastMessage(pluginTag + " " + msg);
    }

    protected void broadcastListEntry(String msg) {
        Bukkit.broadcastMessage(listTag + " " + msg);
    }

    protected void sendMessage(CommandSender receiver, String msg) {
        receiver.sendMessage(pluginTag + " " + msg);
    }

    protected void sendErrorMessage(CommandSender receiver, String msg) {
        receiver.sendMessage(errorTag + " " + msg);
    }

    protected void sendListEntry(CommandSender receiver, String msg) {
        receiver.sendMessage(listTag + " " + msg);
    }

    protected String getRoundStatus(RoundInterface round) {

        String statusText;

        RoundInterface.Status status = null;
        String roundNumber = "0";

        if (round != null) {
            status = round.getStatus();
            roundNumber = "" + round.getRoundNumber();
        }

        if (status == RoundInterface.Status.GAME_IS_RUNNING) {
            statusText = messages.get("round.status.running");
        } else if (status == RoundInterface.Status.GAME_IS_HALTED) {
            statusText = messages.get("round.status.halted");
        } else {
            statusText = messages.get("round.status.finished");
        }
        return messages.get("round.status.statusMessage").replace("<<roundNumber>>", roundNumber).replace("<<status>>", statusText);
    }

}
