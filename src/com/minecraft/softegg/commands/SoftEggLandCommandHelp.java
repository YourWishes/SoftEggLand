/*
 * SoftEggHelp - Handles Player /help
 */

package com.minecraft.softegg.commands;

import com.minecraft.softegg.SoftEggLand;
import com.minecraft.softegg.SoftEggLandBase;
import static com.minecraft.softegg.SoftEggLandBase.ChatDefault;
import static com.minecraft.softegg.SoftEggLandBase.ChatImportant;
import com.minecraft.softegg.SoftEggLandUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandCommandHelp extends SoftEggLandBase implements CommandExecutor {
    
    /* References Main Plugin */
    private final SoftEggLand plugin;
    
    /* Basic Constructor */
    public SoftEggLandCommandHelp(SoftEggLand base) {
        plugin = base;
    }
    
    /* Used to get help string for a specific player */
    public String[] getHelp(Player player, int page) {        
        List<String[]> Helps = new ArrayList<String[]>();
        String[] returnValue = new String[] {"", "", "", "", ""};
        
        String job = SoftEggLandUtils.GetJob(player);
        
        Helps.add(new String[] {
            ChatDefault + "",
            ChatDefault + "Welcome to " + ChatImportant + "SoftEggLand " + ChatDefault + player.getName() + ChatDefault + "!",
            ChatDefault + "Be sure to read through all the " + ChatImportant + "/help" + ChatDefault + " pages.",
            ChatDefault + "Also check out the forums " + ChatImportant + "http://minecraft.softegg.com/" + ChatDefault,
            ChatDefault + "for important news, comps and support!"
        });
        Helps.add(new String[] {
            ChatDefault + "",
            //ChatDefault + "Check the map, available online at " + ChatImportant +"http://goo.gl/nGWjC" + ChatDefault + ".",
            ChatDefault + "Before playing, also be sure to read the " + ChatImportant + "/rules",
            ChatDefault + "Also try the vanilla server, " + ChatImportant + "minecraft.softegg.com:11367",
            ChatDefault + "You are now getting help for " + ChatImportant + job,
            ChatDefault + "If you enjoy PvP instead, you can also visit " + ChatImportant + "Anemos" + ChatDefault + ".",
            ChatDefault + "Go there with " + ChatImportant + "/warp anemos" + ChatDefault + " (Citizens+ Only)"
        });
        
        String forums = ChatImportant + "http://minecraft.softegg.com/forums2/";
        String jform = ChatImportant + job + ChatDefault;
        String citizen = ChatImportant + "Citizen" + ChatDefault;
        String inspector = ChatImportant + "Inspector" + ChatDefault;
        String citizens = ChatImportant + "Citizen's" + ChatDefault;
        String kit = ChatImportant + "/kit" + ChatDefault;
        String sethome = ChatImportant + "/sethome" + ChatDefault;
        String home = ChatImportant + "/home" + ChatDefault;
        
        //Per Job Help
        if(job.equalsIgnoreCase("Default")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "As a " + job + " you can " + kit + " " + jform + " to help get you started.",
            ChatDefault + "Each job has their own " + kit + " for a list of jobs type " + ChatImportant + "/jobs",
                ChatDefault + "You are only a " + jform + ", and are confined to " + ChatImportant + "Newbland" + ChatDefault + " world only.",
                ChatDefault + "Once you earn " + citizen +  " you can go to the " + ChatImportant + "SoftEggLand" + ChatDefault + " world.",
                ChatDefault + "To earn " + citizen + ", you need to play for two days, build a home",
                ChatDefault + "and join the forums at " + forums
            });
        }
        Helps.add(new String[] {
            ChatDefault + "",
            ChatDefault + "You can " + sethome + " to set a home, you can return with " + home,
            ChatDefault + "It is possible to lock chests, furnaces and doors with " + ChatImportant + "/lock",
            ChatDefault + "If you have something private to tell someone, just " + ChatImportant + "/msg" + ChatDefault + " them.",
            ChatDefault + "If you think your connection to the server is bad, type " + ChatImportant + "/ping",
            ChatDefault + "to check the speed of the connection to the server.",
            ChatDefault + "And if someone's bothering you, you can " + ChatImportant + "/ignore" + ChatDefault + " them."
        });
        Helps.add(new String[] {
            ChatDefault + "",
            ChatDefault + "Wanna see who's online? Type " + ChatImportant + "/list" + ChatDefault + ".",
            ChatDefault + "If you need to get back to spawn, you can type " + ChatImportant + "/spawn" + ChatDefault + ".",
            ChatDefault + "You can make item " + ChatImportant + "Mailboxes" + ChatDefault + ", visit " + ChatImportant + "http://goo.gl/gV2EC" + ChatDefault + " for info.",
            ChatDefault + "Die, and you create a " + ChatImportant + "DeathChest" + ChatDefault + ". Visit " + ChatImportant + "http://goo.gl/JLMbE" + ChatDefault + " for info.",
            ChatDefault + "For Info on how to make your own player games, read " + ChatImportant + "http://goo.gl/6EZdQ"
        });
        
        if(job.equalsIgnoreCase("Citizen")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now a " + citizen + ". You can now go to the " + ChatImportant + "SoftEggLand" + ChatDefault + "world.",
                ChatDefault + "Get there with " + ChatImportant + "/warp SoftEggLand" + ChatDefault + " and take your " + ChatImportant + "Newbland"  + ChatDefault + " stuff!",
                ChatDefault + "You can now also " + ChatImportant + "/kit citizen" + ChatDefault + " and get better goodies!"
            });
        }
        
        if(!job.equalsIgnoreCase("Default")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "It is now possible for lifts, doors and bridges to be made.",
                ChatDefault + "Visit " + ChatImportant + "http://goo.gl/eoiYU" + ChatDefault + " to learn how.",
                ChatDefault + "In order to go up in the ranks, you need to earn rep points.",
                ChatDefault + "Type " + ChatImportant + "/getrep" + ChatDefault + " to check how much Rep you have."
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You earn rep by playing on the server every day.",
                ChatDefault + "You can also get or send rep to other players with " + ChatImportant + "/sendrep",
                ChatDefault + "Once you have 40 Rep points, you can buy a Tier I job.",
                ChatDefault + "You can buy a job with " + ChatImportant + "/buy [job]" + ChatDefault + ", Type " + ChatImportant + "/buy list" + ChatDefault + " for a list."
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "Other places to check out: " + ChatImportant + "/warp creative" + ChatDefault + " and " + ChatImportant + "/warp sandbox",
                ChatDefault + "Other useful commands that " + citizens + " can do are:",
                ChatImportant + "/delhome, /homes, /tpa, /tpahere, ",
                ChatDefault + citizens + " and higher can set many homes. Type " + ChatImportant + "/sethome [name]" + ChatDefault + ""
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You can submit a help ticket to the mods if you need help ASAP",
                ChatDefault + "Read up about it at " + ChatImportant + "http://goo.gl/hpfxr",
                ChatDefault + "A fun aspect is the Mob Arenas, Read the guide " + ChatImportant + "http://goo.gl/FUBVg",
                ChatDefault + "The EnderDragon in the SoftEggLand end world will respawn every 12 hours AFTER the death of the last one."
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "",
                ChatDefault + "",
                ChatDefault + "",
                ChatDefault + ""
            });
        }
        
        /* Tier 1 Jobs */
        if(job.equalsIgnoreCase("Adventurer")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now an Adventurer!",
                ChatDefault + "Adventurer has 1 kit and 1 command",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit adventurer",
                ChatDefault + "The Command that Adventuerers have is " + ChatImportant + "/helm"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("Constructor")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now a Constructor!",
                ChatDefault + "Constructor has 1 kit and 3 commands",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit constructor",
                ChatDefault + "The 3 commands are: " + ChatImportant + "/fixwater /fixlava /drain"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("Forester")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now a Forester!",
                ChatDefault + "Forester has 1 kit and no commands",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit forester"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("Miner")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now a Miner!",
                ChatDefault + "Miner has 1 kit and 1 command",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit miner",
                ChatDefault + "The command is " + ChatImportant + "/furnace"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("Supplier")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now a Supplier!",
                ChatDefault + "Supplier has 1 kit and 1 command",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit supplier",
                ChatDefault + "The command is " + ChatImportant + "/ride"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        
        /* Tier 2 Jobs */
        if(job.equalsIgnoreCase("AdventurerII")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now an AdventurerII!",
                ChatDefault + "AdventurerII has 1 kit, 1 command and all commands from " + ChatImportant + "Adventurer",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit adventurerii",
                ChatDefault + "The Command that AdventuererII's have is " + ChatImportant + "/enchantingtable"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("ConstructorII")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now a ConstructorII!",
                ChatDefault + "ConstructorII has 1 kit, 2 Craftbook ICs and all commands from " + ChatImportant + "Constructor",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit constructorii",
                ChatDefault + "The two Craftbook IC's that ConstructorII's have are " + ChatImportant + "MC0272" + ChatDefault + " and " + ChatImportant + "MC1272"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("ForesterII")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now a ForesterII!",
                ChatDefault + "ForesterII has 2 kits",
                ChatDefault + "" + ChatImportant + "/kit foresterii" + ChatDefault + " and " + ChatImportant + "/kit eggs"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("MinerII")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now an MinerII!",
                ChatDefault + "MinerII has 1 kit, 1 command and all commands from " + ChatImportant + "Miner",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit minerii",
                ChatDefault + "The Command that MinerII's have is " + ChatImportant + "/ingot2block"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("SupplierII")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now a SupplierII!",
                ChatDefault + "SupplierII has 1 kit and all commands from " + ChatImportant + "Supplier",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit supplierii"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        
        /* Tier 3 Jobs */
        if(job.equalsIgnoreCase("AdventurerIII")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now an AdventurerIII!",
                ChatDefault + "AdventurerIII has 1 kit, 1 command and all commands from " + ChatImportant + "AdventurerII",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit adventureriii",
                ChatDefault + "The Command that AdventuererIII's have is " + ChatImportant + "/backpack"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("ConstructorIII")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now an ConstructorIII!",
                ChatDefault + "ConstructorIII has 1 kit, all IC's and all commands from " + ChatImportant + "ConstructorII",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit constructoriii"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("ForesterIII")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now an ForesterIII!",
                ChatDefault + "ForesterIII has 2 kits",
                ChatDefault + "" + ChatImportant + "/kit foresteriii" + ChatDefault + " and " + ChatImportant + "/kit eggs"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("MinerIII")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now an MinerIII!",
                ChatDefault + "MinerIII has 1 kit, 1 command and all commands from " + ChatImportant + "MinerII",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit mineriii",
                ChatDefault + "The Command that MinerIII's have is " + ChatImportant + "/workbench"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        if(job.equalsIgnoreCase("SupplierIII")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You are now an SupplierIII!",
                ChatDefault + "SupplierIII has 1 kit, 1 command and all commands from " + ChatImportant + "SupplierII",
                ChatDefault + "For the kit, type " + ChatImportant + "/kit supplieriii",
                ChatDefault + "The Command that SupplierIII's have is " + ChatImportant + "/dump"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "To quit your job and return to " + citizen + " at any time, type",
                ChatImportant + "/buy resign",
                ChatDefault + "Your rep points will not be refunded!"
            });
        }
        
        /* Staff Ranks */
        if(job.equalsIgnoreCase("Inspector")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "As an " + inspector + ", it is your duty to protect peoples homes",
                ChatDefault + "using " + ChatImportant + "WorldGuard" + ChatDefault + ", help can be found at " + ChatImportant + "",
                ChatImportant + "http://wiki.sk89q.com/wiki/WorldGuard" + ChatDefault + " To assist with this, you have " + ChatImportant + "/fly /tp"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "It is also your duty to inspect Default's home so they can become",
                ChatDefault + "" + citizens + ", but a few precautions must be taken",
                ChatDefault + "Check they've played for two days by using " + ChatImportant + "/whois",
                ChatDefault + "Also use " + ChatImportant + "/lb tb" + ChatDefault + " to make sure they made the home."
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "If the player has met all these requirements, you can promote",
                ChatDefault + "them with " + ChatImportant + "/manpromote [player] citizen",
                ChatDefault + "Be sure to give kudo's and make sure they check /help!"
            });
        }
        if(job.equalsIgnoreCase("Police")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "For more specific Police help, visit the forums.",
                ChatDefault + "Examples of apropriate actions are:",
                ChatDefault + "Spamming/Swearing" + ChatImportant + " mute or kick.",
                ChatDefault + "Griefing/Stealing/Lavaing" + ChatImportant + " kick or warn"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "Repeated Offence " + ChatImportant + "warn, temp ban or ban",
                ChatDefault + "Large Grief " + ChatImportant + "tempban or ban",
                ChatDefault + "Kicking, warning, muting and banning all require a reason.",
                ChatDefault + "Mute and Ban can be for certain lengths of time"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "Example temporary ban is",
                ChatImportant + "/ban player 1minute reason",
                ChatDefault + "To check a players ban status, use " + ChatImportant + "/bans" + ChatDefault + " works for Kicks, Bans, Mutes, Warns and Strikes too.",
                ChatDefault + "If you're spying on a potential griefer, use " + ChatImportant + "/v" + ChatDefault + " to go into vanish."
            });
        }
        if(job.equalsIgnoreCase("Mod")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "As a " + ChatImportant + "Mod" + ChatDefault + " you can kit all",
                ChatDefault + "Tier I job kits, full" + ChatImportant + " WorldEdit " + ChatDefault + "access in the creative",
                ChatDefault + "world. For more info on WorldEdit visit " + ChatImportant + "wiki.sk89q.com/wiki/WorldEdit",
                ChatDefault + "Also be sure to use " + ChatImportant + "/a" + ChatDefault + " for Mod/Admin chat"
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "Also be sure to check out the modreq commands. dev.bukkit.org/server-mods/modreq/",
                ChatDefault + "And use " + ChatImportant + "/activated" + ChatDefault + " to check if a user is registered on the forums.",
                ChatDefault + "",
                ChatDefault + ""
            });
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "Mods can also use LogBlock Rollback commands, visit",
                ChatImportant + "github.com/LogBlock/LogBlock/wiki" + ChatDefault + " for more info.",
                ChatDefault + "Be sure to check the forums as much as possible and help",
                ChatDefault + "whenever help is needed! Your Admin, §d~Dom"
            });
        }
        if(job.equalsIgnoreCase("Admin")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "No help for Admins."
            });
        }
        
        if(job.equalsIgnoreCase("Creative")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "If you're in the " + ChatImportant + "sandbox" + ChatDefault + " world, go ahead and start building!",
                ChatDefault + "However, if you're in the " + ChatImportant + "creative" + ChatDefault + " world, you'll need an " + inspector + " to claim land for you."
            });
        }
        
        String anemos = ChatImportant + "Anemos" + ChatDefault;
        
        if(job.equalsIgnoreCase("Hunter")) {
            Helps.add(new String[] {
                ChatDefault + "",
                ChatDefault + "You can PvP other players, here in " + anemos + ". Raiding and griefing is allowed.",
                ChatDefault + "Also, you can level up stats and get perks for skills.",
                ChatDefault + "Visit " + ChatImportant + "http://goo.gl/Q1Q7V" + ChatDefault + " for more information."
            });
        }
        
        Helps.add(new String[] {
            ChatDefault + "",
            ChatDefault + "For more info about each Job, read " + ChatImportant + "http://goo.gl/UH3RV",
            ChatDefault + "As always, thanks for playing §d~Dom "
        });
        
        for(int x = 0; x < Helps.size(); x++) {
            Helps.get(x)[0] = ChatImportant + "SoftEggLand" + ChatDefault + " help. Page " + ChatImportant + page + ChatDefault + " of " + ChatImportant + Helps.size();
        }
        
        if(page > Helps.size()) {
            returnValue = new String[] {ChatError + "No page " + page + ". Enter a Number between 1 and " + Helps.size()};
            return returnValue;
        }
        
        return Helps.get(page - 1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("help")) {
            if(args.length > 1) {
                sender.sendMessage(ChatError + "Too many arguments.");
                return false;
            }
            
            int page = 0;
            
            if(args.length == 0) {
                page = 1;
            } else {
                try {
                    page = Integer.parseInt(args[0]);
                    if(page < 1) {
                        sender.sendMessage(ChatError + "Page must be 1 or above.");
                        return false;
                    }
                } catch(NumberFormatException nfe) {
                    sender.sendMessage(ChatError + "Page must be a number.");
                    return false;
                }
            }
            
            if(!(sender instanceof Player)) {
                return false;
            } else {
                Player player = (Player) sender;
                sender.sendMessage(getHelp(player, page));
                return true;
            }
        }
        return false;
    }
}
