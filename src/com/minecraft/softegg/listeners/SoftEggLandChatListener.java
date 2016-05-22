package com.minecraft.softegg.listeners;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBansUtils;
import com.minecraft.softegg.SoftEggLandBase;
import com.minecraft.softegg.SoftEggLandUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitTask;

public class SoftEggLandChatListener extends SoftEggLandBase implements Listener {
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    public BukkitTask checkChests;
    
    /* Basic Constructor */
    public SoftEggLandChatListener(SoftEggLand base) {
        plugin = base;
    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        
        if(!SoftEggLandUtils.CanPlayerTalk(e.getPlayer())) {
            e.getPlayer().sendMessage(ChatDefault + "You can't talk, you're muted for " + ChatImportant + SoftEggLandBansUtils.getBanReason(e.getPlayer(), "mute").get("reason"));
            Bukkit.getLogger().info(e.getPlayer().getName() + " tried to say \"" + e.getMessage() + "\" but is muted.");
            e.setCancelled(true);
        }
        
        if(e.getPlayer().hasPermission("SoftEggLand.color")) {
            e.setMessage(SoftEggLandUtils.FormatString(e.getMessage()));
        }
        /*
        Player p = e.getPlayer();
        if(p.getName().equalsIgnoreCase("DOMIN8TRIX25")) {
            p.setDisplayName("Dom");
            p.setPlayerListName(p.getDisplayName());
        } else if(p.getName().equalsIgnoreCase("Lazy_Architect")) {
            p.setDisplayName("Lazy");
            p.setPlayerListName(p.getDisplayName());
        } else if(p.getName().equalsIgnoreCase("Ronald_McButtSex")) {
            p.setDisplayName("Ronald");
            p.setPlayerListName(p.getDisplayName());
        } else if(p.getName().equalsIgnoreCase("Lady_Penny")) {
            p.setDisplayName("Jenae");
            p.setPlayerListName(p.getDisplayName());
        } else if(p.getName().equalsIgnoreCase("yoyo_power")) {
            p.setDisplayName("Yoyo");
            p.setPlayerListName(p.getDisplayName());
        } else if(p.getName().equalsIgnoreCase("BobKyle10")) {
            p.setDisplayName("BobKyle");
            p.setPlayerListName(p.getDisplayName());
        } else if(p.getName().equalsIgnoreCase("Jim_69")) {
            p.setDisplayName("Jill");
            p.setPlayerListName(p.getDisplayName());
        }*/
        
        if(!SoftEggLandUtils.GetJob(e.getPlayer()).equalsIgnoreCase("Default")) {
            e.setMessage(e.getMessage().replaceAll("<3", "❤"));
        }
        
        
        if(e.getMessage().toLowerCase().contains("iphone using minechat")) {
            mineChat(e.getPlayer());
            e.setCancelled(true);
        }
        
        if(e.getMessage().toLowerCase().contains("ipad using minechat")) {
            mineChat(e.getPlayer());
            e.setCancelled(true);
        }
        
        if(e.getMessage().toLowerCase().contains("ipod touch using minechat")) {
            mineChat(e.getPlayer());
            e.setCancelled(true);
        }
        
        if(e.getMessage().toLowerCase().contains("iphone usin minechat")) {
            mineChat(e.getPlayer());
            e.setCancelled(true);
        }
        
        if(e.getMessage().toLowerCase().contains("ipad usin minechat")) {
            mineChat(e.getPlayer());
            e.setCancelled(true);
        }
        
        if(e.getMessage().toLowerCase().contains("ipod touch usin minechat")) {
            mineChat(e.getPlayer());
            e.setCancelled(true);
        }
        
        if(e.getMessage().toLowerCase().contains("\"mc chat\" for android!")) {
            mineChat(e.getPlayer());
            e.setCancelled(true);
        }
        /*
        e.setMessage(e.getMessage().replaceAll("(?i)BUDDER", "Gold"));
        e.setMessage(e.getMessage().replaceAll("(?i)BUTTER", "Gold"));
        */
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void highChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer().hasPermission("multiverse.access.skylands")) {
            String newmsg = "§2*§6" + e.getFormat();
            e.setFormat(newmsg);
        }
        
        if(e.getMessage().equalsIgnoreCase("")) {
            e.setCancelled(true);
            return;
        }
    }
    
    @EventHandler
    public void onServerPing(ServerListPingEvent e) {
        e.setMotd(SoftEggLandUtils.motd);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage(ChatColor.DARK_RED + "1.7 Server Changes this Friday! Read minecraft.softegg.com/forums2/viewtopic.php?t=5732 for info.");
        if(e.getPlayer().hasPlayedBefore()) {
            return;
        }
        
        List<String> pages = new ArrayList<String>();
        
        pages.add("Hello and Welcome to"
                + "\n§9SoftEggLand§0,"
                + "\n§9" + e.getPlayer().getDisplayName()
                + "\n"
                + "\n§0You have arrived in \"§9Newbland§0\" of"
                + "\n§9SoftEggLand§0 and are currently a \"§9Default§0\""
                + "\nplayer. Please feel"
                + "\nfree to explore"
                + "\n§9Newbland§0. If you get"
                + "\nlost, type \"§9/warp"
                + "\nnewbland§0\" and you...");
        
        pages.add("will return to the"
                + "\nplace where you first"
                + "\nentered."
                + "\n"
                + "\nIf you like what you"
                + "\nsee, and what to stay,"
                + "\nfind an empty place"
                + "\nthat is not protected"
                + "\n(you will see a red"
                + "\nmessage on-screen if"
                + "\nit is already"
                + "\nprotected) and do the"
                + "\nfollowing to become...");
        
        pages.add("a Citizen:"
                + "\n"
                + "\n§91.§0 Register online at:"
                + "\n§9minecraft.softegg.com/forums2/ §0 (Click"
                + "\n\"§oRegister§r\" in the"
                + "\nupper right, using the"
                + "\nexact same username"
                + "\nas your minecraft"
                + "\nusername, then"
                + "\nactivate your account"
                + "\nin-game by typing \"§9/account activate§0\"");
        
        pages.add("It is important to "
                + "\ncheck the forums for"
                + "\ncurrent information,"
                + "\nso that questions and"
                + "\nanswers are not"
                + "\ncontinually repeated"
                + "\non the in-game chat."
                + "\n(Less chat = easier"
                + "\non the server Admins.)"
                + "\n"
                + "\n§92.§0 Build a nice house"
                + "\nThis should be"
                + "\nsomething you would..");
        
        pages.add("be proud to live in, in"
                + "\nreal life. It should"
                + "\nhave walls, windows, a"
                + "\nroof, a floor, doors"
                + "\nand should be"
                + "\nfurnished. Please do"
                + "\nnot make houses out"
                + "\nof dirt. Also the"
                + "\nhouse should not"
                + "\nresemble a box. Make"
                + "\nit look good. show off"
                + "\nyour creativity and..");
        
        pages.add("imagination! The"
                + "\nmaterials can be"
                + "\nanything you are able"
                + "\nto obtain without"
                + "\ngriefing, such as logs"
                + "\nor wood, cobble or"
                + "\nbricks, glass, etc. To"
                + "\nget you started, type"
                + "\n§9/kit default§0 and"
                + "\nyou will recieve"
                + "\nhelpful items in your"
                + "\ninventory."
                + "\n§r §r §r §r(continued..)");
        
        pages.add("§93. §0Play for 2 days."
                + "\nThe staff is able to"
                + "\nsee how long you have"
                + "\nplayer. From the time"
                + "\nthe you enter"
                + "\n§9Newbland§0, the counter"
                + "\nbegins. 48 hours from"
                + "\nthat time, you are"
                + "\neligible to become a"
                + "\n§9Citizen§0, so long as you"
                + "\nhave met the other"
                + "\nrequirements. ...");
        
        pages.add("§94.§0 Once the 2 days"
                + "\nhas passed and your"
                + "\nhouse is built, "
                + "you can ask an "
                + "§9Inspector§0, §9Police§0, or §bMod§0"
                + " to come and inspect"
                + "your house. Please "
                + "be patient and wait "
                + "your turn if the staff "
                + "workers are busy. If "
                + "your house passes "
                + "inspection, you can "
                + "then...");
        
        pages.add("be promoted to §9Citizen§0"
                + "\nby the person doing"
                + "\nthe inspection. If you"
                + "\nwould like to have"
                + "\nyour new house"
                + "\nprotected, they can do"
                + "\nthat as well. However,"
                + "\nit is strongly"
                + "\nrecommended that you"
                + "\ngo to \"§9/warp softeggland§0\""
                + " and play where the other §9Citizens§0...");
        
        pages.add("are playing and building"
                + " in a much larger world. §9Newbland§0 "
                + "is reset on a regular basis because "
                + "it is limited to new players. Moving "
                + "to §9SoftEggLand§0 (or §9SEL§0) "
                + "allows you more space. "
                + "When the §9Newbland§0 map is reset"
                + ", all homes and chests vanish...");
        
        pages.add("and will not be recovered. "
                + "\nNow that you are a §9Citizen§0,"
                + " you can live in your §9Newbland§0 house "
                + "while you explore §9SoftEggLand§0 (teleporting"
                + " back to its spawn point at any time by "
                + "typing \"§9/warp newbland§0\"), or you can "
                + "go to...");
        
        pages.add("§9/warp softeggland\n"
                + "§0or §9/warp creative\n"
                + "§0or §9/warp sandbox\n"
                + "§0or §9/warp skylands\n"
                + "§0or §9/warp anemos\n"
                + "§0(§9Skylands§0 is only available "
                + "to §2donators§0.) There are areas where you "
                + "may play with others who've reached level of "
                + "§9Citizen§0 or beyond..");
        
        pages.add("You may build anything "
                + "you like, so long as it "
                + "is not obscene and doesn't "
                + "involve the overuse of "
                + "materials which invite griefing "
                + "(precious ores) or "
                + "cause server lag "
                + "(massive redstone "
                + "contraptions, large amounts "
                + "of contained mobs.) "
                + "When building in...");
        
        pages.add("a town or village, try to "
                + "make your building blend with the "
                + "theme of that area (avoid such "
                + "things as a skycraper in a fishing "
                + "village, etc.)\n"
                + "\nEvery day you play, you recieve a §9rep§0 point. "
                + "These points are useful, once collected...");
        
        pages.add("to 40 points, to buy jobs "
                + "and recieve other useful kits. "
                + "After you have gotten a job, you have gotten "
                + "a job, you can collect further points up to "
                + "90 points for Teir II jobs in the same category "
                + "and another 150 points for Tier III jobs in that "
                + "category. More");
        
        pages.add("information about the jobs on forums. \n§9Rep§0 "
                + "points can be earned for helping other players; for "
                + "getting involved in large projects; for joining "
                + "teams in competitive builds and for building creatively. "
                + "Though it's not forbidden to buy or sell...");
        
        pages.add("§9Rep§0 points, it is frowned upon. Jobs should be "
                + "earned for being consistently good at playing "
                + "§oMinecraft§r and for good behaviour.\n\n§4§lRules:§r§0\nNo "
                + "Griefing - This includes destroying ...");
        
        pages.add("public or other player's property (blocks, crops, "
                + "livestock) and placing blocks on others' property "
                + "without permission. \n\nNo Stealing/Peeking - "
                + "Don't take from others' chests, or even peek inside,"
                + " even ...");
        
        pages.add("if the chest is not locked.\n\nBe Respectful - "
                + "Do not offend with language (swearing or vulgar),\n"
                + "racial clurs, harassment, threats. Be kind.\n\nNo"
                + " Account Sharing - Do not let others use your "
                + "account...");
        
        pages.add("for playing §oMinecraft§r at "
                + "§9SoftEggLand§0. If they use it and cause "
                + "grief enough to get banned, you will not "
                + "be given a second chance to join the server.\n\n"
                + "No Advertising - Do not talk about other §oMinecraft§r "
                + "servers while playing...");
        
        pages.add("§9SoftEggLand§0, not ask about other servers'.\n\n"
                + "No Spam - This includes rapid chat or commands, repeated "
                + "teleport requests, logging in and out or using Caps to get"
                + " attention.");
        
        pages.add("No Cheating - No use "
                + "of hacks, mods, cheats or exploiting bugs."
                + "\n\nNo modifications of your minecraft.jar or any "
                + "other minecraft file on your system that give"
                + " you an unfair advantage during play.\n\n(continued...)");
        
        pages.add("Do not ask to be Promoted/Hired - "
                + "Repeated requests for OP or a server "
                + "job will get you banned. Repeated "
                + "requests for player "
                + "job/citizen may result in being "
                + "demoted, muted or banned.\n\nDo Not Lag The "
                + "Server - Excessive auto-...");
        
        pages.add("farms, animal/villager breeding, "
                + "redsstone clocks will cause lag. "
                + "Anything causing lag is subject to "
                + "removal without warning.\n\nMost of all, have fun "
                + "and enjoy the game! §b~§dDom");
        
        ItemStack newbBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bMeta = (BookMeta) newbBook.getItemMeta();
        bMeta.setAuthor("§5Dom§r");
        bMeta.setTitle("§7Welcome to §9SoftEggLand§7, §9" + e.getPlayer().getDisplayName());
        bMeta.setPages(pages);
        newbBook.setItemMeta(bMeta);
        
        e.getPlayer().getInventory().addItem(newbBook);
        
        Bukkit.getWorld("Newbland").setSpawnLocation(-2, 221, -3);
    }

    private void mineChat(Player player) {
        SoftEggLandBansUtils.KickPlayer(player, "MineChat is not allowed.", Bukkit.getConsoleSender());
    }
}
