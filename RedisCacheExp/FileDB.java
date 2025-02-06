package mini.projects;

import java.io.*;
import java.util.*;


public class FileDB {
    private String dbFile;
    private Map<String, Long> offsets;
    private String separator;

    public FileDB(String filename) {
        this.dbFile = filename;
        this.offsets = new HashMap<>();
        this.separator = ",";
    }

    public void set(String key, String value) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dbFile, true))) {
            long position = new File(dbFile).length();
            offsets.put(key, position);
           
            StringBuilder sb = new StringBuilder();
            sb.append(key).append(separator).append(value);
            writer.write(sb.toString());
            writer.newLine();
            // Get the file length after writing
            writer.flush();
        
            System.out.println("Successfully written to database: " + key + " - " + value);
        } catch (IOException e) {
            System.err.println("Error appending to database: " + e.getMessage());
        }
    }

    public String get(String key) {
        // Check if we have the offset for this key
        if (offsets.containsKey(key)) {
            try (RandomAccessFile file = new RandomAccessFile(dbFile, "r")) {
                // Seek to the position where the key-value pair starts
                file.seek(offsets.get(key));
                String line = file.readLine();
                if (line != null) {
                    String[] parts = line.split(separator);
                    if (parts.length == 2 && parts[0].equals(key)) {
                        return parts[1];
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading from offset: " + e.getMessage());
            }
        }

        return null;
    }

    public void delete(String key) {
        throw new IllegalArgumentException("Delete operation is not supported");
    }

    // public static void main(String[] args) {
    //     FileDB db = new FileDB("database.txt");
        
    //     // Example usage
    //     db.set("name", "John");
    //     db.set("age", "30");
        
    //     System.out.println("Name: " + db.get("name"));
    //     System.out.println("Age: " + db.get("age"));
        
    //     db.delete("age");
    // }
}
