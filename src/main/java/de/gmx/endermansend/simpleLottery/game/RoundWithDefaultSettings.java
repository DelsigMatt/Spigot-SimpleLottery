package de.gmx.endermansend.simpleLottery.game;

import de.gmx.endermansend.simpleLottery.chat.ChatHandler;
import de.gmx.endermansend.simpleLottery.helper.InventoryHandler;
import de.gmx.endermansend.simpleLottery.main.SimpleLottery;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Represents a round in the lottery where multiple players can bet on the same number as well as have multiple tickets.
 */
public class RoundWithDefaultSettings extends RoundInterface {

    public RoundWithDefaultSettings(SimpleLottery main, int roundNumber) {
        super(main, roundNumber);
        tickets = new ArrayList<Ticket>();
    }

    /**
     * Adds a new ticket to the round if player or ticketNumber are different from the already existing tickets.
     *
     * @param player       Initiator of the lottery entry
     * @param ticketNumber Lottery number the player chose
     * @param bet          Bet the player has made
     * @param chat         Used to inform the player if the purchase was successful
     */
    public void addLotteryEntry(Player player, int ticketNumber, ItemStack bet, ChatHandler chat) {

        Ticket ticket = new Ticket(player.getUniqueId(), ticketNumber, bet);

        if (tickets.contains(ticket)) {
            chat.send.ticketError(player);
            return;
        }

        if (!InventoryHandler.giveTicketToPlayer(player, ticket, roundNumber)) {
            chat.send.ticketError(player);
            return;
        }
        tickets.add(ticket);
        chat.send.ticketBought(player, ticketNumber);

    }

    public Collection<Ticket> getTickets() {
        return tickets;
    }

    /**
     * @param player Object who's tickets are asked for
     * @return All ticketNumbers of a player
     */
    public Collection<Integer> getTicketsOf(UUID player) {

        List<Integer> playerTickets = new ArrayList<Integer>();

        UUID owner;
        for (Ticket t : tickets) {
            owner = t.getOwner();
            if (owner.equals(player))
                playerTickets.add(t.getTicketNumber());
        }

        return playerTickets;

    }

    /**
     * Finds all players who have the specified ticket number.
     *
     * @param ticketNumber Number that will be searched for
     * @return All players who have a ticket with the specified ticketNumber in the current round
     */
    public Collection<String> getOwnersOf(int ticketNumber) {

        List<String> owners = new ArrayList<String>();

        for (Ticket t : tickets) {
            if (ticketNumber == t.getTicketNumber())
                owners.add(t.getOwnerName());
        }

        return owners;

    }

    /**
     * @param winningNumber Representing the number that won the round
     * @return Returns a collection of the tickets that won
     */
    public Collection<Ticket> getWinningTickets(int winningNumber) {

        List<Ticket> winningTickets = new ArrayList<Ticket>();

        for (Ticket t : tickets) {
            if (winningNumber == t.getTicketNumber())
                winningTickets.add(t);
        }
        return winningTickets;

    }

    /**
     * Gives the owners of tickets with the winning number their reward via InventoryHandler and returns a collection
     * of Strings of those players.
     *
     * @param winningNumber
     * @return
     */
    public Collection<String> handOutRewards(int winningNumber) {

        Collection<Ticket> winningTickets = getWinningTickets(winningNumber);
        Collection<String> winners = new ArrayList<String>();

        for (Ticket t : winningTickets) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(t.getOwner());
            if (player.isOnline()) {
                winners.add(player.getName());
                if (!(InventoryHandler.giveRewardToPlayer((Player) player, t.getReward(multiplier))))
                    chat.send.rewardError((Player) player);
            }
        }

        return winners;

    }

}
