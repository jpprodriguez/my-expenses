/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import play.db.ebean.Model;
import play.libs.Json;

/**
 *
 * @author JP Rodriguez
 */
@Entity
public class myMoney extends Model{
    @Id
    private Long id;
    private String concept;
    private String type;
    private int moneyDate;
    private Double moneyValue;
    @ManyToOne
    @JoinColumn(name = "MY_EXPENSE_ID")
    private myExpense expense;
 
    private static Model.Finder<Long, myMoney> find = new Model.Finder<>(Long.class, myMoney.class);
    
    public myExpense getExpense() {
        return expense;
    }

    public void setExpense(myExpense expense) {
        this.expense = expense;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(Double moneyValue) {
        this.moneyValue = moneyValue;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }
    public int getMoneyDate() {
        return moneyDate;
    }

    public void setMoneyDate(int date) {
        this.moneyDate = date;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public static void create(myMoney money)throws Exception{
        money.save();
    }
    
    public static void update(myMoney money) throws Exception{
        money.update();
    }
    
     public static void delete(myMoney money)throws Exception{
        money.delete();
    }
    public static myMoney getById(Long id){
        return(find.byId(id));
    } 
     public ObjectNode toJson(){
            ObjectNode node = JsonNodeFactory.instance.objectNode();
            ObjectNode expenseNode = JsonNodeFactory.instance.objectNode();
            expenseNode.put("id", this.expense.getId());
            node.put("moneyValue", this.moneyValue);
            node.put("type", this.type);
            node.put("concept", this.concept);
            node.put("moneyDate",this.moneyDate);
            node.put("expense",expenseNode);
            node.put("id", this.id);
            return node;
     }
    public static myMoney fromJson(JsonNode myMoneyNode)
        throws Exception{
        
        return Json.fromJson(myMoneyNode, myMoney.class);
    }
}
