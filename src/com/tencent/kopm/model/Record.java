package com.tencent.kopm.model;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class Record implements java.io.Serializable {

	// Fields

	private Integer uid;
	private String username;
	private Integer score;
	private Integer rank;

	// Constructors

	/** default constructor */
	public Record() {
	}

	public Record(Integer uid, String username, Integer score, Integer rank) {
		this.uid = uid;
		this.username = username;
		this.score = score;
		this.rank = rank;
	}

	// Property accessors

	public Integer getUid() {
		return this.uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

}