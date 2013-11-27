package model;

public class Descriptor {
	private String name;
	private int duration;
	private String description;
	private int hardness = 0;
	
	public Descriptor(String name, int duration, String desc, int hardness) {
		this.name = name;
		this.duration = duration;
		this.description = desc;
		this.hardness = hardness;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getHardness() {
		return hardness;
	}
	public void setHardness(int hardness) {
		this.hardness = hardness;
	}
	
}
