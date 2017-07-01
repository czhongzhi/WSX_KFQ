package com.inetgoes.fangdd.opencity.bean;

import java.util.List;

public class City {
	private String id;
	private String name;
	private List<District> districts;

	public City() {
		// TODO Auto-generated constructor stub
	}

	public City(String id, String name, List<District> districts) {
		super();
		this.id = id;
		this.name = name;
		this.districts = districts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}

	@Override
	public String toString() {
		return name;
	}
	
	

}
