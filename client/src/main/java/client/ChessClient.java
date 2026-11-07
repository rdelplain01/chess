package client;

import ui.Repl;


public class ChessClient {

    public static Repl repl;
    private String url;

    public ChessClient(String url) {
        this.url = url;
    }

    public void run() {
        repl = new Repl();
        repl.run(url);
    }
}
