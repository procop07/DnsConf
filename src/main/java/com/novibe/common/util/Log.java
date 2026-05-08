package com.novibe.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static String prefix() {
        String time = LocalDateTime.now().format(TIME_FMT);
        String caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames
                        .skip(2)
                        .findFirst()
                        .map(f -> f.getDeclaringClass().getSimpleName())
                        .orElse("?"));
        return "[%s] [%s] ".formatted(time, caller);
    }

    public static void global(String msg) {
        IO.println(Color.YELLOW_BOLD + "\n#==#==# " + Color.GREEN_BOLD + msg + Color.YELLOW_BOLD + " #==#==#\n" + Color.RESET);
    }

    public static void step(String msg) {
        IO.println(prefix() + Color.BLUE_BOLD + "--- " + msg + Color.RESET);
    }

    public static void io(String msg) {
        IO.println(prefix() + Color.YELLOW + ">>> " + Color.PURPLE + msg + Color.RESET);
    }

    public static void fail(String msg) {
        IO.println(prefix() + Color.YELLOW_BOLD + "!!! " + Color.RED + msg + Color.RESET);
    }

    public static void common(String msg) {
        IO.println(prefix() + msg);
    }

    public static void progress(String msg) {
        IO.print(msg + "\r");
    }

    private static class Color {

        public static final String RESET = "\033[0m";

        public static final String RED = "\033[0;31m";
        public static final String YELLOW = "\033[0;33m";
        public static final String PURPLE = "\033[0;35m";
        public static final String BLUE_BOLD = "\033[1;34m";
        public static final String YELLOW_BOLD = "\033[1;93m";
        public static final String GREEN_BOLD = "\033[1;92m";
    }

}

