import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HubManager {
	//static settings
	private static int threads;
	
	private Set<Hub> premiumLogs = new HashSet<Hub>();
	private Set<Hub> accs = new HashSet<Hub>();
	private final int startingSize;
	
    public HubManager(String[] emails, String[] passwords) {
        startingSize = emails.length;

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(startingSize);

        for (int i = 0; i < emails.length; i++) {
            final int index = i; // To use in the lambda expression
            executor.submit(() -> {
                try {
                    Hub acc = new Hub(emails[index], passwords[index]);
                    if (acc.isValid()) {
                        if (acc.isPremium()) {
                            synchronized (premiumLogs) {
                                premiumLogs.add(acc);
                            }
                        } else {
                            synchronized (accs) {
                                accs.add(acc);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                    System.out.printf("Progress: %d/%d complete%n", startingSize - latch.getCount(), startingSize);
                }
            });
        }
        //wait on threads
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            e.printStackTrace();
        }
    }
    
    
    //set static settings
    public static void setSettings(int maxThreads) {
    	threads = maxThreads;
    }
	
	//gets
	public Set<Hub> getPremiumLogs() {
		return premiumLogs;
	}
	public Set<Hub> getAccounts() {
		return accs;
	}
	
	//statistics
	public int getValidCount() {
		return accs.size() + premiumLogs.size();
	}
	public double getValidPercent() {
		return getValidCount() / (double)startingSize;
	}
	public int getPremiumLogCount() {
		return premiumLogs.size();
	}
	public double getPremiumPercent() {
		return premiumLogs.size() / (double)getValidCount();
	}
}
