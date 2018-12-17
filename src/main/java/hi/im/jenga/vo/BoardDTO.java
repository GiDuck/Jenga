package hi.im.jenga.vo;


import java.util.Arrays;

public class BoardDTO {

	String title;
	String bookmarks;
	String introduce;
	String[] category;
	String[] tags;
	Long time;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getBookmarks() {
		return bookmarks;
	}
	public void setBookmarks(String bookmarks) {
		this.bookmarks = bookmarks;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String[] getCategory() {
		return category;
	}
	public void setCategory(String[] category) {
		this.category = category;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "BoardDTO [title=" + title + ", bookmarks=" + bookmarks + ", introduce=" + introduce + ", category="
				+ Arrays.toString(category) + ", tags=" + Arrays.toString(tags) + ", time=" + time + "]";
	}
	
	
	
	

	
	
	
}
