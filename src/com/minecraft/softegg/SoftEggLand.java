/*
MMMMMMMMMMMMmy//-:////:.:hMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMMMMh:-+syyyyhdddds-:mMMMMMMMMMMMMMMMMMNNNNNMMMMMMNNNNNNNNNNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMhhhhdM
MMMMMMMm+:/ss:.`   `./hddd+`sMMMMMMMMMMMMMm+:-//:.sMMM+/..........-/yMMMMMMMMMMMMMMMMMMMMMMMhyyyyhMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMo -/:`+
MMMMMNy.:yy.  `-:::.`-ydddds oMMMMMMMMMMMs`/hdddh:-sNN.oddddddddddd-.MMMMMMMMMMMMMMMMMMMMMMM:.oo: dMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM+`ydh.s
MMMMm:-sdy`  -yddddhyddddddd/`dMMMMMMMMMM`/ddy:-/ys`yM.odds:///////-yMMMMMMMMMMMMMMMMMMMMMMM:/dds dMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM+`ydh.s
MMMh.:hdd-   oddddddddddddddh./dhooohdNmd`sdd:  sdd.od.oddo mNNNNNNMMNmyooooooosomdyoooooooo./dds dMMMMMMMMmdyoooohdNNdddddmdyooosdNMMMMmdooooh/`ydh.s
MMd./hddd.   /ddddddddddddddh:-/osso+:-`:+hddsoohddso+.odd+ mMMMMMMmo::+sooooooo .:ossooooo. /dds dMMMMMNo/::/oooo+::. -+/ -:+ooo+-:hNd/.:+ooo/.`ydh.s
MN-:hddddo`  `:shdddddddddds+ydhyyyhddy/sydddhyydddyyy:odd+./+++++/.-ydhyyhdddy+:yhyyyhddds. /dds dMMMMMd ohhyoooshdh/ odh+yhsosyddo`-`+hdhsosyhshdh.s
M+`hdddddds-`   `-/shdddddo+dds-/oo::ydd/.ydd-``ydd./s`oddddddddddy-hdh-/s-oddo:dd/`s:.ydh`-./dds dMMMMMd.://:sss-.ydh.oddh/-+s+`+dd+`sddo.:so--sddh.s
N.+dddddddddyo/.`   .:ohdh/hdy.yMMMN/.hdh.ydd-  ydd.yM.oddh-::::::--ddy`sd/-hds+dd:`dh`+dh./:/dds dMMMMMMmyhd+::::`sdd:odd-.mMMM-.hdy:ddy`oMMMN/.ydh.s
y`yddddddddddddhyo:`   .oy/dds`NMMMMm odd-ydd-  ydd.yM.oddh hddddds`yddy//+hdh-.ydh+:/+hds d:/dds dMMMMMmo-:+oyyyyyhdd:odd.-MMMMs`ydy+dd+ NMMMM+`ydh.s
s.hddddddddddddddddy/   `s/ddh./NMMMy sdy`sdd:  ydd.yM.oddh mMMMMMN+`sdddddy+-:-.yddddhs/-sM:/dds dMMMMM-.ydh+:-::-sdd:odd.-MMMMs`ydy+ddo dMMMM+`ydh.s
s`ydddddddddddddddddd:   /ssddy-:oo+-odh- sdd:  ydd-::`oddh`ooooooo-`hdd+:..ohN-:dds:..ohmMM:/dds hNNNNs odd/`hdmo sdd:odd.-MMMMs`ydy-ddh`:mMNh--hdh.s
y`oddddddyddddddddddd:   +d++hddyssyhho-: sdd:  oddyyhoodddhhhhhhhy:.hddo+///:/`+ddho+//:/oh-/ddy-:////:-+ddo.//:-oddd:odd.-MMMMs`ydy`+ddy--+::ohhdh.s
M/.ddddh:`:ydddddddh+`  `hh-.::+sso+/:/hM`/oo..s./osso-:sssssssssss:+ddhyyyddhy+yddhyyhdhho` :dddhhhhhhhh//ydhyyyyo+dh:odd.-MMMMs`ydy``/yhhhyhhs:+dh.s
Mm./dddh-  `-/++++:`   :yh:-NNhyyyyydNMMMdyyyhNMNhyyyyyyyyyyyyyyy/`sdh/-++/./hdddy--++:.sdd/`:-::::::::::.--:///::/:::.-::-yMMMMh.-:-:ds/-///:-/:-::-h
MMm.:hddds:`        .:sho.+NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMs`sddo://::oddddh/://:/ydd-:NNNNNNNNNNNNNNNNNNNNNMNNNNNNNNMMMMMMNNNNMMMMNNNNNNMMNNNNM
MMMN/.ohdddhyssoooshhh+--hMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM:`+hdddddddy:-ohddddddhs:+mMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMd+::oyyhhhhhys+--sNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMd+-://::-:/shs/:////::omMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
MMMMMMMNmss+/////osmmMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNmmmNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
*/

package com.minecraft.softegg;

/* Imports */
import com.minecraft.softegg.commands.*;
import com.minecraft.softegg.listeners.*;
import com.minecraft.softegg.objects.SoftEggLandArenas;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import me.furt.forumaa.ForumAA;
import me.furt.forumaa.SQLQuery;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SoftEggLand extends JavaPlugin {
    /* Classes */
    
    //Command Classes
    public final SoftEggLandCommandHelp CommandHelp = new SoftEggLandCommandHelp(this);
    public final SoftEggLandCommandHead CommandHead = new SoftEggLandCommandHead(this);
    public final SoftEggLandCommandJobs CommandJobs = new SoftEggLandCommandJobs(this);
    public final SoftEggLandCommandSendItem CommandSendItem = new SoftEggLandCommandSendItem(this);
    public final SoftEggLandCommandMailbox CommandMailbox = new SoftEggLandCommandMailbox(this);
    public final SoftEggLandCommandBobCast CommandBobCast = new SoftEggLandCommandBobCast(this);
    public final SoftEggLandCommandPlayerMessage CommandPlayerMessage = new SoftEggLandCommandPlayerMessage(this);
    public final SoftEggLandCommandTell CommandTell = new SoftEggLandCommandTell(this);
    public final SoftEggLandCommandBan CommandBan = new SoftEggLandCommandBan(this);
    public final SoftEggLandCommandKick CommandKick = new SoftEggLandCommandKick(this);
    public final SoftEggLandCommandStrike CommandStrike = new SoftEggLandCommandStrike(this);
    public final SoftEggLandCommandPardon CommandPardon = new SoftEggLandCommandPardon(this);
    public final SoftEggLandCommandMute CommandMute = new SoftEggLandCommandMute(this);
    public final SoftEggLandCommandPlayerInfo CommandPlayerInfo = new SoftEggLandCommandPlayerInfo(this);
    public final SoftEggLandCommandHoliday CommandHoliday = new SoftEggLandCommandHoliday(this);
    public final SoftEggLandCommandRep CommandRep = new SoftEggLandCommandRep(this);
    public final SoftEggLandCommandRocket CommandRocket = new SoftEggLandCommandRocket(this);
    public final SoftEggLandCommandArena CommandArena = new SoftEggLandCommandArena(this);
    //public final SoftEggLandCommandMMO CommandMMO = new SoftEggLandCommandMMO(this);
    public final SoftEggLandCommandGame CommandGame = new SoftEggLandCommandGame(this);
    public final SoftEggLandCommandActivated CommandActivated = new SoftEggLandCommandActivated(this);
    public final SoftEggLandCommandMarry CommandMarry = new SoftEggLandCommandMarry(this);
    
    //Event Classes
    private SoftEggLandMailboxListener ListenerMailbox;
    private SoftEggLandBlockAddressListener ListenerBlockAddress;
    private SoftEggLandDeathChestListener ListenerDeathChest;
    private SoftEggLandForeverFallingListener ListenerForeverFalling;
    private SoftEggLandAntiBuildListener ListenerAntiBuild;
    private SoftEggLandChatListener ListenerChat;
    private SoftEggLandBanListener ListenerBan;
    private SoftEggLandCombatListener ListenerCombat;
    private SoftEggLandHolidayListener ListenerHoliday;
    private SoftEggLandRepListener ListenerRep;
    private SoftEggLandArenaListener ListenerArena;
    //private SoftEggLandMMOListener ListenerMMO;
    //private SoftEggLandVoteListener ListenerVote;
    private SoftEggLandCustomEventListenerListener ListenerEvents;
    private SoftEggLandGameListener ListenerGame;
    //private SoftEggLandFamilyListener ListenerFamily;
    private SoftEggLandMarriageListener ListenerMarriage;
    
    /* Global Variables */
    
    /* Class Level Variables */
    public YamlConfiguration PluginYML;
    
    public static boolean isPluginLoaded = false;
    
    /* Enable the plugin */
    @Override
    public void onEnable() {
        /* Define UtilLevel Params */
        SoftEggLandUtils.setDataFolder(getDataFolder());
        SoftEggLandUtils.motd = getServer().getMotd();
        
        InputStream is = this.getResource("plugin.yml");
        PluginYML = YamlConfiguration.loadConfiguration(is);
        
        //Get Plugin Manager
        PluginManager pluginManager = getServer().getPluginManager();
        
        /* Load Dependancies */
        //Vault
        if(!setupPermissions()) {
            getLogger().info("Failed to load Vault!");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        
        //ForumAA
        if(!setupForums()) {
            getLogger().info("Failed to load ForumAA!");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        
        //Sets up my directories
        if(!setupDirectories()) {
            getLogger().info("Failed to Enable Plugin!");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        
        SoftEggLandRepUtils.ReloadConfig();
        SoftEggLandArenaUtils.ReloadConfig();
        SoftEggLandArenas.SetupArenas();
        SoftEggLandGameUtils.Initialize();
        
        /* Register Commands */
        getCommand("help").setExecutor(CommandHelp);
        getCommand("head").setExecutor(CommandHead);
        getCommand("jobs").setExecutor(CommandJobs);
        getCommand("senditem").setExecutor(CommandSendItem);
        getCommand("mailbox").setExecutor(CommandMailbox);
        getCommand("bobcast").setExecutor(CommandBobCast);
        getCommand("setmotd").setExecutor(CommandBobCast);
        getCommand("playermessage").setExecutor(CommandPlayerMessage);
        getCommand("tell").setExecutor(CommandTell);
        getCommand("reply").setExecutor(CommandTell);
        getCommand("ban").setExecutor(CommandBan);
        getCommand("kick").setExecutor(CommandKick);
        getCommand("warn").setExecutor(CommandKick);
        getCommand("strike").setExecutor(CommandStrike);
        getCommand("pardon").setExecutor(CommandPardon);
        getCommand("mute").setExecutor(CommandMute);
        getCommand("playerinfo").setExecutor(CommandPlayerInfo);
        getCommand("bunny").setExecutor(CommandHoliday);
        getCommand("getrep").setExecutor(CommandRep);
        getCommand("giverep").setExecutor(CommandRep);
        getCommand("setrep").setExecutor(CommandRep);
        getCommand("sendrep").setExecutor(CommandRep);
        getCommand("buykit").setExecutor(CommandRep);
        getCommand("rocket").setExecutor(CommandRocket);
        getCommand("explode").setExecutor(CommandRocket);
        getCommand("joinarena").setExecutor(CommandArena);
        getCommand("leavearena").setExecutor(CommandArena);
        //getCommand("stats").setExecutor(CommandMMO);
        getCommand("games").setExecutor(CommandGame);
        getCommand("game").setExecutor(CommandGame);
        getCommand("hostgame").setExecutor(CommandGame);
        getCommand("joingame").setExecutor(CommandGame);
        getCommand("leavegame").setExecutor(CommandGame);
        getCommand("closegame").setExecutor(CommandGame);
        getCommand("gamemsg").setExecutor(CommandGame);
        getCommand("countdown").setExecutor(CommandGame);
        getCommand("activated").setExecutor(CommandActivated);
        getCommand("marry").setExecutor(CommandMarry);
        getCommand("formatusers").setExecutor(CommandMarry);
        
        /* CB 1.5.1 Fix: Move Registered Listeners */
        ListenerMailbox = new SoftEggLandMailboxListener(this);
        ListenerBlockAddress = new SoftEggLandBlockAddressListener(this);
        ListenerDeathChest = new SoftEggLandDeathChestListener(this);
        ListenerForeverFalling = new SoftEggLandForeverFallingListener(this);
        ListenerAntiBuild = new SoftEggLandAntiBuildListener(this);
        ListenerChat = new SoftEggLandChatListener(this);
        ListenerBan = new SoftEggLandBanListener(this);
        ListenerCombat = new SoftEggLandCombatListener(this);
        ListenerHoliday = new SoftEggLandHolidayListener(this);
        ListenerRep = new SoftEggLandRepListener(this);
        ListenerArena = new SoftEggLandArenaListener(this);
        //ListenerMMO = new SoftEggLandMMOListener(this);
        //ListenerVote = new SoftEggLandVoteListener(this);
        ListenerEvents = new SoftEggLandCustomEventListenerListener(this);
        ListenerGame = new SoftEggLandGameListener(this);
        //ListenerFamily = new SoftEggLandFamilyListener(this);
        ListenerMarriage = new SoftEggLandMarriageListener(this);
        
        /* Register Listeners */
        pluginManager.registerEvents(ListenerMailbox, this);
        pluginManager.registerEvents(ListenerBlockAddress, this);
        pluginManager.registerEvents(ListenerDeathChest, this);
        pluginManager.registerEvents(ListenerForeverFalling, this);
        pluginManager.registerEvents(ListenerAntiBuild, this);
        pluginManager.registerEvents(ListenerChat, this);
        pluginManager.registerEvents(ListenerBan, this);
        pluginManager.registerEvents(ListenerCombat, this);
        pluginManager.registerEvents(ListenerHoliday, this);
        pluginManager.registerEvents(ListenerRep, this);
        pluginManager.registerEvents(ListenerArena, this);
        //pluginManager.registerEvents(ListenerMMO, this);
        //pluginManager.registerEvents(ListenerVote, this);
        pluginManager.registerEvents(ListenerEvents, this);
        pluginManager.registerEvents(ListenerGame, this);
        //pluginManager.registerEvents(ListenerFamily, this);
        pluginManager.registerEvents(ListenerMarriage, this);
        
        /* Plugin Loaded Fine */
        isPluginLoaded = true;
        SoftEggLandUtils.broadcastWithPerm("SoftEggLand.*", "§dSoftEggLand version " + PluginYML.getString("version") + " is now enabled.");
    }
    
    @Override
    public void onDisable() {
        if(!isPluginLoaded) {
            getLogger().info("Plugin Disabled before loaded.");
            return;
        }
        
        /* Stop Threads */
        ListenerDeathChest.checkChests.cancel();
        ListenerBan.checkBans.cancel();
        ListenerBan.checkVanish.cancel();
        ListenerCombat.checkEnderdragon.cancel();
        ListenerHoliday.checkBunnies.cancel();
        ListenerRep.checkRep.cancel();
        ListenerArena.checkArenas.cancel();
        //ListenerMMO.savePlayers.cancel();
        //ListenerMMO.checkSkills.cancel();
        ListenerGame.checkGames.cancel();
        
        SoftEggLandRepUtils.SavePlayersConfig();
        SoftEggLandArenaUtils.SaveConfig();
        
        /* Close SQL */
        SoftEggLandUtils.sqlClose();
    }
    
    /* Vault Permissions Setup */
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            SoftEggLandUtils.permission = permissionProvider.getProvider();
        }
        return (SoftEggLandUtils.permission != null);
    }

    private boolean setupForums() {
        ForumAA forumProvider = (ForumAA) getServer().getPluginManager().getPlugin("ForumAA");
        if (forumProvider != null) {
            try {
                Field f = forumProvider.getClass().getDeclaredField("sqlDB");
                f.setAccessible(true);
                SoftEggLandUtils.forum = (SQLQuery) f.get(forumProvider);
            } catch (Exception ex) {
            }
        }
        return (SoftEggLandUtils.forum != null);
    }
    
    public boolean setupDirectories() {
        
        /* Make Directory */
        File Dir = new File(getDataFolder() + "");
        if(!Dir.exists()) {
            Dir.mkdir();
        }
        
        /* Mailbox YML */
        File YML = new File(getDataFolder(), "/mailbox.yml");
        if(!YML.exists()) {
                try {
                YML.createNewFile();
            } catch (IOException ex) {
                return false;
            }
        }
        
        /* Deathchest YML */
        File deathChest = new File(getDataFolder() + "/deathchest.yml");
        if(!deathChest.exists()) {
             try {
                deathChest.createNewFile();
            } catch (IOException ex) {
                return false;
            }
        }
        
        /* Deathsign YML */
        File deathSign = new File(getDataFolder() + "/deathsign.yml");
        if(!deathSign.exists()) {
             try {
                deathSign.createNewFile();
            } catch (IOException ex) {
                return false;
            }
        }
        
        /* Generate EnderDragon YML */
        File enderYML = new File(getDataFolder() + "/dragon.yml");
        if(!enderYML.exists()) {
             try {
                enderYML.createNewFile();
            } catch (IOException ex) {
                return false;
            }
        }
        
        /* Generate Arena YML */
        File arenaYML = new File(getDataFolder() + "/arenas.yml");
        if(!arenaYML.exists()) {
            try {
                arenaYML.createNewFile();
            } catch(IOException ex) {
                return false;
            }
        }
        
        /* Generate Marriage YML */
        File marriageYML = new File(getDataFolder() + "/marriage/");
        if(!marriageYML.exists()) {
            marriageYML.mkdir();
        }
        SoftEggLandMarriageUtils.offers = new HashMap<OfflinePlayer, OfflinePlayer>();
        
        /* Generate Arena YML */
        File mmoDir = new File(getDataFolder() + "/SELMMO");
        if(!mmoDir.exists()) {
            mmoDir.mkdir();
        }
        SoftEggLandMMOUtils.SetupDirs();
        
        /* Generate Config File */
        File config = new File(getDataFolder(), "config.yml");
        if(!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        
        /*** Stores SQL Settings ***/
        SoftEggLandUtils.sqlHost = getConfig().getString("SQL.host");
        SoftEggLandUtils.sqlDB = getConfig().getString("SQL.database");
        SoftEggLandUtils.sqlUser = getConfig().getString("SQL.username");
        SoftEggLandUtils.sqlPass = getConfig().getString("SQL.password");
        SoftEggLandUtils.sqlPort = getConfig().getString("SQL.port");
        SoftEggLandUtils.sqlTable = getConfig().getString("SQL.table");
        
        /* Create the Bans Database */
        if(!SoftEggLandUtils.sqlConnect()) {
            getLogger().info("Failed to enable SoftEggLand! SQL Server Unreachable.");
            return false;
        }

        /*** Create the Bans Table ***/
        String statement = ""
                + "CREATE TABLE IF NOT EXISTS " + SoftEggLandUtils.sqlDB + "."+ SoftEggLandUtils.sqlTable + "Bans ("
                    + "id INT NOT NULL AUTO_INCREMENT, "
                    + "player VARCHAR(45) NOT NULL, "
                    + "reason VARCHAR(200) NOT NULL, "
                    + "type VARCHAR(15) NOT NULL, "
                    + "playerby VARCHAR(45) NOT NULL, "
                    + "pos VARCHAR(150) NOT NULL, "
                    + "date DATETIME NOT NULL, "
                    + "unbandate DATETIME NOT NULL, "
                    + "active VARCHAR(15) NOT NULL, "
                    + "PRIMARY KEY (id) "
                + ");";
        SoftEggLandUtils.sqlQuery(statement);
        return true;
    }
}