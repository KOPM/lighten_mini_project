package com.tencent.kopm.model;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private Integer uid;
	private String username;
	private String password;
	private Integer score;

	// Constructors

	/** default constructor */
	public User() {
	}
    /** no uid**/
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	/** minimal constructor */
	public User(Integer uid, String username, String password) {
		this.uid = uid;
		this.username = username;
		this.password = password;
	}

	/** full constructor */
	public User(Integer uid, String username, String password, Integer score) {
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.score = score;
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

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}