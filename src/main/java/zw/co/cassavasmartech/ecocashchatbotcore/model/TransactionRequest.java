package zw.co.cassavasmartech.ecocashchatbotcore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import java.io.Serializable;

@JsonPropertyOrder({
        "field1",
        "field2",
        "field3",
        "field4",
        "field5",
        "field6",
        "field7",
        "field8",
        "field9",
        "field10",
        "field11",
        "field12",
        "field13",
        "field14",
        "field15",
        "field16",
        "field17",
        "field18",
        "field19",
        "field20",
        "field21",
        "field22",
        "field23"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("transactionRequest")
public class TransactionRequest implements Serializable {
   @JsonProperty("field1")
   public String field1;
   @JsonProperty("field2")
   public String field2;
   @JsonProperty("field3")
   public String field3;
   @JsonProperty("field4")
   public String field4;
   @JsonProperty("field5")
   public String field5;
   @JsonProperty("field6")
   public String field6;
   @JsonProperty("field7")
   public String field7;
   @JsonProperty("field8")
   public String field8;
   @JsonProperty("field9")
   public String field9;
   @JsonProperty("field10")
   public String field10;
   @JsonProperty("field11")
   public String field11;
   @JsonProperty("field12")
   public String field12;
   @JsonProperty("field13")
   public String field13;
   @JsonProperty("field14")
   public String field14;
   @JsonProperty("field15")
   public String field15;
   @JsonProperty("field16")
   public String field16;
   @JsonProperty("field17")
   public String field17;
   @JsonProperty("field18")
   public String field18;
   @JsonProperty("field19")
   public String field19;
   @JsonProperty("field20")
   public String field20;
   @JsonProperty("field21")
   public String field21;
   @JsonProperty("field22")
   public String field22;
   @JsonProperty("field23")
   public String field23;
   private static final long serialVersionUID = -3734546037674257624L;
}

