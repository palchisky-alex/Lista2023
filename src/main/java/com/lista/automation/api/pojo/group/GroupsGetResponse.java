package com.lista.automation.api.pojo.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class GroupsGetResponse {

	@JsonProperty("amount")
	private int amount;

	@JsonProperty("image_path")
	private String imagePath;

	@JsonProperty("name")
	private String name;

	@JsonProperty("is_automatic")
	private boolean isAutomatic;

	@JsonProperty("id")
	private int id;


}