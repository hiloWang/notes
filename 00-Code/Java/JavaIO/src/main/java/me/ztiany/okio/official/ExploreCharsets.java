package me.ztiany.okio.official;

import okio.ByteString;
import okio.Utf8;

/**
 * NFC 和 NFD 的区别
 */
public final class ExploreCharsets {

    public void run() throws Exception {
        dumpStringData("Café \uD83C\uDF69"); // NFC: é is one code point.
        dumpStringData("Café \uD83C\uDF69"); // NFD: e is one code point, its accent is another.
    }

    public void dumpStringData(String s) {
        System.out.println("                       " + s);
        System.out.println("        String.length: " + s.length());
        System.out.println("String.codePointCount: " + s.codePointCount(0, s.length()));
        System.out.println("            Utf8.size: " + Utf8.size(s));
        System.out.println("          UTF-8 bytes: " + ByteString.encodeUtf8(s).hex());
        System.out.println();
    }

    public static void main(String... args) throws Exception {
        new ExploreCharsets().run();
    }
} 