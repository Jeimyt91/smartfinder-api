package com.tuempresa.smartfinder.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DispositivoDTO {
  public UUID id;
  public String name;
  public UUID brandId;
  public String brandName;
  public String type;
  public LocalDate releaseDate;
  public BigDecimal price;
  public String shortDesc;
  public String review;
  public List<String> images = new ArrayList<>();
  public Map<String,String> specs = new LinkedHashMap<>();
}
