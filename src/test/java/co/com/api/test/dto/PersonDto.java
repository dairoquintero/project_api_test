package co.com.api.test.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonDto {

    private String name;
    private String age;
    private String city;

}
