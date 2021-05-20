package co.com.api.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepoDto {
  private String id;
  private String name;
  private String full_Name;
  private String description;
  private String svn_url;
  private String url;
  private String default_branch;

}

