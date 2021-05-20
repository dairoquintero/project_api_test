package co.com.api.test.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepoListDto {
  private String name;
  private String path;
  private String sha;

  @JsonCreator
  public GithubRepoListDto(@JsonProperty("name") String name,
                           @JsonProperty("path") String path, @JsonProperty("sha") String sha) {
    this.name = name;
    this.path = path;
    this.sha = sha;
  }
}


