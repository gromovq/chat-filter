package it.gromov.chatfilter.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import it.gromov.chatfilter.bootstrap.ChatFilterPlugin;
import it.gromov.chatfilter.database.model.StatsSnapshot;
import it.gromov.chatfilter.database.model.ViolationRecord;
import it.gromov.chatfilter.util.Color;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CommandAlias("zchatfilter|zcf")
@CommandPermission("zchatfilter.admin")
public class ZChatFilterCommand extends BaseCommand {

    private static final int PAGE_SIZE = 1;
    private final ChatFilterPlugin plugin;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    public ZChatFilterCommand(ChatFilterPlugin plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    public void reload(CommandSender sender) {
        plugin.reloadEverything();
        sender.sendMessage(Color.of(plugin.getPluginConfig().messages.reloadDone));
    }

    @Subcommand("info")
    @CommandCompletion("@players")
    public void info(CommandSender sender, @Optional String nick, @Default("1") int page) {
        StatsSnapshot snapshot = plugin.getViolationService().summary();
        sender.sendMessage(Color.of("&6&lZChatFilter &8| &fСтатистика"));
        sender.sendMessage(Color.of("&7Уникальных нарушителей: &eсегодня " + snapshot.uniqueToday + "&7, за 7 дней " + snapshot.uniqueWeek + "&7, всего " + snapshot.uniqueTotal));
        sender.sendMessage(Color.of("&7Нарушений: &eсегодня " + snapshot.violationsToday + "&7, за 7 дней " + snapshot.violationsWeek + "&7, всего " + snapshot.violationsTotal));

        if (nick == null || nick.trim().isEmpty()) {
            sender.sendMessage(Color.of("&7Используй: &e/zchatfilter info <ник> [страница]"));
            return;
        }

        int total = plugin.getViolationService().countByPlayer(nick);
        if (total == 0) {
            sender.sendMessage(Color.of("&cПо игроку " + nick + " нарушений не найдено."));
            return;
        }

        int maxPage = Math.max(1, (int) Math.ceil(total / (double) PAGE_SIZE));
        int currentPage = Math.max(1, Math.min(page, maxPage));
        int offset = (currentPage - 1) * PAGE_SIZE;

        List<ViolationRecord> pageRecords = plugin.getViolationService().byPlayer(nick, PAGE_SIZE, offset);
        if (!pageRecords.isEmpty()) {
            ViolationRecord record = pageRecords.get(0);
            sender.sendMessage(Color.of("&8&m------------------------------------------"));
            sender.sendMessage(Color.of("&eИгрок: &f" + record.playerName + " &8| &eВсего нарушений: &f" + total));
            sender.sendMessage(Color.of("&eПричина: &f" + record.reason));
            sender.sendMessage(Color.of("&eIP: &f" + record.ip));
            sender.sendMessage(Color.of("&eДата: &f" + formatter.format(Instant.ofEpochSecond(record.createdAt))));
            sender.sendMessage(Color.of("&eСообщение: &f" + record.message));
            sender.sendMessage(Color.of("&7Просмотр: &e" + currentPage + "&7/&e" + maxPage + " &8(" + currentPage + "/" + total + " наказаний)"));
            sendPager(sender, nick, currentPage, maxPage);
            sender.sendMessage(Color.of("&8&m------------------------------------------"));
        }
    }

    private void sendPager(CommandSender sender, String nick, int page, int maxPage) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.of("&7Навигация доступна игрокам в чате: /zchatfilter info " + nick + " " + Math.max(1, page - 1) + " / " + Math.min(maxPage, page + 1)));
            return;
        }

        Player player = (Player) sender;
        TextComponent prev = new TextComponent(Color.of("&c« Назад "));
        prev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zchatfilter info " + nick + " " + Math.max(1, page - 1)));
        prev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Открыть предыдущую запись").create()));

        TextComponent next = new TextComponent(Color.of("&aВперёд »"));
        next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zchatfilter info " + nick + " " + Math.min(maxPage, page + 1)));
        next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Открыть следующую запись").create()));

        player.spigot().sendMessage(prev, new TextComponent(Color.of(" &7| ")), next);
    }
}
