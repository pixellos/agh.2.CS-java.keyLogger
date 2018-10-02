import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.function.Predicate;

public class GlobalKeyListener implements NativeKeyListener {

    private ArrayList<Predicate<NativeKeyEvent>> events;

    public void Subscribe(Predicate<NativeKeyEvent> predicate)
    {
        this.events.add(predicate);
    }

    public GlobalKeyListener()
    {
        this.events = new ArrayList<Predicate<NativeKeyEvent>>();
    }

    public void nativeKeyPressed(NativeKeyEvent e) {

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        for(var i = this.events.iterator(); i.hasNext();)
        {
            var item = i.next();
            var result = item.test(e);
            if(result == false)
            {
                break;
            }
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        for(var i = this.events.iterator(); i.hasNext();)
        {
            var item = i.next();
            var result = item.test(e);
            if(result == false)
            {
                break;
            }
        }
    }
}
