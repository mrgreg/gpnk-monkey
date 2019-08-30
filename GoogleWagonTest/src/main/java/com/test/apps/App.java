package com.test.apps;

/**
 * Hello world!
 *
 */
public final class App {

    /**
     * To satisfy Checkstyle's HideUtilityClassConstructor.
     */
    private App() {
    }

    /**
     * Sample method.
     * @return Sum i + j.
     */
    public static int foo(final int i, final int j) {
        return i + j;
    }

    /**
     * Hello World method.
     * @param args - args
     */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(final String[] args) {
        System.out.println("Hello World!");
    }
}
