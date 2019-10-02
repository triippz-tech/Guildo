package com.triippztech.guildo.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgsUtil
{
    private final static Pattern MENTION = Pattern.compile("^<@!?(\\d{17,19})>");
    private final static Pattern BROKEN_MENTION = Pattern.compile("^@(\\S.{0,30}\\S)#(\\d{4})");
    private final static Pattern ID = Pattern.compile("^(\\d{17,19})");
    private final static String TIME_REGEX = "(?is)^((\\s*-?\\s*\\d+\\s*(d(ays?)?|h((ou)?rs?)?|m(in(ute)?s?)?|s(ec(ond)?s?)?)\\s*,?\\s*(and)?)*).*";

    public static ResolvedArgs resolve(String args, Guild guild)
    {
        return resolve(args, false, guild);
    }

    public static ResolvedArgs resolve(String args, boolean allowTime, Guild guild)
    {
        Set<Member> members = new LinkedHashSet<>();
        Set<User> users = new LinkedHashSet<>();
        Set<Long> ids = new LinkedHashSet<>();
        Set<String> unresolved = new LinkedHashSet<>();
        Matcher mat;
        User u;
        long i;
        boolean found = true;
        while(!args.isEmpty() && found)
        {
            found = false;
            mat = MENTION.matcher(args);
            if(mat.find())
            {
                i = Long.parseLong(mat.group(1));
                u = guild.getJDA().getUserById(i);
                if(u==null)
                    ids.add(i);
                else if(guild.isMember(u))
                    members.add(guild.getMember(u));
                else
                    users.add(u);
                args = args.substring(mat.group().length()).trim();
                found = true;
                continue;
            }
            mat = BROKEN_MENTION.matcher(args);
            if(mat.find())
            {
                for(User user: guild.getJDA().getUserCache().asList())
                {
                    if(user.getName().equals(mat.group(1)) && user.getDiscriminator().equals(mat.group(2)))
                    {
                        if(guild.isMember(user))
                            members.add(guild.getMember(user));
                        else
                            users.add(user);
                        found = true;
                        break;
                    }
                }
                args = args.substring(mat.group().length()).trim();
                if(found)
                    continue;
                unresolved.add(mat.group());
                found = true;
                continue;
            }
            mat = ID.matcher(args);
            if(mat.find())
            {
                try
                {
                    i = Long.parseLong(mat.group(1));
                }catch(NumberFormatException ex)
                {
                    i = 0;
                }
                u = guild.getJDA().getUserById(i);
                if(u==null)
                    ids.add(i);
                else if(guild.isMember(u))
                    members.add(guild.getMember(u));
                else
                    users.add(u);
                args = args.substring(mat.group().length()).trim();
                found = true;
            }
        }
        int time = 0;
        if(allowTime)
        {
            String timeString = args.replaceAll(TIME_REGEX, "$1").trim();
            if(!timeString.isEmpty())
            {
                args = args.substring(timeString.length()).trim();
                time = OtherUtil.parseTime(timeString);
            }
        }
        return new ResolvedArgs(members, users, ids, unresolved, time, args);
    }

    public static class ResolvedArgs
    {
        public final Set<Member> members;
        public final Set<User> users;
        public final Set<Long> ids;
        public final Set<String> unresolved;
        public final int time;
        public final String reason;

        private ResolvedArgs(Set<Member> members, Set<User> users, Set<Long> ids, Set<String> unresolved, int time, String reason)
        {
            this.members = members;
            this.users = users;
            this.ids = ids;
            this.unresolved = unresolved;
            this.time = time;
            this.reason = reason;
        }

        public boolean isEmpty()
        {
            return members.isEmpty() && users.isEmpty() && ids.isEmpty() && unresolved.isEmpty();
        }
    }
}
