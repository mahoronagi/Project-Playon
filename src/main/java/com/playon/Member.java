package com.playon;

public class Member {
	public long id;
	public String user;
        public long role;
	public String getName() { return user; }
	public String getFullName() { return "Mr " + user; }
        public long getRole() { return role; }
}
