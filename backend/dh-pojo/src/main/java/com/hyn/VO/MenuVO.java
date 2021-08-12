package com.hyn.VO;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class MenuVO {
    String key;
    String title;
    String pKey;
    List<MenuVO> children;
}
