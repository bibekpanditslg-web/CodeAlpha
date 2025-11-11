import java.io.*;
import java.util.*;

// Class representing a stock
class Stock implements Serializable {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    @Override
    public String toString() {
        return symbol + " - $" + String.format("%.2f", price);
    }
}

// Class representing a user's portfolio
class User implements Serializable {
    private String name;
    private double balance;
    private Map<String, Integer> portfolio = new HashMap<>();

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cost > balance) {
            System.out.println("❌ Not enough balance!");
            return;
        }
        balance -= cost;
        portfolio.put(stock.getSymbol(), portfolio.getOrDefault(stock.getSymbol(), 0) + quantity);
        System.out.println("✅ Bought " + quantity + " shares of " + stock.getSymbol());
    }

    public void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.getSymbol(), 0);
        if (owned < quantity) {
            System.out.println("❌ Not enough shares to sell!");
            return;
        }
        double revenue = stock.getPrice() * quantity;
        balance += revenue;
        portfolio.put(stock.getSymbol(), owned - quantity);
        System.out.println("✅ Sold " + quantity + " shares of " + stock.getSymbol());
    }

    public void displayPortfolio(StockMarket market) {
        System.out.println("\n📊 Portfolio Summary for " + name);
        System.out.println("-----------------------------------");
        double totalValue = balance;
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            Stock stock = market.getStock(entry.getKey());
            if (stock != null) {
                double stockValue = entry.getValue() * stock.getPrice();
                System.out.printf("%s - %d shares @ $%.2f = $%.2f\n",
                        stock.getSymbol(), entry.getValue(), stock.getPrice(), stockValue);
                totalValue += stockValue;
            }
        }
        System.out.printf("💰 Cash Balance: $%.2f\n", balance);
        System.out.printf("📈 Total Portfolio Value: $%.2f\n", totalValue);
    }
}

// Class representing the stock market
class StockMarket implements Serializable {
    private Map<String, Stock> stocks = new HashMap<>();

    public StockMarket() {
        stocks.put("AAPL", new Stock("AAPL", 180.50));
        stocks.put("GOOG", new Stock("GOOG", 2800.75));
        stocks.put("AMZN", new Stock("AMZN", 3500.30));
        stocks.put("TSLA", new Stock("TSLA", 900.10));
    }

    public void displayMarket() {
        System.out.println("\n📈 Market Prices:");
        for (Stock stock : stocks.values()) {
            System.out.println(stock);
        }
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public void updatePrices() {
        Random rand = new Random();
        for (Stock stock : stocks.values()) {
            double change = (rand.nextDouble() - 0.5) * 10; // random price change
            stock.setPrice(Math.max(1, stock.getPrice() + change));
        }
    }
}

// Utility class for saving/loading data
class FileManager {
    public static void saveUser(User user) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.dat"))) {
            out.writeObject(user);
            System.out.println("💾 Portfolio saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User loadUser() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.dat"))) {
            return (User) in.readObject();
        } catch (Exception e) {
            System.out.println("No saved portfolio found. Starting new.");
            return null;
        }
    }
}

// Main trading simulator
public class StockTradingSimulator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StockMarket market = new StockMarket();

        User user = FileManager.loadUser();
        if (user == null) {
            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            user = new User(name, 10000); // initial balance
        }

        int choice;
        do {
            System.out.println("\n===== STOCK TRADING SIMULATOR =====");
            System.out.println("1. View Market Prices");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Update Market Prices");
            System.out.println("6. Save & Exit");
            System.out.print("Choose an option: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> market.displayMarket();
                case 2 -> {
                    System.out.print("Enter stock symbol: ");
                    String symbol = sc.next().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int qty = sc.nextInt();
                    Stock stock = market.getStock(symbol);
                    if (stock != null) user.buyStock(stock, qty);
                    else System.out.println("❌ Invalid stock symbol.");
                }
                case 3 -> {
                    System.out.print("Enter stock symbol: ");
                    String symbol = sc.next().toUpperCase();
                    System.out.print("Enter quantity: ");
                    int qty = sc.nextInt();
                    Stock stock = market.getStock(symbol);
                    if (stock != null) user.sellStock(stock, qty);
                    else System.out.println("❌ Invalid stock symbol.");
                }
                case 4 -> user.displayPortfolio(market);
                case 5 -> {
                    market.updatePrices();
                    System.out.println("📊 Market prices updated!");
                }
                case 6 -> FileManager.saveUser(user);
                default -> System.out.println("❌ Invalid option.");
            }
        } while (choice != 6);
    }
}
