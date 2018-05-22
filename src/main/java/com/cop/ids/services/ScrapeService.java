package com.cop.ids.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cop.ids.AppConfig;
import com.cop.ids.data.Chapter;
import com.cop.ids.data.Title;
import com.cop.ids.processes.SectionScrapeProcess;
import com.mongodb.MongoClient;

public class ScrapeService {

	MongoOperations mongoOps;

	public static String BASE_URL = "https://legislature.idaho.gov";

	public ScrapeService() {
		mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), "ids"));
	}

	/**
	 * Gather and store all Idaho State Law data into the mongo DB repository "ids".
	 * 
	 * @return true if successful
	 */
	public boolean scrape() {
	    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	    ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
		try {
			HashMap<Integer, String> titleLinks = getTitles();
			System.out.println("Titles loaded: " + titleLinks.size());
			for (Integer titleId : titleLinks.keySet()) {

				HashMap<Integer, String> chapterLinks = getChapters(titleId, titleLinks.get(titleId));
				//Add each chapter to the execution queue for processing
				for (Integer chapterId : chapterLinks.keySet()) {
				    taskExecutor.execute(new SectionScrapeProcess(titleId, chapterId, chapterLinks.get(chapterId), mongoOps));
				}
				System.out.println("Chapters loaded: " + chapterLinks.size());
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally {
			context = null;
		}
		return true;
	}

	/**
	 * Populate title collection with idaho state titles
	 * 
	 * @return A list of links to each of the individual title pages
	 * @throws IOException
	 */
	public HashMap<Integer, String> getTitles() throws IOException {
		HashMap<Integer, String> titleLinks = new HashMap<Integer, String>();

		Document doc = Jsoup.connect(BASE_URL + "/statutesrules/idstat/").get();
		
		Element pageBody = doc.getElementsByClass("parent-section no-padding").get(0);
		
		Elements titles = pageBody.getElementsByTag("tr");
		
		int id = 1;
		for (Element title : titles) {
			Elements cols = title.getElementsByTag("td");
			String link = cols.get(0).getElementsByTag("a").get(0).attr("href");
			String name = cols.get(2).text();
			if (mongoOps.findById(id, Title.class) == null) {
				mongoOps.insert(new Title(id, name));
				titleLinks.put(id, link);
			}
			id++;

		}
		return titleLinks;

	}

	/**
	 * Populate section collection with sections from a specific title
	 * 
	 * @param titleId  titleId to be stored as secondary key
	 * @param titleLink  link to visit the specified title page
	 * @return A list of links to each of the individual section pages
	 * @throws IOException
	 */
	public HashMap<Integer, String> getChapters(int titleId, String titleLink) throws IOException {
		
		HashMap<Integer, String> chapterLinks = new HashMap<Integer, String>();
		
		Document doc = Jsoup.connect(BASE_URL + titleLink).get();
		
		Element pageBody = doc.getElementsByClass("parent-section no-padding").get(0);
	
		Elements chapters = pageBody.getElementsByTag("tr");
		
		int id = 1;
		for (Element chapter : chapters) {
			try {
				Elements cols = chapter.getElementsByTag("td");
				Elements aref = cols.get(0).getElementsByTag("a");
				String name = cols.get(2).text();
				if (!aref.isEmpty())  chapterLinks.put(id, aref.get(0).attr("href"));
				if (mongoOps.findById(id, Chapter.class) == null) {
					mongoOps.insert(new Chapter(id, name, titleId));
				}
				id++;
			} catch (Exception e) {
				System.out.println("ERROR - skipping" + BASE_URL + titleLink + ":" + e.getStackTrace());
			}
		}
		return chapterLinks;
	}

	/**
	 * Populate section collection with individual sections from a given chapter
	 * 
	 * @param titleId secondary key for the title this section is contained within
	 * @param chapterId  secondary key for the chapter this section is contained within
	 * @param chapterLink chapter link to visit the specified chapter page
	 * @return A list of section id's corresponding with each of the sections added
	 * @throws IOException
	 */

}
