package com.MasterWorker.WebCrawler;

import java.util.*;
import java.util.concurrent.*;

// Task to represent fetching and parsing a web page
class CrawlTask {
    private final String url;

    public CrawlTask(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

// Worker to fetch and parse web pages
class CrawlerWorker implements Callable<List<String>> {
    private final CrawlTask task;

    public CrawlerWorker(CrawlTask task) {
        this.task = task;
    }

    @Override
    public List<String> call() throws Exception {
        // Simulate fetching and parsing web page
        Thread.sleep(1000); // Simulate processing time

        // Return links found on the web page (simulated)
        return Arrays.asList(task.getUrl() + "/link1", task.getUrl() + "/link2", task.getUrl() + "/link3");
    }
}

// Master to coordinate web crawling tasks
class WebCrawlerMaster {
    private final ExecutorService executor;
    private final List<Future<List<String>>> results;

    public WebCrawlerMaster(int numWorkers) {
        executor = Executors.newFixedThreadPool(numWorkers);
        results = new CopyOnWriteArrayList<>(); // Thread-safe list
    }

    public void submitTask(CrawlTask task) {
        results.add(executor.submit(new CrawlerWorker(task)));
    }

    public void awaitCompletion() throws InterruptedException, ExecutionException {
        for (Future<List<String>> result : results) {
            System.out.println("Links found on page: " + result.get()); // Print links found on web page
        }
        executor.shutdown();
    }
}

public class WebCrawler {
    public static void main(String[] args) {
        // Example usage
        WebCrawlerMaster master = new WebCrawlerMaster(4); // 4 worker threads
        master.submitTask(new CrawlTask("https://example.com/page1"));
        master.submitTask(new CrawlTask("https://example.com/page2"));
        master.submitTask(new CrawlTask("https://example.com/page3"));

        try {
            master.awaitCompletion();
            System.out.println("Web crawling completed successfully.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
