import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        var gkl = new GlobalKeyListener();
        var baseUrl = args.length == 0 ? "http://127.0.0.1:8080/" : args[0];
        var ep = new ClientEntryPoint(new ClientPreferences(), new ApiClient(baseUrl), gkl, baseUrl);
        Main.RegisterHook(gkl);
        ep.Run();
    }


    public static void RegisterHook(NativeKeyListener nkl) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        logger.setUseParentHandlers(false);
        GlobalScreen.addNativeKeyListener(nkl);
    }
}


