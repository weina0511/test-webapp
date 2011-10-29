package com.weina.test.tianji.api.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)
public class Card {
	private List<Data> data;
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Data{
		private List<String> emails;
		private List<Phones> phones;
		
		public List<Phones> getPhones() {
			return phones;
		}

		public void setPhones(List<Phones> phones) {
			this.phones = phones;
		}

		public List<String> getEmails() {
			return emails;
		}

		public void setEmails(List<String> emails) {
			this.emails = emails;
		}

		@Override
		public String toString() {
			return "Data [emails=" + emails + ", phones=" + phones + "]";
		}

		
		
	}
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Phones{
		private String number;
		private String type;
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		@Override
		public String toString() {
			return "Phones [number=" + number + ", type=" + type + "]";
		}
		
	}
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Card [data=" + data + "]";
	}
	
}
