package me.fukudck.wynnbombrelay.client;

import com.wynntils.core.text.StyledText;
import com.wynntils.models.worlds.BombModel;
import com.wynntils.models.worlds.type.BombInfo;
import com.wynntils.models.worlds.type.BombType;

import java.util.regex.Pattern;

public class BombParse {

    private final BombModel bombModel = new BombModel();

    // Test in BombModel_BOMB_BELL_PATTERN
    private static final Pattern BOMB_BELL_PATTERN = Pattern.compile(
            "^§#fddd5cff(?:\uE01E\uE002|\uE001) (?<user>.+) has thrown an? §#f3e6b2ff(?<bomb>.+) Bomb§#fddd5cff on §#f3e6b2ff§n(?<server>.+)$");

    // Test in BombModel_BOMB_EXPIRED_PATTERN
    private static final Pattern BOMB_EXPIRED_PATTERN = Pattern.compile(
            "^§#a0c84bff(?:\uE014\uE002|\uE001) §#ffd750ff.+§#a0c84bff (?<bomb>.+) Bomb has expired!.*$");

    // Test in BombModel_BOMB_THROWN_PATTERN
    private static final Pattern BOMB_THROWN_PATTERN =
            Pattern.compile("^§#a0c84bff(?:\uE014\uE002|\uE001) §l(?<bomb>.+) Bomb$");

    public static BombInfo parse(StyledText unwrapped) {
        try {
            var bellMatcher = unwrapped.getMatcher(BOMB_BELL_PATTERN);
            if (bellMatcher.matches()) {
                BombType type = BombType.fromString(bellMatcher.group("bomb"));
                if (type == null) return null;
                return new BombInfo(
                        bellMatcher.group("user"),
                        type,
                        bellMatcher.group("server").trim(),
                        System.currentTimeMillis(),
                        type.getActiveMinutes()
                );
            }

            var localMatcher = unwrapped.getMatcher(BOMB_THROWN_PATTERN);
            if (localMatcher.matches()) {
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
