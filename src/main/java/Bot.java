import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.toggle.BareboneToggle;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.WeatherService;
import service.WeatherServiceImpl;

import java.util.function.Predicate;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public class Bot extends AbilityBot {

    private final static String BOT_TOKEN;
    private final static String BOT_NAME;
    private final static int CREATOR_ID;

    private WeatherService weatherService;

    static {
        BOT_TOKEN = System.getenv("BOT_TOKEN");
        BOT_NAME = System.getenv("BOT_NAME");
        CREATOR_ID = Integer.parseInt(System.getenv("CREATOR_ID"));
    }

    public Bot() {
        super(BOT_TOKEN, BOT_NAME, new BareboneToggle());
        weatherService = new WeatherServiceImpl();
    }

    @Override
    public int creatorId() {
        return CREATOR_ID;
    }

    public Ability startCommand() {
        String message = "Привіт! Щоб отримати дані про погоду на даний момент використовуй команду /current" +
                "\n\nДетальний прогноз на 5 наступних днів — команда /forecast";
        return Ability
                .builder()
                .name("start")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> silent.send(message, ctx.chatId()))
                .build();
    }

    public Ability currentCommand() {
        String message = "У якому місті ти хочеш дізнатися погоду?";
        return Ability.builder()
                .name("current")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> silent.forceReply(message, ctx.chatId()))
                .reply(upd -> silent.sendMd(weatherService.getCurrentWeather(upd.getMessage().getText()), upd.getMessage().getChatId()),
                        Flag.MESSAGE, Flag.REPLY, isReplyToBot(), isReplyToMessage(message))
                .build();
    }

    public Ability forecastCommand() {
        String message = "Для якого міста ти хочеш отримати прогноз погоди?";
        return Ability.builder()
                .name("forecast")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action(ctx -> silent.forceReply(message, ctx.chatId()))
                .reply(upd -> silent.sendMd(weatherService.getWeatherForecast(upd.getMessage().getText()), upd.getMessage().getChatId()),
                        Flag.MESSAGE, Flag.REPLY, isReplyToBot(), isReplyToMessage(message))
                .build();
    }

    private Predicate<Update> isReplyToMessage(String message) {
        return upd -> {
            Message reply = upd.getMessage().getReplyToMessage();
            return reply.hasText() && reply.getText().equalsIgnoreCase(message);
        };
    }

    private Predicate<Update> isReplyToBot() {
        return upd -> upd.getMessage().getReplyToMessage().getFrom().getUserName().equalsIgnoreCase(getBotUsername());
    }
}
