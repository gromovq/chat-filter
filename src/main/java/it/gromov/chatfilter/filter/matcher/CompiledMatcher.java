package it.gromov.chatfilter.filter.matcher;

import java.util.regex.Pattern;

public class CompiledMatcher {
    private final Pattern pattern;
    private final String source;

    public CompiledMatcher(String source) {
        this.source = source;
        this.pattern = Pattern.compile(source);
    }

    public boolean matches(String text) {
        return pattern.matcher(text).find();
    }

    public String getSource() {
        return source;
    }
}
