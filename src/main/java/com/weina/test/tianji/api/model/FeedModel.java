package com.weina.test.tianji.api.model;

public class FeedModel {
	private String userName;
	private String senderName;
	private String message;
	private String senderPhoto;
	private String updated_time;
	
	public String getUpdated_time() {
		return updated_time;
	}
	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSenderName() {
		return senderName;
	}
	@Override
	public String toString() {
		return "FeedModel [userName=" + userName + ", senderName=" + senderName + ", message=" + message + ", senderPhoto=" + senderPhoto + ", updated_time=" + updated_time + ", senderLink="
				+ senderLink + "]";
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSenderPhoto() {
		return senderPhoto;
	}
	public void setSenderPhoto(String senderPhoto) {
		this.senderPhoto = senderPhoto;
	}
	public String getSenderLink() {
		return senderLink;
	}
	public void setSenderLink(String senderLink) {
		this.senderLink = senderLink;
	}
	private String senderLink;
}
