package util;

import java.util.HashMap;
import java.util.Map;

public class EmojiUtils {

    private final static Map<String, String > EMOJI_UNICODES;

    static {
        EMOJI_UNICODES = new HashMap<>();
        EMOJI_UNICODES.put("Thunderstorm", "\uD83C\uDF29");
        EMOJI_UNICODES.put("Drizzle", "\uD83C\uDF27");
        EMOJI_UNICODES.put("Rain", "\uD83C\uDF26");
        EMOJI_UNICODES.put("Snow", "\uD83C\uDF28");
        EMOJI_UNICODES.put("Mist", "\uD83C\uDF2B");
        EMOJI_UNICODES.put("Smoke", "\uD83C\uDF2B");
        EMOJI_UNICODES.put("Haze", "\uD83C\uDF2B");
        EMOJI_UNICODES.put("Sand", "\uD83D\uDCA8");
        EMOJI_UNICODES.put("Dust", "\uD83D\uDCA8");
        EMOJI_UNICODES.put("Ash", "\uD83C\uDF2B");
        EMOJI_UNICODES.put("Squall", "\uD83D\uDCA8");
        EMOJI_UNICODES.put("Tornado", "\uD83C\uDF2A");
        EMOJI_UNICODES.put("Clear", "\u2600");
        EMOJI_UNICODES.put("Clouds", "\u2601");
    }

    public static String getEmoji(String key) {
        return EMOJI_UNICODES.getOrDefault(key, "\uD83E\uDD37");
    }
}
