import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Start {
	private static String input;
	private static char delimiter = '|';
	private static String savePath = "Output/premium.txt";
	private static String savePath2 = "Output/accounts.txt";
	private static String userAgentsPath;
	private static int maxThreads = 5;
	private static String proxyFile = "Resources/proxies.txt";
	private static int attempts = 5;
	
    public static void main(String[] args) {
		//for testing
    	//args = new String[] { "-f", "combos.txt", "-d", "|"};
    	
    	//allow basic auth for proxies
		Properties props = System.getProperties();
		props.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    	
        Map<String, String> flagMap = new HashMap<>();

        // Check if any arguments were passed
        if (args.length == 0) {
            help();
            return;
        }

        // Process arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            // Check if the argument is a flag
            if (arg.startsWith("-") && (i + 1) < args.length) {
                String flag = arg;
                String value = args[i + 1];
                flagMap.put(flag, value);
                i++;
            } else {
                System.err.println("Invalid flag usage for: " + arg);
                return;
            }
        }

        for (Map.Entry<String, String> entry : flagMap.entrySet()) {
        	//required
            if (entry.getKey().equals("-f") || entry.getKey().equals("--file")) {
            	input = entry.getValue();
            }
            //optional
            else if (entry.getKey().equals("-d") || entry.getKey().equals("--delimiter")) {
            	if (entry.getValue().length() != 1) {
            		System.err.println("-d must be followed by a single character");
            	}
            	delimiter = entry.getValue().charAt(0);
            }
            else if (entry.getKey().equals("--output-file")) {
            	savePath = entry.getValue();
            }
            else if (entry.getKey().equals("--output-normal-file")) {
            	savePath2 = entry.getValue();
            }
            else if (entry.getKey().equals("--useragents-file")) {
            	userAgentsPath = entry.getValue();
            }
            else if (entry.getKey().equals("--threads")) {
            	maxThreads = Integer.parseInt(entry.getValue());
            }
            else if (entry.getKey().equals("--proxy-file")) {
            	proxyFile = entry.getValue();
            }
            else if (entry.getKey().equals("--attempts")) {
            	attempts = Integer.parseInt(entry.getValue());
            }
        }
        
        if (userAgentsPath != null)
        	Scrapper.setJSONFile(userAgentsPath);
        HubManager.setSettings(maxThreads);
        Combo.setDelimiter(delimiter);
        Scrapper.setDelimiter(delimiter);
        Scrapper.setProxyFile(proxyFile);
        Scrapper.setAttempts(attempts);
        
        //verify required params are set
        if (input.isBlank()) {
        	System.err.println("Required Paramaters Not Given");
        	System.out.println("\n");
        	help();
        }
        
    	try {
    		//load combos
			File f = new File(input);
			Combo combos = new Combo(f);
    		
    		//scrape
    		HubManager hubScrapper = new HubManager(combos.getAllCombos()[0],
    				combos.getAllCombos()[1]);
    		System.out.println("Completed! Saving Data");
    		
    		//Save info
    		Output output = new Output(hubScrapper);
    		Output.export(savePath, output.getPremium());
    		if (!savePath2.isEmpty())
    			Output.export(savePath2, output.getAccounts());
    		
    		//Print Statistics
    		System.out.println("Results:");
    		
    		System.out.println("PornHub:");
    		System.out.println("\tValid Accounts: " + hubScrapper.getValidCount());
    		System.out.println("\tPercent Valid: " + hubScrapper.getValidPercent());
    		System.out.println("\tPremium Accounts: " + hubScrapper.getPremiumLogCount());
    		System.out.println("\tPercent Premium: " + hubScrapper.getPremiumPercent());
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void help() {
    	System.out.println("Help:");
    	
    	System.out.println("\tRequired Flags:");
    	System.out.println("\t\t-f or --file:      File with combos");
    	
    	System.out.println("\tOptional Flags:");
    	System.out.println("\t\t-d or --delimiter:      Character used to seperate emails and passwords");
    	System.out.println("\t\t                        (Must be same in combos to check and in instagram logs)");
    	System.out.println("\t\t                        (Defualt is |)");
    	System.out.println("\t\t--output-file:          Program will save information to this file in json format");
    	System.out.println("\t\t                        (Defualt is Output/output.json)");
    	System.out.println("\t\t--output-normal-file:   Program will save information to this file in json format");
    	System.out.println("\t\t                        (Will only save premium accounts if this is not set)");
    	System.out.println("\t\t--useragents-file:      Program will read list of user agents from this file");
    	System.out.println("\t\t                        (Defualt is Resources/userAgents.json)");
    	System.out.println("\t\t                        (Download lists from: https://www.useragents.me/)");
    	System.out.println("\t\t--threads:              How many threads to use");
    	System.out.println("\t\t                        (Defualt is 5)");
    	System.out.println("\t\t--attempts:             How many times to retry a failed connection with different Proxy and UserAgent");
    	System.out.println("\t\t                        (Defualt is 5)");
    	System.out.println("\t\t--proxy-file:           File to read proxies from");
    	System.out.println("\t\t                        (Defualt is Resources/proxies.txt)");
    }
}
