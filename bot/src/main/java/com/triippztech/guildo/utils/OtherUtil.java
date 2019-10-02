package com.triippztech.guildo.utils;

import com.triippztech.guildo.domain.DiscordUser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OtherUtil
{
    private final static Logger LOG = LoggerFactory.getLogger(OtherUtil.class);

    public static int parseTime(String timestr)
    {
        timestr = timestr.replaceAll("(?i)(\\s|,|and)","")
            .replaceAll("(?is)(-?\\d+|[a-z]+)", "$1 ")
            .trim();
        String[] vals = timestr.split("\\s+");
        int timeinseconds = 0;
        try
        {
            for(int j=0; j<vals.length; j+=2)
            {
                int num = Integer.parseInt(vals[j]);
                if(vals[j+1].toLowerCase().startsWith("m"))
                    num*=60;
                else if(vals[j+1].toLowerCase().startsWith("h"))
                    num*=60*60;
                else if(vals[j+1].toLowerCase().startsWith("d"))
                    num*=60*60*24;
                timeinseconds+=num;
            }
        }
        catch(Exception ex)
        {
            return 0;
        }
        return timeinseconds;
    }

    public static DiscordUser randomUser(Set<DiscordUser> discordUsers)
    {
        int size = discordUsers.size();
        if (size == 0 )
            return null;
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for(DiscordUser obj : discordUsers)
        {
            if (i == item)
                return obj;
            i++;
        }
        return null;
    }

    public static List<User> resolveUsersFromId(List<Long> ids, Guild guild) {
        List<User> users = new ArrayList<>();
        for ( Long userId: ids ) {
            Member member = guild.getMemberById(userId);
            users.add(Objects.requireNonNull(member).getUser());
        }
        return users;
    }

    public static List<User> resolveUsers(List<Long> ids, ShardManager shardManager) {
        List<User> users = new ArrayList<>();
        for ( Long userId: ids ) {
            User user = shardManager.getUserById(userId);
            if ( user != null )
                users.add(user);
        }
        return users;
    }

    public static List<User> resolveUsersFromId(List<Long> ids, JDA jda) throws NullPointerException{
        List<User> users = new ArrayList<>();
        for ( Long userId: ids ) {
            User user = jda.getShardManager().getUserById(userId);
            users.add(user);
        }
        return users;
    }

}
