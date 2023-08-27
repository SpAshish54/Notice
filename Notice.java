package controller;

public class Notice {
    private String title;
    private String content;
    private String contactName;
    private String phoneNumber;

    public Notice(String title, String content, String contactName, String phoneNumber) {
        this.title = title;
        this.content = content;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getContactName() {
        return contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String toString() {
    	return String.format("%s %s %s %s",title,content,contactName,phoneNumber);
    }
}

