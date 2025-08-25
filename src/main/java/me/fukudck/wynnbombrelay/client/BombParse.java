package me.fukudck.wynnbombrelay.client;

import com.wynntils.core.text.StyledText;
import com.wynntils.models.worlds.BombModel;
import com.wynntils.models.worlds.type.BombInfo;
import com.wynntils.models.worlds.type.BombType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BombParse {

    private final BombModel bombModel = new BombModel();

    // Test in BombModel_BOMB_BELL_PATTERN
    private static final Pattern BOMB_BELL_PATTERN = Pattern.compile(
            "^§#fddd5cff(?:\uE01E\uE002|\uE001) (?<user>.+) has thrown an? §#f3e6b2ff(?<bomb>.+) Bomb§#fddd5cff (?<server>.+)$", Pattern.DOTALL);


    // Test in BombModel_BOMB_THROWN_PATTERN
    private static final Pattern BOMB_THROWN_PATTERN =
            Pattern.compile("^§#a0c84bff(?:\uE014\uE002|\uE001) §l(?<bomb>.+) Bomb$");

    public static BombInfo parse(StyledText unwrapped) {
        try {
            var bellMatcher = unwrapped.getMatcher(BOMB_BELL_PATTERN);
            if (bellMatcher.matches()) {
                BombType type = BombType.fromString(bellMatcher.group("bomb"));
                if (type == null) return null;

                String server = bellMatcher.group("server").trim();

                Pattern pattern = Pattern.compile("(AS|EU|NA)\\d{1,2}$");
                Matcher matcher = pattern.matcher(server);

                if (server.length() > 4 && matcher.find()) {
                    server = matcher.group();
                }


                return new BombInfo(
                        bellMatcher.group("user"),
                        type,
                        server,
                        System.currentTimeMillis(),
                        type.getActiveMinutes()
                );
            }

            // Currently not implemented, It's work but can't detect player's name and server.
            var localMatcher = unwrapped.getMatcher(BOMB_THROWN_PATTERN);
            if (localMatcher.matches()) {
                System.out.println("[BOMB-DEBUG] BOMB_THROWN_PATTERN MATCHED ");
                BombType type = BombType.fromString(localMatcher.group("bomb"));
                if (type == null) return null;
                return new BombInfo(
                        "",
                        type,
                        "local",
                        System.currentTimeMillis(),
                        type.getActiveMinutes()
                );
            }


        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}
