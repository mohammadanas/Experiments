package mini.projects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import redis.clients.jedis.Jedis;


public class RedisApp {

    private FileDB db;
    private Jedis jedis;

    public RedisApp() {
        this.db = new FileDB("database.txt");
        this.jedis = connectToRedisCluster();
    }

    String getGreeting() {
        return "Yupp!!";
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        
        RedisApp app = new RedisApp();

        app.handleClientRequests();
    }

    private void handleClientRequests() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;

        System.out.println("Redis-like CLI. Available commands:");
        System.out.println("SET key value - Set a key-value pair");
        System.out.println("GET key - Get value for a key");
        System.out.println("DEL key - Delete a key-value pair");
        System.out.println("EXIT - Exit the program");
        
        while (running) {
            try {
                System.out.print("> ");
                String input = reader.readLine();
                String[] parts = input.trim().split("\\s+");

                if (parts.length == 0) continue;

                String command = parts[0].toUpperCase();
                
                switch (command) {
                    case "SET":
                        db.set(parts[1], parts[2]);
                        jedis.del(parts[1]);
                        break;
                    case "GET":
                        String val = jedis.get(parts[1]);
                        
                        if (val == null) {
                            val = db.get(parts[1]);
                            if (val == null) {
                                System.out.println("GET failed: key: " + parts[1] + " not found");
                            } else {
                                System.out.println("GET: retrieved from DB: " + val);
                                jedis.set(parts[1], val);
                            }
                        } else {
                            System.out.println("GET: retrieved from cache: " + val);
                        }
                        
                        break;
                    case "EXIT":
                        running = false;
                        break;
                    default:
                        System.out.println("Unknown command. Available commands: SET, GET, DEL, EXIT");
                }

            } catch (IOException e) {
                System.err.println("Error reading input: " + e.getMessage());
            }
        }
    }
    
    private Jedis connectToRedisCluster() {
        try {
            return new Jedis("localhost", 6379);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to Redis: " + e.getMessage());
        } finally {
            if (this.jedis != null) this.jedis.close();
        }
    }
}
