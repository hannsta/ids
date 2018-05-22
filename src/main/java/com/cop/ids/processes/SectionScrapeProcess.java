package com.cop.ids.processes;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.mongodb.core.MongoOperations;

import com.cop.ids.data.Section;
import com.cop.ids.services.ScrapeService;

public class SectionScrapeProcess implements Runnable {

	private int titleId;
	private int chapterId;
	private String chapterLink;
	private MongoOperations mongoOps;

	public SectionScrapeProcess(int titleId, int chapterId, String chapterLink, MongoOperations mongoOps) {
		this.titleId = titleId;
		this.chapterId = chapterId;
		this.chapterLink = chapterLink;
		this.mongoOps = mongoOps;
	}

	@Override
	public void run() {

		ArrayList<Integer> sectionIds = new ArrayList<Integer>();

		Document doc = null;
		try {
			doc = Jsoup.connect(ScrapeService.BASE_URL + chapterLink).get();
		} catch (Exception e) {
			return;
		}
		if (doc == null)
			return;

		Element pageBody = doc.getElementsByClass("parent-section no-padding").get(0);

		Elements sections = pageBody.getElementsByTag("tr");

		int id = 1;
		for (Element section : sections) {
			try {
				Elements cols = section.getElementsByTag("td");
				Elements aref = cols.get(0).getElementsByTag("a");
				if (aref.isEmpty() || (mongoOps.findById(id, Section.class) != null)) {
					continue;
				}
				String sectionLink = aref.get(0).attr("href");
				String sectionNumber = aref.get(0).text();
				String sectionName = cols.get(2).text();
				System.out.println(ScrapeService.BASE_URL + sectionLink);
				Document sectionDoc = Jsoup.connect(ScrapeService.BASE_URL + sectionLink).get();

				Elements textLines = sectionDoc.getElementsByClass("f11s");

				StringBuffer lawText = new StringBuffer("");
				StringBuffer historyText = new StringBuffer("");

				boolean textBodyFound = false;
				boolean historyFound = false;
				for (Element line : textLines) {
					if (line.text().startsWith((sectionNumber)))
						textBodyFound = true;
					if (line.text().startsWith("[(") || line.text().startsWith("["+sectionNumber))
						historyFound = true;
					if (textBodyFound && !historyFound) {
						lawText.append("\n" + line.text());
					} else if (textBodyFound && historyFound) {
						historyText.append(line.text());
					}
				}
				String law = lawText.toString().replace(sectionNumber + ". ", "").replaceFirst("\n", "");
				law = law.substring(law.indexOf(".")+2);
				String history = historyText.toString();
				mongoOps.insert(new Section(titleId, chapterId, sectionName, sectionNumber, law, history));
				sectionIds.add(id);

			} catch (Exception e) {
				System.out.println("ERROR - skipping " + ScrapeService.BASE_URL + chapterLink);
				e.printStackTrace();

				
			}
		}
	}

}
