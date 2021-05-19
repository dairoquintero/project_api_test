package co.com.api.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubDto {
  String id;
  String name;
  String full_Name;
  String description;
  String svn_url;
  String default_Branch;

}
