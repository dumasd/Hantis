package com.thinkerwolf.hantis.example;

import java.util.Date;

import com.thinkerwolf.hantis.orm.annotation.Entity;
import com.thinkerwolf.hantis.orm.annotation.GeneratedValue;
import com.thinkerwolf.hantis.orm.annotation.Id;
import com.thinkerwolf.hantis.orm.annotation.Index;

@Entity
public class Blog {

	@Id
	@GeneratedValue
	private int id;

	@Index
	private int userId;

	private String title;

	private String content;

	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", content=" + content + ", createTime:" + createTime + "]";
	}

}