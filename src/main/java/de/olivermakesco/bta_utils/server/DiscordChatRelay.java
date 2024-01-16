package de.olivermakesco.bta_utils.server;

import club.minnced.discord.webhook.external.JDAWebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.minecraft.server.MinecraftServer;
import de.olivermakesco.bta_utils.BtaUtilsMod;
import net.minecraft.core.net.command.TextFormatting;
import java.time.Instant;

public class DiscordChatRelay {

    private static final JDAWebhookClient webhookClient = DiscordClient.getWebhook();

    public static void sendToMinecraft(String author, String message) {
        MinecraftServer server = MinecraftServer.getInstance();
        message = "[" + TextFormatting.PURPLE + "DISCORD" + TextFormatting.RESET + "] <" + author + "> " + message;
        BtaUtilsMod.info(message);
        String[] lines = message.split("\n");
        for (String chatMessage : lines) {
            server.playerList.sendEncryptedChatToAllPlayers(chatMessage);
        }
    }

    public static void sendToDiscord(String author, String message) {
        if (webhookClient == null) return;
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(author);
        builder.setAvatarUrl("https://www.mc-heads.net/head/" + author);
        builder.setContent(message);
        webhookClient.send(builder.build());
    }

    public static void sendJoinLeaveMessage(String username, boolean joined) {
        if (webhookClient == null) return;
        String avatarUrl = "https://www.mc-heads.net/head/" + username;
        String joinLeaveText = username + (joined ? " joined" : " left") + " the server";
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(joined ? 0x00FF00 : 0xFF0000)
                .setAuthor(new WebhookEmbed.EmbedAuthor(joinLeaveText, avatarUrl, null))
                .build();
        sendMessage(null, embed);
    }

    public static void sendDeathMessage(String deathMessage) {
        if (webhookClient == null) return;
        String avatarUrl = "https://i.imgur.com/l1EJ6fx.png";
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0xFF0000)
                .setAuthor(new WebhookEmbed.EmbedAuthor(deathMessage, avatarUrl, null))
                .build();
        sendMessage(null, embed);
    }

    public static void sendServerStartMessage() {
        if (webhookClient == null) return;
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0x4ae485)
                .setAuthor(new WebhookEmbed.EmbedAuthor("✅ Server started!", null, null))
                .setTimestamp(Instant.now())
                .build();
        sendMessage(null, embed);
    }

    public static void sendServerStoppedMessage() {
        if (webhookClient == null) return;
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0xf92f60)
                .setAuthor(new WebhookEmbed.EmbedAuthor("❌ Server stopped!", null, null))
                .setTimestamp(Instant.now())
                .build();
        sendMessage(null, embed);
    }

    public static void sendMessage(String content, WebhookEmbed embed) {
        if (webhookClient == null) return;
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername("Server");
        builder.setAvatarUrl("https://i.imgur.com/SXd58i2.png");
        if (content != null && !content.isEmpty()) {
            builder.setContent(content);
        }
        if (embed != null) {
            builder.addEmbeds(embed);
        }
        webhookClient.send(builder.build());
    }
}
