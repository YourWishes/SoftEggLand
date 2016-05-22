package com.minecraft.softegg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoftEggLandBansUtils extends SoftEggLandBase {
    
    public static void addBan(OfflinePlayer player, String reason, CommandSender banner, Date unbandate, String type) {
        boolean active = false;
        if(unbandate.getTime() > (new Date()).getTime()) {
            active = true;
        }
        
        addBan(player, reason, banner, unbandate, type, active);
    }
    
    public static void addBan(OfflinePlayer player, String reason, CommandSender banner, Date unbandate, String type, boolean active) {        
        String BannerName = "CONSOLE";
        String location = "0, 0, 0";

        if(!SoftEggLandUtils.isConsole(banner)) {
            BannerName = ((Player) banner).getName();
            location = SoftEggLandUtils.getStringLocation(((Player) banner).getLocation());
        }
        
        reason = reason.replaceAll("'", "");
        reason = reason.replaceAll("/", "");
        
        String statement = ""
                + "INSERT INTO " + SoftEggLandUtils.sqlDB + "."+ SoftEggLandUtils.sqlTable + "Bans ("
                    + "`player`, "
                    + "`reason`, "
                    + "`type`, "
                    + "`playerby`, "
                    + "`pos`, "
                    + "`date`, "
                    + "`unbandate`, "
                    + "`active`"
                + ") VALUES ("
                    + "'" + player.getName() + "', "
                    + "'" + StringEscapeUtils.escapeJava(reason) + "', "
                    + "'" + type + "', "
                    + "'" + BannerName + "', "
                    + "'" + location + "', "
                    + "'" + SoftEggLandUtils.dateToSQL(new Date()) + "', "
                    + "'" + SoftEggLandUtils.dateToSQL(unbandate) + "', "
                    + "'" + active + "'"
                + ");";
        SoftEggLandUtils.sqlQuery(statement);
    }
    
    public static void deleteBan(int id) {
        String statement = ""
                + "UPDATE " + SoftEggLandUtils.sqlDB + "."+ SoftEggLandUtils.sqlTable + "Bans "
                + "SET `active` = 'false' "
                + "WHERE `id`=" + id + ";";
        SoftEggLandUtils.sqlQuery(statement);
    }
    
    public static void BanPlayer(OfflinePlayer player, String reason, CommandSender banner, Date unbandate, boolean useTime) {
        player.setBanned(true);
        
        if(!useTime) {
            SoftEggLandBansUtils.addBan(player, reason, banner, unbandate, "ban", true);
            //createPost(player, reason, banner);
        } else {
            SoftEggLandBansUtils.addBan(player, reason, banner, unbandate, "ban");
        }
        String PlayerName = "CONSOLE";

        if(!SoftEggLandUtils.isConsole(banner)) {
            PlayerName = ((Player) banner).getName();
        }

        String NotifyMessage = ChatImportant + PlayerName + ChatDefault + " banned " + ChatImportant + player.getName() + ChatDefault + " for " + ChatImportant + reason;

        if(useTime) {
            NotifyMessage += ChatDefault + " for " + ChatImportant + SoftEggLandUtils.TimeAway(unbandate);
        }

        NotifyMessage += ChatDefault + ".";

        SoftEggLandUtils.broadcastWithPerm("SoftEggLand.ban.notify", NotifyMessage);
        /* Send Localized Message */
        if(player.isOnline()) {
            if(useTime) {
                reason = SoftEggLandUtils.TimeAway(unbandate) + ChatDefault + " for " + ChatImportant + reason;
            }
            String KickMessage = 
                    ChatDefault + "You have been banned for " + 
                    ChatImportant + reason + 
                    ChatDefault + ". Visit " + 
                    ChatImportant + SoftEggLandUtils.website + 
                    ChatDefault + " to appeal.";
            ((Player) player).kickPlayer(KickMessage);
        }
    }

    public static void KickPlayer(Player target, String reason, CommandSender banner) {
        SoftEggLandBansUtils.addBan(target, reason, banner, new Date(), "kick");
        
        String PlayerName = "CONSOLE";
        
        if(!SoftEggLandUtils.isConsole(banner)) {
            PlayerName = ((Player) banner).getName();
        }
        
        String NotifyMessage = ChatImportant + PlayerName + ChatDefault + " kicked " + ChatImportant + target.getName() + ChatDefault + " for " + ChatImportant + reason + ChatDefault + ".";
        
        SoftEggLandUtils.broadcastWithPerm("SoftEggLand.kick.notify", NotifyMessage);
        
        
        String KickMessage = ChatDefault + "You have been kicked for " + ChatImportant + reason + ChatDefault + ".";
        target.kickPlayer(KickMessage);
    }
    
    public static void WarnPlayer(OfflinePlayer target, String reason, CommandSender banner) {
        SoftEggLandBansUtils.addBan(target, reason, banner, new Date(), "warn");
        
        String PlayerName = "CONSOLE";
        
        if(!SoftEggLandUtils.isConsole(banner)) {
            PlayerName = ((Player) banner).getName();
        }
        
        String NotifyMessage = ChatImportant + PlayerName + ChatDefault + " warned " + ChatImportant + target.getName() + ChatDefault + " for " + ChatImportant + reason + ChatDefault + ".";
        
        SoftEggLandUtils.broadcastWithPerm("SoftEggLand.warn.notify", NotifyMessage);
        
        
        String KickMessage = ChatDefault + "You have been warned for " + ChatImportant + reason + ChatDefault + ".";
        if(target.isOnline()) {
            target.getPlayer().kickPlayer(KickMessage);
        }
    }
    
    public static void MutePlayer(OfflinePlayer player, String reason, CommandSender banner, Date unbandate, boolean useTime) {
        if(!useTime) {
            SoftEggLandBansUtils.addBan(player, reason, banner, unbandate, "mute", true);
        } else {
            SoftEggLandBansUtils.addBan(player, reason, banner, unbandate, "mute");
        }
        String PlayerName = "CONSOLE";

        if(!SoftEggLandUtils.isConsole(banner)) {
            PlayerName = ((Player) banner).getName();
        }
        
        
        String NotifyMessage = ChatImportant + PlayerName + ChatDefault + " muted " + ChatImportant + player.getName() + ChatDefault + " for " + ChatImportant + reason;

        if(useTime) {
            NotifyMessage += ChatDefault + " for " + ChatImportant + SoftEggLandUtils.TimeAway(unbandate);
        }

        NotifyMessage += ChatDefault + ".";

        SoftEggLandUtils.broadcastWithPerm("SoftEggLand.mute.notify", NotifyMessage);
        
        /* Send Localized Message */
        if(player.isOnline()) {
            if(useTime) {
                reason = SoftEggLandUtils.TimeAway(unbandate) + ChatDefault + " for " + ChatImportant + reason;
            }
            String MuteMessage = 
                    ChatDefault + "You have been muted for " + 
                    ChatImportant + reason +
                    ChatDefault + ".";
            ((Player) player).sendMessage(MuteMessage);
        }
    }
    
    public static void StrikePlayer(OfflinePlayer target, String reason, CommandSender banner) {
        SoftEggLandBansUtils.addBan(target, reason, banner, SoftEggLandUtils.nowAndString("1month"), "strike", true);

        String NotifyMessage = ChatImportant + target.getName() + ChatDefault + " recieved a strike for " + ChatImportant + reason + ChatDefault + ".";
        SoftEggLandUtils.broadcastWithPerm("SoftEggLand.strike.notify", NotifyMessage);
        
        if(getActiveBannedTimes(target, "strike") >= 3) {
            String job = SoftEggLandUtils.GetJob(target);
            if(job.equalsIgnoreCase("Inspector")) {
                SoftEggLandUtils.demote(target, "Citizen");
            } else if(job.equalsIgnoreCase("Police")) {
                SoftEggLandUtils.demote(target, "Inspector");
            } else if(job.equalsIgnoreCase("Mod")) {
                SoftEggLandUtils.demote(target, "Police");
            } else {
            }
        }
    }
    
    public static List<Integer> getActiveBans(OfflinePlayer target, String type) {
        SoftEggLandBansUtils.checkBans();
        List<Integer> ids = new ArrayList<Integer>();
        String statement = ""
                + "SELECT id FROM " + SoftEggLandUtils.sqlDB + "."+ SoftEggLandUtils.sqlTable + "Bans "
                + "WHERE player='" + target.getName() + "' AND active='true' AND type='" + type + "'";
        List<Map<String, String>> results = SoftEggLandUtils.sqlFetch(statement);
        if(results != null) {
            for(int i = 0; i < results.size(); i++) {
                ids.add(Integer.parseInt(results.get(i).get("id")));
            }
        }
        return ids;
    }
    
    public static List<Integer> getBans(OfflinePlayer target, String type) {
        List<Integer> ids = new ArrayList<Integer>();
        String statement = ""
                + "SELECT id FROM " + SoftEggLandUtils.sqlDB + "."+ SoftEggLandUtils.sqlTable + "Bans "
                + "WHERE player='" + target.getName() + "' AND type='" + type + "' ORDER BY date DESC";
        List<Map<String, String>> results = SoftEggLandUtils.sqlFetch(statement);
        if(results == null) {
            return ids;
        }
        for(int i = 0; i < results.size(); i++) {
            ids.add(Integer.parseInt(results.get(i).get("id")));
        }
        return ids;
    }
    
    public static Map<String, String> getBanData(int id) {
        String statement = ""
                + "SELECT * FROM " + SoftEggLandUtils.sqlDB + "."+ SoftEggLandUtils.sqlTable + "Bans "
                + "WHERE id='" + id + "' LIMIT 0,1";
        List<Map<String, String>> results = SoftEggLandUtils.sqlFetch(statement);
        if(results == null) {
            return null;
        }
        if(results.size() >= 1) {
            return results.get(0);
        }
        return null;
    }
    
    public static void PardonPlayer(OfflinePlayer target, String type) {
        List<Integer> IDs = getActiveBans(target, type);
        for(int i = 0; i < IDs.size(); i++) {
            deleteBan(IDs.get(i));
        }
    }
    
    public static boolean hasActiveBans(OfflinePlayer player, String type) {
        if(getActiveBans(player, type).size() > 0) {
            return true;
        }
        return false;
    }
    
    public static boolean isPlayerBanned(OfflinePlayer player) {
        if(hasActiveBans(player, "bans")) {
            return true;
        }
        
        if(player.isBanned()) {
            return true;
        }
        
        return false;
    }
    
    public static int getActiveBannedTimes(OfflinePlayer player, String type) {
        return getActiveBans(player, type).size();
    }

    public static Map<String, String> getBanReason(OfflinePlayer target, String type) {
        Map<String, String> r = new HashMap<String, String>();
        String statement = ""
                + "SELECT * FROM " + SoftEggLandUtils.sqlDB + "."+ SoftEggLandUtils.sqlTable + "Bans "
                + "WHERE player='" + target.getName() + "' AND active='true' AND type='" + type + "'";
        List<Map<String, String>> results = SoftEggLandUtils.sqlFetch(statement);
        if(results == null) {
            return r;
        }
        return results.get(0);
    }

    public static String getPastTenseStringName(String type) {
        type = type.toLowerCase();
        if(type.equalsIgnoreCase("ban")) {
            return "banned";
        }
        if(type.equalsIgnoreCase("warn")) {
            return "warned";
        }
        if(type.equalsIgnoreCase("kick")) {
            return "kicked";
        }
        if(type.equalsIgnoreCase("mute")) {
            return "muted";
        }
        if(type.equalsIgnoreCase("strike")) {
            return "striked";
        }
        return type + "ed";
    }
    
    public static void checkBans() {
        String statement = ""
                + "SELECT * FROM " + SoftEggLandUtils.sqlDB + "."+ SoftEggLandUtils.sqlTable + "Bans "
                + "WHERE active='true' ORDER BY date ASC";
        List<Map<String, String>> results = SoftEggLandUtils.sqlFetch(statement);
        if(results == null) {
            return;
        }
        for(int i = 0; i < results.size(); i++) {
            Map<String, String> data = results.get(i);
            if(data.get("date").equalsIgnoreCase(data.get("unbandate"))) {
                continue;
            }
            
            long now = (new Date()).getTime();
            long unb = SoftEggLandUtils.getSQLDate(data.get("unbandate")).getTime();
            long diff = unb - now;
            if(diff > 0) {
                continue;
            }
            deleteBan(Integer.parseInt(data.get("id")));
            if(data.get("type").equalsIgnoreCase("ban")) {
                OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(data.get("player"));
                op.setBanned(false);
                if(isPlayerBanned(op)) {
                    op.setBanned(true);
                }
            }
        }
    }
    /*
    private static void createPost(OfflinePlayer player, String reason, CommandSender banner) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        long nowinsec = (new Date()).getTime() / 1000;
        
        String msg = "The player " + player.getName() + " was banned by the player " + banner.getName() + ".\n"
                + "\nReason: " + reason
                + "\nWhen: " + cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DATE)
                + " - " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
        
        if(banner instanceof Player) {
            Player p = (Player) banner;
            msg += "\nLocation: " + SoftEggLandUtils.getStringLocation(p.getLocation()) + " - " + p.getWorld().getName();
        }
        
        msg += "\n\nPlease post your appeal here.";
        
        msg = StringEscapeUtils.escapeJava(msg);
        
        String un = banner.getName();
        if(un.equalsIgnoreCase("CONSOLE")) {
            un = "BobKyle";
        }
        
        List<Map<String, String>> results = SoftEggLandUtils.sqlFetch("SELECT user_id FROM SoftEggLandWP.phpbb_users WHERE username LIKE '" + un + "' LIMIT 1;");
        if(results == null || results.size() < 1) {
            //return;
        }
        
        String userID = results.get(0).get("user_id");
        String postTitle = player.getName();
        String userIP = "127.0.0.1";
        if(banner instanceof Player) {
            Player p = (Player) banner;
            userIP = p.getAddress().getAddress().getHostAddress();
        }
        
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());

            byte byteData[] = md.digest();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (Exception ex) {
            //return;
        }
        
        String query = "INSERT INTO SoftEggLandWP.phpbb_posts ("
                + "`forum_id`, `poster_id`, `post_time`, `post_approved`, `post_subject`, `post_text`, `post_checksum`, `bbcode_uid`, `post_postcount`"
                + ") VALUES ("
                + "'19', '" + userID + "', '" + userIP + "', '" + nowinsec + "', '1', '" + postTitle + "', '" + msg + "', '" + sb.toString() + "', 'b5pc23wg', '1'"
                + ");";
        int result = SoftEggLandUtils.sqlQueryID(query);
        if(result == -1) {
            //return;
        }
        int post = result;
        
        query = "INSERT INTO SoftEggLandWP.phpbb_topics (" +
            "`forum_id`, `topic_title`, `topic_poster`, `topic_time`, `topic_first_post_id`, `topic_first_poster_name`, `topic_last_post_id`, `topic_last_poster_id`, `topic_last_poster_name`, `topic_last_post_subject`, `topic_last_post_time`, `topic_last_view_time`" +
            ") VALUES (" +
            "'19', '" + player.getName() + "', '" + userID + "', '" + nowinsec + "', '" + post + "', '" + un + "', '" + post + "', '" + userID + "', '" + un + "', '" + player.getName() + "', '" + nowinsec + "', '" + nowinsec + "'" +
            ");";
        result = SoftEggLandUtils.sqlQueryID(query);
        if(result == -1) {
            //return;
        }
        int topic = result;
        
        query = "INSERT INTO SoftEggLandWP.phpbb_topics_posted (" +
            "`user_id`, `topic_id`, `topic_posted`" +
            ") VALUES (" +
            "'" + userID + "', '" + topic + "', '1'" +
            ");";
        if(!SoftEggLandUtils.sqlQuery(query)) {
            //return;
        }
        
        query = "aaaaaaaaaaaaaaaaaaa";
        if(!SoftEggLandUtils.sqlQuery(query)) {
            //return;
        }
    }*/
}
