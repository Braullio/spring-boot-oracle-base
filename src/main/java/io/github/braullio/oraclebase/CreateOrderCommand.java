package io.github.braullio.oraclebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderCommand{
    private Long id;
    private String item;
    private String acao;
}