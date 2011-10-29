package com.weina.test.tianji.api.model;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Feed {
	private String name;
	private List<SenderClass> data;


	public List<SenderClass> getData() {
		return data;
	}

	public void setData(List<SenderClass> data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class SenderClass{
		private String name;
		private Date updated_time;
		private String link;
		private String label;
		private String message;
		private From from;
		
		public From getFrom() {
			return from;
		}
		public void setFrom(From from) {
			this.from = from;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Date getUpdated_time() {
			return updated_time;
		}
		public void setUpdated_time(Date updated_time) {
			this.updated_time = updated_time;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
		@Override
		public String toString() {
			return "SenderClass [name=" + name + ", updated_time=" + updated_time + ", link=" + link + ", label=" + label + ", message=" + message + ", from=" + from + "]";
		}				
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class From{
		private String picture_small;

		public String getPicture_small() {
			return picture_small;
		}

		public void setPicture_small(String picture_small) {
			this.picture_small = picture_small;
		}

		@Override
		public String toString() {
			return "From [picture_small=" + picture_small + "]";
		}
		
	}
	
}
