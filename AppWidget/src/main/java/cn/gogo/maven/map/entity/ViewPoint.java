package cn.gogo.maven.map.entity;

import java.io.Serializable;

/**
 * @author Administrator
 * @category 景点详细信息类
 * 
 */
@SuppressWarnings("serial")
public class ViewPoint implements Serializable {
	private int id;
	private double lantitude;
	private double longitude;
	private String title;
	private String type;
	private int level;
	private int rating;
	private String image;
	private String content;
	private String audio;
	private String panoramic;
	private String description;
	private String json;

	public ViewPoint() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLantitude() {
		return lantitude;
	}

	public void setLantitude(double lantitude) {
		this.lantitude = lantitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public String getPanoramic() {
		return panoramic;
	}

	public void setPanoramic(String panoramic) {
		this.panoramic = panoramic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
