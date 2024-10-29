import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Output {
	private final HubManager hub;
	private String premium = "";
	private String accounts = "";
	public Output(HubManager hub) {
		this.hub = hub;
	}
	
	public String getPremium() {
		if (premium == null)
			build();
		return premium;
	}
	
	public String getAccounts() {
		if (accounts == null)
			build();
		return accounts;
	}
	
	private void build() {
		for (Hub acc : hub.getAccounts()) {
			if (acc.isValid() && acc.isPremium())
				premium += acc.getAsLog() + "\n";
			else if (acc.isValid())
				accounts += acc.getAsLog() + "\n";
		}
	}
	
	public static void export(String logs, String fileName) {
        String filePath = fileName;
        
        // Check if the parent directory exists, if not create it
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); // Create the directory structure
        }
        
        int count = 1;
        // Check if the file already exists and modify the filename if necessary
        while (new File(filePath).exists()) {
            filePath = fileName.replace(".json", "") + "(" + count + ").json";
            count++;
        }
		
		//export to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(logs);
        } catch (Exception e) {
        	System.err.println("Failed to save to file");
            e.printStackTrace();
        }
	}
}
