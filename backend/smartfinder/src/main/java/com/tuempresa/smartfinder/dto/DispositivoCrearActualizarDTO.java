package com.tuempresa.smartfinder.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DispositivoCrearActualizarDTO {
  public String name;
  public String type;
  public UUID brandId;
  public LocalDate releaseDate;
  public BigDecimal price;
  public String shortDesc;
  public String review;
  public List<String> images = new ArrayList<>();
  public Map<String,String> specs = new LinkedHashMap<>();
}
