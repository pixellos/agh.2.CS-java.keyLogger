import java.awt.event.KeyEvent;

public class ClientEntryPoint {

    private ClientPreferences ClientPreferences;
    private ApiClient ApiClient;
    private GlobalKeyListener GlobalKeyListener;
    private String BaseUrl;
    private User User;

    public ClientEntryPoint(ClientPreferences cp, ApiClient ac, GlobalKeyListener gkc, String url) {
        this.ClientPreferences = cp;
        this.ApiClient = ac;
        this.GlobalKeyListener = gkc;
        this.BaseUrl = url;
        this.SetUser();
        gkc.Subscribe((k) -> {
            this.ApiClient.Report(k.paramString(), this.ClientPreferences.getKey());
            return true;
        });
    }

    public void SetUser() {
        var key = this.ClientPreferences.getKey();
        this.User = this.ApiClient.Authorize(key);
    }

    public void Run() {
        this.Greetings();
    }

    private void Greetings() {
        System.out.println("Hi, " + this.User.Name + ".");
        var url = this.BaseUrl + "/login";
        this.Authorize(url);

        var scanner = new java.util.Scanner(System.in);

        while (true) {
            System.out.println("Interactive mode. L to log out, Q to quit.");
            var c = scanner.next();
            switch (c) {
                case "Q":
                    System.exit(0);
                    break;
                case "L":
                    this.User = null;
                    this.ClientPreferences.setKey("");
                    this.Authorize(url);
                    break;
                default:
                    System.out.println("Try again");
                    break;
            }
        }
    }

    private void Authorize(String url) {
        System.out.print(Color.RED);
        if (this.User == null || !this.User.Authorized) {
            var scaner = new java.util.Scanner(System.in);
            while (this.User == null || !this.User.Authorized) {
                System.out.println("You're not authorized - please refresh your code by visiting " + url + ", log in with your google account to receive code.");
                System.out.print(Color.RESET);
                System.out.print("Your new code: ");
                var line = scaner.nextLine();
                this.ClientPreferences.setKey(line);
                this.SetUser();
                if(this.User != null)
                {
                    System.out.println("Hi, " + this.User.Name + ".");
                }
            }
        } else {
            System.out.println("You're authorized, this software is monitoring this pc.");
        }
        System.out.print(Color.RESET);
    }
}
