package com.triippztech.guildo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

/**
 * Properties specific to Bot.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final Discord discord = new Discord();

    public static class Discord {
        @NotBlank
        private String token;

        private String authorId;

        @NotBlank
        private String prefix;

        private String alternatePrefix;

        private String shards;

        private String guildId;

        private Integer schedulerPoolSize;

        private String logWebookUrl;

        private String categoryId;

        private String serverInvite;

        private Emojis emojis;


        public String getToken() {
            return token;
        }

        public String getAuthorId() {
            return authorId;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getAltPrefix() {
            return alternatePrefix;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public void setAlternatePrefix(String alternatePrefix) {
            this.alternatePrefix = alternatePrefix;
        }

        public String getAlternatePrefix() {
            return alternatePrefix;
        }

        public String getShards() {
            return shards;
        }

        public String getGuildId() {
            return guildId;
        }

        public Integer getSchedulerPoolSize() {
            return schedulerPoolSize;
        }

        public void setShards(String shards) {
            this.shards = shards;
        }

        public void setGuildId(String guildId) {
            this.guildId = guildId;
        }

        public void setSchedulerPoolSize(Integer schedulerPoolSize) {
            this.schedulerPoolSize = schedulerPoolSize;
        }

        public String getLogWebookUrl() {
            return logWebookUrl;
        }

        public void setLogWebookUrl(String logWebookUrl) {
            this.logWebookUrl = logWebookUrl;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getServerInvite() {
            return serverInvite;
        }

        public void setServerInvite(String serverInvite) {
            this.serverInvite = serverInvite;
        }

        public Emojis getEmojis() { return this.emojis; }

        public void setEmojis(Emojis emojis) {
            this.emojis = emojis;
        }

        public static class Emojis {
            private String success;
            private String warning;
            private String error;

            public String getSuccess() {
                return success;
            }

            public void setSuccess(String success) {
                this.success = success;
            }

            public String getWarning() {
                return warning;
            }

            public void setWarning(String warning) {
                this.warning = warning;
            }

            public String getError() {
                return error;
            }

            public void setError(String error) {
                this.error = error;
            }
        }
    }


    public Discord getDiscord() {
        return discord;
    }
}
