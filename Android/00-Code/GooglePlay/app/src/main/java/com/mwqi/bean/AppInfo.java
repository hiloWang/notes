package com.mwqi.bean;

import java.util.List;

/**
 * Created by mwqi on 2014/6/7.
 */
public class AppInfo {
	private long id;//app的id
	private String name;//app的软件名称
	private String packageName;//app的包名
	private String iconUrl;//app的icon地址
	private float stars;//app的评价星级
	private String downloadNum;//app的下载数量
	private String version;//app的版本
	private String date;//app的发布日期
	private long size;//app的size
	private String downloadUrl;//下载地址
	private String des;//简介
	private String author; //作者
	private List<String> screen;//截图下载地址
	private List<String> safeUrl;//安全信息图表地址
	private List<String> safeDesUrl;//安全信息图表地址
	private List<String> safeDes;//安全信息图表地址
	private List<Integer> safeDesColor;//安全信息的文字颜色

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public float getStars() {
		return stars;
	}

	public void setStars(float stars) {
		this.stars = stars;
	}

	public String getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<String> getScreen() {
		return screen;
	}

	public void setScreen(List<String> screen) {
		this.screen = screen;
	}

	public List<String> getSafeUrl() {
		return safeUrl;
	}

	public void setSafeUrl(List<String> safeUrl) {
		this.safeUrl = safeUrl;
	}

	public List<String> getSafeDesUrl() {
		return safeDesUrl;
	}

	public void setSafeDesUrl(List<String> safeDesUrl) {
		this.safeDesUrl = safeDesUrl;
	}

	public List<String> getSafeDes() {
		return safeDes;
	}

	public void setSafeDes(List<String> safeDes) {
		this.safeDes = safeDes;
	}

	public List<Integer> getSafeDesColor() {
		return safeDesColor;
	}

	public void setSafeDesColor(List<Integer> safeDesColor) {
		this.safeDesColor = safeDesColor;
	}
}
