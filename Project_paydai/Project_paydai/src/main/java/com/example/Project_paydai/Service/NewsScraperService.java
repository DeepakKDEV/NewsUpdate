package com.example.Project_paydai.Service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsScraperService {

    private static final Logger log = LoggerFactory.getLogger(NewsScraperService.class);

    public List<Map<String, String>> scrapeGoogleNews(String searchTerm) {
        List<Map<String, String>> newsUrls = new ArrayList<>();
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            // Navigate to Google News
            page.navigate("https://news.google.com/");

            // Wait for the page to load
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);

            // Perform the search
            page.fill("input[type='text']", searchTerm);
            page.keyboard().press("Enter");

            // Wait for search results
            page.waitForSelector("article a[href]");

            // Extract the URLs from search results
            List<ElementHandle> linkElements = page.locator("article a[href]").elementHandles();

            // Limit the number of results to 5
            int limit = Math.min(5, linkElements.size());

            for (int i = 0; i < limit; i++) {
                ElementHandle element = linkElements.get(i);
                String href = element.getAttribute("href");
                if (href != null) {
                    // Extract the actual article URL if necessary
                    String articleUrl = href;

                    // Check if the URL is a full URL or a relative path
                    if (articleUrl.startsWith("/")) {
                        articleUrl = "https://news.google.com/search?q=indian%20sports%20news&hl=en-IN&gl=IN&ceid=IN%3Aen" + articleUrl;
                    }

                    // Create a Map to store the URL in JSON format
                    Map<String, String> result = new HashMap<>();
                    result.put("url", articleUrl);

                    // Add the result to the list
                    newsUrls.add(result);
                }
            }

            // Close the browser
            browser.close();
        } catch (Exception e) {
            log.error("Error occurred while scraping news: ", e);
        }
        return newsUrls;
    }
}

