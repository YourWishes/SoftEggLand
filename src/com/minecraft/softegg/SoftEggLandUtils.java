/*
 * SoftEggLandUtils - Has Basic Utilities used Class wide in static space.
 */
package com.minecraft.softegg;

import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import static com.minecraft.softegg.SoftEggLandBase.ChatError;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.furt.forumaa.SQLQuery;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class SoftEggLandUtils extends SoftEggLandBase {
    public static Permission permission = null;
    public static SQLQuery forum = null;
    private static File dataFolder;
    
    public static String sqlHost = "";
    public static String sqlDB = "";
    public static String sqlUser = "";
    public static String sqlPass = "";
    public static String sqlPort = "";
    public static String sqlTable = "";
    
    public static String motd = "Welcome to SoftEggLand!";
    
    public static String website = "http://minecraft.softegg.com/forums2/";
    
    public static Map<CommandSender, CommandSender> PlayerMessageReplies = new HashMap<CommandSender, CommandSender>();
    
    private static Connection dbCon;
    
    public static final HashMap<OfflinePlayer, Scoreboard> scoreboards = new HashMap<OfflinePlayer, Scoreboard>();
    public static final HashMap<Scoreboard, Objective> healthObjectives = new HashMap<Scoreboard, Objective>();
    
    public static EntityType[] hostileTypes = {
        EntityType.BLAZE,
        EntityType.CAVE_SPIDER,
        EntityType.CREEPER,
        EntityType.ENDERMAN,
        EntityType.ENDER_DRAGON,
        EntityType.GHAST,
        EntityType.GIANT,
        EntityType.MAGMA_CUBE,
        EntityType.PIG_ZOMBIE,
        EntityType.SILVERFISH,
        EntityType.SKELETON,
        EntityType.SLIME,
        EntityType.SPIDER,
        EntityType.WITHER,
        EntityType.WITCH,
        EntityType.WITHER_SKULL,
        EntityType.ZOMBIE
    };
    
    /* Will Return the Job a Player Belongs to */
    public static String GetJob(Player player) {
        String job = permission.getPrimaryGroup(player);
        return job;
    }
    
    public static String GetJob(OfflinePlayer player) {
        String job = "";
        if(permission.getPrimaryGroup(player.getName(), "SoftEggLand") != null) {
            job = permission.getPrimaryGroup("SoftEggLand", player.getName());
        } else {
            job = permission.getPrimaryGroup(player.getName(), "SoftEggLand");
        }
        return job;
    }
    
    public static String getStringLocation (Location location) {
        return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }
    
    public static Location getLocationString(String string, String world) {
        String[] values = string.split(", ");
        try {
            int x = Integer.parseInt(values[0]);
            int y = Integer.parseInt(values[1]);
            int z = Integer.parseInt(values[2]);
            return new Location(Bukkit.getServer().getWorld(world), x,y,z);
        } catch (NumberFormatException NFE) {
        }
        return null;
    }
    
    public static Location getLocationString(String string, World world) {
        String[] values = string.split(", ");
        try {
            int x = Integer.parseInt(values[0]);
            int y = Integer.parseInt(values[1]);
            int z = Integer.parseInt(values[2]);
            return new Location(world, x,y,z);
        } catch (NumberFormatException NFE) {
        }
        return null;
    }
    
    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }
    
    public static LWCPlugin getLWC() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("LWC");

        if (plugin == null || !(plugin instanceof LWCPlugin)) {
            return null;
        }

        return (LWCPlugin) plugin;
    }
    
    public static boolean canClaimChest(Chest chest, Player player) {
        Protection protection = getLWC().getLWC().findProtection(chest.getBlock());
        boolean canAccess = getLWC().getLWC().canAccessProtection(player, protection);
        
        if(!canAccess) {
            return false;
        }
        
        if(!getWorldGuard().canBuild(player, chest.getBlock())) {
            return false;
        }
        
        if(SoftEggLandMailBoxUtils.getMailboxPlayer(chest) != null) {
            return false;
        }
        
        return true;
    }
    
    public static boolean doesChestHaveSpace(Chest chest) {
        ItemStack[] Contents = chest.getBlockInventory().getContents();
        int items = 0;
        for(int i = 0; i < Contents.length; i++) {
            ItemStack is = Contents[i];
            if(is != null && is.getType() != Material.AIR) {
                items += 1;
            }
        }
        if(items >= Contents.length - 1) {
            return false;
        }
        return true;
    }
    
    public static boolean doesChestHaveItems(Chest chest) {
        ItemStack[] Contents = chest.getBlockInventory().getContents();
        int items = 0;
        for(int i = 0; i < Contents.length; i++) {
            ItemStack is = Contents[i];
            if(is != null && is.getType() != Material.AIR) {
                items += 1;
            }
        }
        if(items > 0) {
            return true;
        }
        return false;
    }
    
    public static void setDataFolder(File folder) {
        dataFolder = folder;
    }
    
    public static File getDataFolder() {
        return dataFolder;
    }
    
    public static boolean isStringAnIP(String ipAddress) {
        Pattern pattern = Pattern.compile("^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,4})(/\\S*)?$");
        if(pattern.matcher(ipAddress).matches()) {
            if(ipAddress.contains("softegg")) {
                return false;
            }
            return true;
        }
        
        ipAddress = ipAddress.replaceAll("[^\\d.:-]", "");
        String[] parts = ipAddress.split("[\\.\\-]");
        if ( parts.length != 4 ) {
            return false;
        }
        for ( String s : parts ) {            
            try {
                int i = Integer.parseInt(s);
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            } catch (Exception ex) {
                
            }
        }
        
        return true;
    }
    
    public static String FormatString(String msg) {
        String[] andCodes = {"&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f", "&l", "&o", "&n", "&m", "&k", "&r"};
        String[] altCodes = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f", "§l", "§o", "§n", "§m", "§k", "§r"};
        
        for(int x = 0; x < andCodes.length; x++) {
            msg = msg.replaceAll(andCodes[x], altCodes[x]);
        }
        
        return msg;
    }
    
    public static String PlainString(String msg) {
        String[] andCodes = {
            "&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f", "&l", "&o", "&n", "&m", "&k", "&r",
            "§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f", "§l", "§o", "§n", "§m", "§k", "§r"
        };
        
        for(int x = 0; x < andCodes.length; x++) {
            msg = msg.replaceAll(andCodes[x], "");
        }
        
        return msg;
    }
    
    public static long getNow() {
        Date someTime = new Date();
        long currentMS = someTime.getTime();
        return currentMS;
    }
    
    public static boolean CanPlayerTalk(OfflinePlayer player) {
        if(SoftEggLandBansUtils.hasActiveBans(player, "mute")) {
            return false;
        }
        return true;
    }
    
    public static CommandSender GetLastMessager(CommandSender player) {
        CommandSender returnvar = PlayerMessageReplies.get(player);
        return returnvar;
    }
    
    public static void SendPrivateMessage(CommandSender from, CommandSender to, String message) {
        if(from.hasPermission("SoftEggLand.color")) {
            message = FormatString(message);
        } else {
            message = SoftEggLandUtils.removeColors(message);
        }
        
        String fromName = ChatImportant + from.getName() + ChatDefault;
        String toName = ChatImportant + to.getName() + ChatDefault;
        
        String messageTo = ChatDefault + "[" + fromName + ChatDefault + " -> " + toName + ChatDefault + "] §r§f" + ChatColor.RESET + ChatColor.WHITE + message;
                
        String messageFrom = ChatDefault + "[" + fromName + ChatDefault + " -> " + toName + ChatDefault + "] §r§f" + ChatColor.RESET + ChatColor.WHITE + message;
        
        if(from.hasPermission("SoftEggLand.bespy") || to.hasPermission("SofteggLand.bespy")) {
            if(!to.getName().equalsIgnoreCase("DOMIN8TRIX25") && !from.getName().equalsIgnoreCase("DOMIN8TRIX25")) {
                if(Bukkit.getPlayer("DOMIN8TRIX25") != null) {
                    Bukkit.getPlayer("DOMIN8TRIX25").sendMessage(messageTo);
                }
            }
        }
        
        PlayerMessageReplies.put(from, to);
        PlayerMessageReplies.put(to, from);
        
        from.sendMessage(messageFrom);
        to.sendMessage(messageTo);
    }
    
    public static void demote(OfflinePlayer target, String job) {
        String name = target.getName();
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "manuadd " + name + " " + job + " SoftEggLand");
        if(job.equalsIgnoreCase("Citizen")) {
            job = "Player";
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "manuadd " + name + " " + job + " Anemos");
        if(job.equalsIgnoreCase("Player")) {
            job = "Creative";
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "manuadd " + name + " " + job + " Creative");
    }
    
    public static boolean isValidTime(String input) {
        String[] names = new String[]{
            ("year"),
            ("years"),
            ("month"),
            ("months"),
            ("day"),
            ("days"),
            ("hour"),
            ("hours"),
            ("minute"),
            ("minutes"),
            ("second"),
            ("seconds")
        };
        
        Pattern timePattern = Pattern.compile(
        "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
        
        Matcher m = timePattern.matcher(input);
        
        while(m.find()) {
            	if (m.group() == null || m.group().isEmpty()) {
                    continue;
                }
                for (int i = 0; i < m.groupCount(); i++) {
                    if (m.group(i) != null && !m.group(i).isEmpty()) {
                        return true;
                    }
                }
        }
        
        return false;
    }
    
    public static Date nowAndString(String input) {
        boolean found = false;
        
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        Date now = new Date();
        String[] names = new String[]{
            ("year"),
            ("years"),
            ("month"),
            ("months"),
            ("day"),
            ("days"),
            ("hour"),
            ("hours"),
            ("minute"),
            ("minutes"),
            ("second"),
            ("seconds")
        };
        
        Pattern timePattern = Pattern.compile(
        "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
        
        Matcher m = timePattern.matcher(input);
        
        while(m.find()) {
            	if (m.group() == null || m.group().isEmpty()) {
                    continue;
                }
                for (int i = 0; i < m.groupCount(); i++) {
                    if (m.group(i) != null && !m.group(i).isEmpty()) {
                        found = true;
                    }
                    if(found) {
                        if (m.group(1) != null && !m.group(1).isEmpty()) {
                            years = Integer.parseInt(m.group(1));
                        }
                        if (m.group(2) != null && !m.group(2).isEmpty()) {
                            months = Integer.parseInt(m.group(2));
                        }
                        if (m.group(3) != null && !m.group(3).isEmpty()) {
                            weeks = Integer.parseInt(m.group(3));
                        }
                        if (m.group(4) != null && !m.group(4).isEmpty()) {
                            days = Integer.parseInt(m.group(4));
                        }
                        if (m.group(5) != null && !m.group(5).isEmpty()) {
                            hours = Integer.parseInt(m.group(5));
                        }
                        if (m.group(6) != null && !m.group(6).isEmpty()) {
                            minutes = Integer.parseInt(m.group(6));
                        }
                        if (m.group(7) != null && !m.group(7).isEmpty()) {
                            seconds = Integer.parseInt(m.group(7));
                        }
                        break;
                    }
                }
        }
        
        Calendar c = Calendar.getInstance();
        if (years > 0) {
            c.add(Calendar.YEAR, years);
        }
        if (months > 0)  {
            c.add(Calendar.MONTH, months);
        }
        if (weeks > 0) {
            c.add(Calendar.WEEK_OF_YEAR, weeks);
        }
        if (days > 0) {
            c.add(Calendar.DAY_OF_MONTH, days);
        }
        if (hours > 0) {
            c.add(Calendar.HOUR_OF_DAY, hours);
        }
        if (minutes > 0) {
            c.add(Calendar.MINUTE, minutes);
        }
        if (seconds > 0) {
            c.add(Calendar.SECOND, seconds);
        }
        now = c.getTime();
        return now;
    }
    
    public static boolean sqlConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://"+sqlHost+":"+sqlPort+"/" + sqlDB;
            Bukkit.getLogger().info("Opening SQL connection to " + url);
            dbCon = DriverManager.getConnection(url,sqlUser,sqlPass);
            return true;
        } catch (Exception ex) {
            Bukkit.getLogger().info("SQL Connection failed! " + ex.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean sqlQuery(String query) {
        try {
            PreparedStatement sqlStmt = dbCon.prepareStatement(query);
            boolean result = sqlStmt.execute(query);
            return result;
        } catch (SQLException ex) {
            Bukkit.getPlayer("DOMIN8TRIX25").sendMessage(ChatError + "[SQL] " + ex.getMessage());
            return false;
        }
    }
    
    public static int sqlQueryID(String query) {
        try {
            PreparedStatement sqlStmt = dbCon.prepareStatement(query);
            int result = sqlStmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            return result;
        } catch (SQLException ex) {
            Bukkit.getPlayer("DOMIN8TRIX25").sendMessage(ChatError + "[SQL] " + ex.getMessage());
            return -1;
        }
    }
    
    public static List<Map<String, String>> sqlFetch(String query) {
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();
        try {
            Statement myStmt = dbCon.createStatement();
            ResultSet result = myStmt.executeQuery(query);
            while (result.next()){
                Map<String, String> data = new HashMap<String, String>();
                for(int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    data.put(result.getMetaData().getColumnName(i), result.getString(result.getMetaData().getColumnName(i)));
                }
                results.add(data);
            }
        }
        catch (Exception sqlEx) {
            Bukkit.getPlayer("DOMIN8TRIX25").sendMessage(ChatError + "[SQL] " + sqlEx.getMessage());
        }
        
        if(results.size() < 1) {
            return null;
        }
        
        return results;
    }
    
    public static void sqlClose() {
        try {
            dbCon.close();
            Bukkit.getLogger().info("Closing SQL connection...");
        } catch (Exception ex) {
            Bukkit.getLogger().info("SQL Connection failed! " + ex.getLocalizedMessage());
        }
    }

    public static void broadcastWithPerm(String permission, String message) {
        Player[] OnlinePlayers = Bukkit.getOnlinePlayers();
        for(int i = 0; i < OnlinePlayers.length; i++) {
            Player p = OnlinePlayers[i];
            if(p.hasPermission(permission)) {
                p.sendMessage(message);
            }
        }
        Bukkit.getConsoleSender().sendMessage(message);
    }

    public static boolean isConsole(CommandSender sender) {
        if(sender instanceof Player) {
            return false;
        }
        return true;
    }

    public static String TimeAway(Date unbanDate) {
        Long NowInMilli = (new Date()).getTime();
        Long TargetInMilli = unbanDate.getTime();
        Long diffInSeconds = (TargetInMilli - NowInMilli) / 1000+1;

        long diff[] = new long[] {0,0,0,0,0};
        /* sec */diff[4] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
        /* min */diff[3] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        /* hours */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        /* days */diff[1] = (diffInSeconds = (diffInSeconds / 24)) >= 31 ? diffInSeconds % 31: diffInSeconds;
        /* months */diff[0] = (diffInSeconds = (diffInSeconds / 31));
        
        String message = "";
        
        if(diff[0] > 0) {
            message += diff[0] + " month";
            if(diff[0] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[1] > 0) {
            message += diff[1] + " day";
            if(diff[1] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[2] > 0) {
            message += diff[2] + " hour";
            if(diff[2] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[3] > 0) {
            message += diff[3] + " minute";
            if(diff[3] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[4] > 0) {
            message += diff[4] + " second";
            if(diff[4] > 1) {
                message += "s";
            }
            return message;
        }
        
        return "Invalid Time Diff!";
    }
    
    public static String TimeAgo(Date date) {
        Long NowInMilli = (new Date()).getTime();
        Long TargetInMilli = date.getTime();
        Long diffInSeconds = (NowInMilli - TargetInMilli) / 1000+1;

        long diff[] = new long[] {0,0,0,0,0};
        /* sec */diff[4] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
        /* min */diff[3] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        /* hours */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        /* days */diff[1] = (diffInSeconds = (diffInSeconds / 24)) >= 31 ? diffInSeconds % 31: diffInSeconds;
        /* months */diff[0] = (diffInSeconds = (diffInSeconds / 31));
        
        String message = "";
        
        if(diff[0] > 0) {
            message += diff[0] + " month";
            if(diff[0] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[1] > 0) {
            message += diff[1] + " day";
            if(diff[1] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[2] > 0) {
            message += diff[2] + " hour";
            if(diff[2] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[3] > 0) {
            message += diff[3] + " minute";
            if(diff[3] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[4] > 0) {
            message += diff[4] + " second";
            if(diff[4] > 1) {
                message += "s";
            }
            return message;
        }
        
        return "Invalid Time Diff!";
    }
    
    public static String TimeDiff(Date early, Date late) {
        Long NowInMilli = late.getTime();
        Long TargetInMilli = early.getTime();
        Long diffInSeconds = (NowInMilli - TargetInMilli) / 1000;

        long diff[] = new long[] {0,0,0,0,0};
        /* sec */diff[4] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
        /* min */diff[3] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        /* hours */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        /* days */diff[1] = (diffInSeconds = (diffInSeconds / 24)) >= 31 ? diffInSeconds % 31: diffInSeconds;
        /* months */diff[0] = (diffInSeconds = (diffInSeconds / 31));
        
        String message = "";
        
        if(diff[0] > 0) {
            message += diff[0] + " month";
            if(diff[0] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[1] > 0) {
            message += diff[1] + " day";
            if(diff[1] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[2] > 0) {
            message += diff[2] + " hour";
            if(diff[2] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[3] > 0) {
            message += diff[3] + " minute";
            if(diff[3] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[4] > 0) {
            message += diff[4] + " second";
            if(diff[4] > 1) {
                message += "s";
            }
            return message;
        }
        
        return "Invalid Time Diff!";
    }
    
    public static String dateToSQL(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String now = ft.format(date);
        return now;
    }
    
    public static Date getSQLDate(String sqlDate) {
        SimpleDateFormat fat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date returnDate = new Date();
        try {
            returnDate = fat.parse(sqlDate);
        } catch (ParseException ex) {
            return new Date();
        }
        return returnDate;
    }
    
    public static boolean playerInProximity (Player player1, Player player2) {
        List<Entity> ne = player1.getNearbyEntities(40, 40, 40);
        for(Entity e : ne) {
            if(e.getEntityId() == player2.getEntityId()) {
                return true;
            }
        }
        return false;
    }
    
    public static List<Entity> getClosestMob(Player player) {
        return player.getNearbyEntities(1, 1, 1);
    }
    
    public static Entity getClosestAttacker(Player player) {
        for(Entity ent : getClosestMob(player)) {
            if(ent.getType() == null) {
                continue;
            }
            
            if(!SoftEggLandUtils.isTypeHostile(ent.getType())) {
                continue;
            }
            return ent;
        }
        return null;
    }

    public static boolean canSee(Player player, Player player0) {
        if(player.canSee(player0)) {
            return true;
        }
        
        if(player.hasPermission("SoftEggLand.invisible")) {
            return true;
        }
        return false;
    }

    public static boolean isInt(String string) {
        try {
            int number = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
    public static String[] ObjToString (Object[] ObjArr) {
        String[] StrArr = new String[ObjArr.length];
        for(int i = 0; i < ObjArr.length; i++) {
            StrArr[i] = ObjArr[i].toString();
        }
        return StrArr;
    }

    public static String[] ListToArray(List<String> SurvivalList) {
        Object[] ObjArr = SurvivalList.toArray();
        String[] StrArr = ObjToString(ObjArr);
        return StrArr;
    }
    
    public static double getHigher(double loc1, double loc2) {
        if(loc1 >= loc2) {
            return loc1;
        } else if(loc2 >= loc1) {
            return loc2;
        } else {
            return loc1;
        }
    }
    
    public static double getLower(double loc1, double loc2) {
        if(loc1 <= loc2) {
            return loc1;
        } else if(loc2 <= loc1) {
            return loc2;
        } else {
            return loc1;
        }
    }
    
    public static List<Block> getBlocksWithinLocations(Location location1, Location location2) {
        List<Block> blocks = new ArrayList<Block>();
        
        double lowerX = getLower(location1.getBlockX(), location2.getBlockX());
        double higherX = getHigher(location1.getBlockX(), location2.getBlockX());
        
        double lowerY = getLower(location1.getBlockY(), location2.getBlockY());
        double higherY = getHigher(location1.getBlockY(), location2.getBlockY());
        
        double lowerZ = getLower(location1.getBlockZ(), location2.getBlockZ());
        double higherZ = getHigher(location1.getBlockZ(), location2.getBlockZ());
        
        for(int x = (int) lowerX; x <= higherX; x++) {
            for(int y = (int) lowerY; y <= higherY; y++) {
                for(int z = (int) lowerZ; z <= higherZ; z++) {
                    blocks.add(location1.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        
        return blocks;
    }
    
    public static boolean isLocationInArea(Location area1, Location area2, Location location) {
        if(location.getWorld() != area1.getWorld()) {
            return false;
        }
        
        List<Block> blocks = getBlocksWithinLocations(area1, area2);
        for(Block b : blocks) {
            String loc = getStringLocation(location.getBlock().getLocation());
            String loc2 = getStringLocation(b.getLocation());
            if(loc.equalsIgnoreCase(loc2)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isTypeHostile(EntityType type) {        
        for(EntityType type2 : hostileTypes) {
            if(type == type2) {
                return true;
            }
        }
        return false;
    }    
    
    public static int getRandomNumberBetween(int min, int max) {
        Random foo = new Random();
        int randomNumber = foo.nextInt(max - min) + min;
        return randomNumber;
    }

    public static String arrayToString(String[] args) {
        String s = "";
        for(int i = 0; i < args.length; i++) {
            s += args[i];
            if(i < args.length - 1) {
                s += " ";
            }
        }
        return s;
    }
    
    public static String removeColors(String string) {
        for(String str : SoftEggLandBase.altCodes) {
            string = string.replaceAll(str, "");
        }
        for(String str : SoftEggLandBase.colorCodes) {
            string = string.replaceAll(str, "");
        }
        return string;
    }
    
    public static boolean isTypeMinecart(EntityType type) {
        EntityType[] minecarts = {
            EntityType.MINECART,
            EntityType.MINECART_CHEST,
            EntityType.MINECART_FURNACE,
            EntityType.MINECART_HOPPER,
            EntityType.MINECART_MOB_SPAWNER,
            EntityType.MINECART_TNT
        };
        
        for(EntityType t : minecarts) {
            if(type == t) {
                return true;
            }
        }
        
        return false;
    }

    public static boolean isPickaxe(Material m) {
        Material[] pickaxes = {
            Material.DIAMOND_PICKAXE,
            Material.GOLD_PICKAXE,
            Material.IRON_PICKAXE,
            Material.STONE_PICKAXE,
            Material.WOOD_PICKAXE
        };
        
        for(Material a : pickaxes) {
            if(m == a) {
                return true;
            }
        }
        
        return false;
    }

    public static boolean isAxe(Material m) {
        Material[] axes = {
            Material.DIAMOND_AXE,
            Material.GOLD_AXE,
            Material.IRON_AXE,
            Material.STONE_AXE,
            Material.WOOD_AXE
        };
        
        for(Material a : axes) {
            if(a == m) {
                return true;
            }
        }
        
        return false;
    }

    public static boolean isSword(Material m) {
        Material[] swords = {
            Material.DIAMOND_SWORD,
            Material.GOLD_SWORD,
            Material.IRON_SWORD,
            Material.STONE_SWORD,
            Material.WOOD_SWORD
        };
        
        for(Material a : swords) {
            if(a == m) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPlayerWearingArmour(Player player) {
        Material[] armours = {
            Material.DIAMOND_BOOTS,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_LEGGINGS,
            Material.GOLD_BOOTS,
            Material.GOLD_CHESTPLATE,
            Material.GOLD_HELMET,
            Material.GOLD_LEGGINGS,
            Material.IRON_BOOTS,
            Material.IRON_CHESTPLATE,
            Material.IRON_HELMET,
            Material.IRON_LEGGINGS,
            Material.CHAINMAIL_BOOTS,
            Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_HELMET,
            Material.CHAINMAIL_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_HELMET,
            Material.LEATHER_LEGGINGS
        };
        
        for(Material a : armours) {
            for(ItemStack is : player.getInventory().getArmorContents()) {
                if(is.getType() == a) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public static boolean isEndWorld(World world) {
        for(World w : SoftEggLandBase.EndWorlds) {
            if(world != w) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public static boolean isPlayerRegistered(OfflinePlayer player) {
        try {
            return SoftEggLandUtils.forum.checkExists(player.getName());
        } catch (SQLException ex) {
        } catch (ClassNotFoundException ex) {
        }
        return false;
    }
    
    public static boolean isPlayerActivated(OfflinePlayer player) {
        try {
            return forum.checkActivated(player.getName());
        } catch (ClassNotFoundException c) {
        } catch (SQLException e) {
        }
        return false;
    }

    public static boolean isVisible(Player t) {
        Player[] playerList = Bukkit.getServer().getOnlinePlayers();
        for(int i = 0; i < playerList.length; i++) {
            Player p = playerList[i];
            
            if(p == t) {
                continue;
            }
            
            if(!canSee(p, t)) {
                return false;
            }
        }
        return true;
    }

    public static boolean playerHasItems(Player player) {
        for(ItemStack is : player.getInventory().getContents()) {
            if(is != null && is.getType() != Material.AIR) {
                return true;
            }
        }
        
        for(ItemStack is : player.getInventory().getArmorContents()) {
            if(is != null && is.getType() != Material.AIR) {
                return true;
            }
        }
        
        return false;
    }
    /*
    public static Scoreboard RegisterScoreboardPlayer(OfflinePlayer player) {
        if(!scoreboards.containsKey(player)) {
            Scoreboard sc = Bukkit.getScoreboardManager().getNewScoreboard();
            scoreboards.put(player, sc);
            Objective objective = sc.registerNewObjective("showhealth", "health");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName("§6/ §r20 §d❤");
            healthObjectives.put(sc, objective);
        }
        return scoreboards.get(player);
    }
    
    public static void checkScoreboards() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            RegisterScoreboardPlayer(p);
        }
        
        Set<OfflinePlayer> scores = new HashSet<OfflinePlayer>(scoreboards.keySet());
        for(OfflinePlayer p : scores) {
            if(p.isOnline()) {
                checkScoreboard(p);
                continue;
            }
            Scoreboard s = scoreboards.get(p);
            scoreboards.remove(p);
            healthObjectives.remove(s);
        }
    }
    
    public static void checkScoreboard(OfflinePlayer p) {
        Scoreboard s = RegisterScoreboardPlayer(p);
        Objective o = healthObjectives.get(s);
        
        o.setDisplayName("§6/ §r20 §d❤");
        
        p.getPlayer().setScoreboard(s);
            
        for(Player pl : Bukkit.getOnlinePlayers()) {
            pl.setHealth(pl.getHealth());
        }
    }*/
}
