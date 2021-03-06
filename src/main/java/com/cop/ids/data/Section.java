package com.cop.ids.data;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;

@Cacheable
public class Section {
	
    @Id
    private ObjectId id;
    
    private int titleId;
    private int chapterId;
    private String titleName;
    private String chapterName;
    private String number;
    @TextIndexed(weight=8) private String name;
	@TextIndexed(weight=2) private String text;
	private String history;
	
	public Section() {
		
	}
	public Section(int titleId, int chapterId, String name, String number, String text, String history) {
		this.titleId=titleId;
		this.chapterId=chapterId;
		this.name=name;
		this.number=number;
		this.text=text;
		this.history=history;	
	}
	public String getId() {
		return id.toHexString();
	}
	public void setId(ObjectId id) {

		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}


	public int getTitleId() {
		return titleId;
	}


	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}


	public int getChapterId() {
		return chapterId;
	}


	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	
	
}
