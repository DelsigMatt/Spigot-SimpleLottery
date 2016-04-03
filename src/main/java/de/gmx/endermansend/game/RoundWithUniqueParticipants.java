package de.gmx.endermansend.game;

import org.bukkit.entity.Player;

public class RoundWithUniqueParticipants extends RoundWithDefaultSettings {

    public RoundWithUniqueParticipants(int roundNumber) {
        super(roundNumber);
    }

    /**
     * Allows only one entry per player
     *
     * @param player       Initiator of the lottery entry
     * @param ticketNumber Lottery number the player chose
     * @return true if entry could be added
     */
    public boolean addLotteryEntry(Player player, int ticketNumber) {

        for (Ticket t : tickets) {
            if (t.getOwner().equalsIgnoreCase(player.getName()))
                return false;
        }

        return super.addLotteryEntry(player, ticketNumber);

    }

}
