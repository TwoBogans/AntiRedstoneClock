package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class CheckList {
    private static final CheckList ourInstance = new CheckList();

    public static CheckList getInstance() {
        return ourInstance;
    }

    private CheckList() {
    }

    public void performCMD(CommandSender sender, String... args) {
        try {
            int page = 1;
            if (args.length > 1) {
                page = Integer.parseInt(args[1]);
            }
            Collection<Location> allLocation = RedstoneClockController.getAllLoc();
            int totalPage = (int) Math.ceil(allLocation.size() / 5.0);
            sender.sendMessage(Msg.RED_STONE_CLOCK_LIST_HEADER.toString().replace("$page",
                    "(" + page + "/" + totalPage + ")"));

            int i = 1;
            int minElements = 5 * (page - 1);
            int maxElements = 5 * page;
            for (Location loc : allLocation) {
                if (i > minElements && i <= maxElements) {
                    int maxPulses = Main.getInstance().getConfig().getInt("MaxPulses");
                    int clock = RedstoneClockController.getRedstoneClock(loc).getNumberOfClock();
                    String color = "§2";    //Dark_Green
                    if (clock > maxPulses * 0.75) {
                        color = "§4";       //Dark_Red
                    } else if (clock > maxPulses * 0.5) {
                        color = "§e";       //yellow
                    } else if (clock > maxPulses * 0.250) {
                        color = "§a";       // green
                    }
                    TextComponent textComponent = new TextComponent(color + "RedStoneClock> §fWorld:" + loc.getWorld().getName()
                            + ",X:" + loc.getX()
                            + ",Y:" + loc.getY()
                            + ",Z:" + loc.getZ()
                            + " b:" + clock + "/" + maxPulses);
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp "
                                                + loc.getX()+ " "
                                                + loc.getY()+ " "
                                                + loc.getZ()));
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("Click to teleport you to the redstoneclock").create()));
                    sendFormatedMessageToPlayer(sender, textComponent);
                }
                i++;
            }
            sender.sendMessage(Msg.RED_STONE_CLOCK_LIST_FOOTER.toString());
        } catch (NumberFormatException e) {
            sender.sendMessage(Msg.COMMAND_USE.toString().replace("$command", "checkList <number>"));
        }
    }

    private void sendFormatedMessageToPlayer(CommandSender sender, TextComponent textComponent) {
        try {
            sender.getClass().getDeclaredMethod("spigot", null);
            sender.spigot().sendMessage(textComponent);
        } catch (NoSuchMethodException e) {
            sender.sendMessage(textComponent.getText());
        }
    }
}