package de.gmx.endermansend.simpleLottery.main;

import de.gmx.endermansend.simpleLottery.config.ConfigHandler;
import de.gmx.endermansend.simpleLottery.chat.ChatHandler;
import de.gmx.endermansend.simpleLottery.game.LotteryCoordinatorManual;
import de.gmx.endermansend.simpleLottery.helper.LotteryCalculator;
import de.gmx.endermansend.simpleLottery.game.LotteryCoordinatorAuto;
import de.gmx.endermansend.simpleLottery.helper.LotteryCalculatorInterface;
import de.gmx.endermansend.simpleLottery.game.LotteryCoordinatorInterface;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleLottery extends JavaPlugin {

    private ConfigHandler config;
    private LotteryCoordinatorInterface lottery;
    private ChatHandler chat;
    private LotteryCalculatorInterface calc;

    @Override
    public void onEnable() {

        this.config = new ConfigHandler(this);
        this.chat = new ChatHandler(this);
        this.calc = new LotteryCalculator(config.get.numberRangeMin(), config.get.numberRangeMax());

        if (config.get.autoModeEnabled())
            lottery = new LotteryCoordinatorAuto(this);
        else
            lottery = new LotteryCoordinatorManual(this);

        this.getCommand("lottery").setExecutor(new SimpleLotteryCommandExecutor(this));

        chat.log.pluginEnabled();

    }

    @Override
    public void onDisable() {
        config.set.roundNumber(lottery.getRoundNumber());
        chat.log.pluginDisabled();
    }

    public ConfigHandler getConfigHandler() {
        return config;
    }

    public LotteryCoordinatorInterface getLottery() {
        return lottery;
    }

    public ChatHandler getChat() {
        return chat;
    }

    public LotteryCalculatorInterface getCalc() {
        return calc;
    }
}
