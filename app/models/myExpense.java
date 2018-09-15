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
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import play.Logger;
import play.db.ebean.Model;
import play.libs.Json;

/**
 *
 * @author JP Rodriguez
 */
@Entity
public class myExpense extends Model{
    @Id
    private Long id;
    @ManyToOne
    private myUser user;
    private String name;
    @OneToMany
    private List<myMoney> expenses;
    private Double total;
    private Long expenseMonth;
    private Double totalCredit;

   private static Model.Finder<Long, myExpense> find = new Model.Finder<>(Long.class, myExpense.class);
   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<myMoney> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<myMoney> expenses) {
        this.expenses = expenses;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
    public Long getExpenseMonth() {
        return expenseMonth;
    }

    public void setExpenseMonth(Long month) {
        this.expenseMonth = month;
    }
        public Double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public myUser getUser() {
        return user;
    }

    public void setUser(myUser user) {
        this.user = user;
    }

    
    public static void create(myExpense expense)throws Exception{
        expense.save();
    }
    
    public static void update(myExpense expense) throws Exception{
        expense.update();
    }
    
     public static void delete(myExpense expense)throws Exception{
        expense.delete();
    }
     
    public static void calcTotal(myExpense expense){
        double total = 0;
        for(myMoney money : expense.getExpenses()){
            total+=money.getMoneyValue();
        }
        expense.setTotal(total);
    }
    
    public ObjectNode toJson(){
            ObjectNode node = JsonNodeFactory.instance.objectNode();
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
           for(myMoney money : this.expenses){
                arrayNode.add(money.toJson());
            }
            node.put("expenses", arrayNode);
            node.put("id",this.id);
            node.put("name", this.name);
            node.put("total",this.total);
            node.put("expenseMonth",this.expenseMonth);
            node.put("totalCredit",this.totalCredit);
            return node;
     }
    public static List<myExpense> get(){
        try{
            return(find.findList());
            
        }catch(Exception e){
            Logger.error("Error obteniendo gastos");
            return null;
        }
    }
    public static myExpense getById(Long id){
        return(find.byId(id));
    }
    public static List<myExpense> getByMonth(Long month){
        return(find.where().eq("expenseMonth",month).findList());
    }
    public static myExpense fromJson(JsonNode myExpenseNode)
        throws Exception{
        
        return Json.fromJson(myExpenseNode, myExpense.class);
    }
}
