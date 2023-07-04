package com.example.restapi.file.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class GetFileRes {
    private String file_name;
    private String file_type;
    private String file_location;
    private String file_regdate;

}
