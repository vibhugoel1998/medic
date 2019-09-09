package com.example.john.medic;

import java.util.List;

public class ResultsItem{

	public static final String NAME="name";
	public static final String FORMATTEDADDRESS="formatted_address";
	public static final String GEOMETRY="geometry";

	private String reference;
	private String formattedAddress;
	private List<String> types;
	private String icon;
	private String name;
	private OpeningHours openingHours;
	private int rating;
	private Geometry geometry;
	private String id;
	private List<PhotosItem> photos;
	private String placeId;



	public void setReference(String reference){
		this.reference = reference;
	}

	public String getReference(){
		return reference;
	}

	public void setFormattedAddress(String formattedAddress){
		this.formattedAddress = formattedAddress;
	}

	public String getFormattedAddress(){
		return formattedAddress;
	}

	public void setTypes(List<String> types){
		this.types = types;
	}

	public List<String> getTypes(){
		return types;
	}

	public void setIcon(String icon){
		this.icon = icon;
	}

	public String getIcon(){
		return icon;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setOpeningHours(OpeningHours openingHours){
		this.openingHours = openingHours;
	}

	public OpeningHours getOpeningHours(){
		return openingHours;
	}

	public void setRating(int rating){
		this.rating = rating;
	}

	public int getRating(){
		return rating;
	}

	public void setGeometry(Geometry geometry){
		this.geometry = geometry;
	}

	public Geometry getGeometry(){
		return geometry;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setPhotos(List<PhotosItem> photos){
		this.photos = photos;
	}

	public List<PhotosItem> getPhotos(){
		return photos;
	}

	public void setPlaceId(String placeId){
		this.placeId = placeId;
	}

	public String getPlaceId(){
		return placeId;
	}

	@Override
 	public String toString(){
		return 
			"ResultsItem{" + 
			"reference = '" + reference + '\'' + 
			",formatted_address = '" + formattedAddress + '\'' + 
			",types = '" + types + '\'' + 
			",icon = '" + icon + '\'' + 
			",name = '" + name + '\'' + 
			",opening_hours = '" + openingHours + '\'' + 
			",rating = '" + rating + '\'' + 
			",geometry = '" + geometry + '\'' + 
			",id = '" + id + '\'' + 
			",photos = '" + photos + '\'' + 
			",place_id = '" + placeId + '\'' + 
			"}";
		}
}