/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.api;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Arrays;
import java.util.List;
import models.myExpense;
import models.myMoney;
import play.Logger;
import static play.mvc.Controller.request;
import play.mvc.Result;
import static play.mvc.Results.created;
import static play.mvc.Results.internalServerError;
import static play.mvc.Results.ok;

/**
 *
 * @author JP Rodriguez
 */
public class myExpenses {
    public static Result create(){
        try{
            //Empiezo transaccion
            Ebean.beginTransaction();
            //Obtengo json de la request
            JsonNode requestNode = request().body().asJson();
            //Obtengo expense del json
            myExpense expense = myExpense.fromJson(requestNode);
            expense.setTotalCredit(0D);
            //Creo myExpense
            myExpense.create(expense);
            //Creo todos los myMoney de la lista de myMoney de la expense
            for(myMoney money : expense.getExpenses()){
                money.setExpense(expense);
                myMoney.create(money);
            }
            Ebean.commitTransaction();
            Ebean.endTransaction();
            //devuelvo en formato de json el expense creado
            return created(expense.toJson());
        
        }catch(Exception e){
            Logger.error("Error creando expense", e);
            return internalServerError();
        }
    }
    public static Result get(){
        //Obtengo todos los expenses de la db
        List<myExpense> expenses = myExpense.get();
        //Creo arrayNode de json
        ArrayNode expensesNode = JsonNodeFactory.instance.arrayNode();
        //Agrego cada expense de la db al array
        for(myExpense expense : expenses){
            expensesNode.add(expense.toJson());
        }
        //Devuelvo el array
        return ok(expensesNode);
    }
    public static Result getByMonth(Long month){
        //Obtengo todos los expenses de la db del mes
        List<myExpense> expenses = myExpense.getByMonth(month);
        //Creo arrayNode de json
        ArrayNode expensesNode = JsonNodeFactory.instance.arrayNode();
        //Agrego cada expense de la db al array
        for(myExpense expense : expenses){
            expensesNode.add(expense.toJson());
        }
        //Devuelvo el array
        return ok(expensesNode);
    }
    public static Result delete(Long id){
        try{
            Ebean.beginTransaction();
            myExpense expense= myExpense.getById(id);
            
            if(expense!=null){
                for(myMoney money : expense.getExpenses()){
                    myMoney.delete(money);
                }
                myExpense.delete(expense);
                Ebean.commitTransaction();
            }else{
                Logger.error("Error eliminando money");
            }
            Ebean.endTransaction();
            return ok();
        }catch(Exception e){
            Logger.error("Error eliminando money");
            return internalServerError();
        }
    }
    public static Result createMoney(){
        myMoney money= new myMoney();
        Ebean.beginTransaction();
        try{  
        //Obtengo json de la request
            JsonNode requestNode = request().body().asJson();
            //Creo objectNode para sacarle la parte de expense al json
            ObjectNode object = (ObjectNode) requestNode;
            //Creo un nuevo expense para asignarselo al money
            myExpense expense= new myExpense();
            //Obtengo del nodo la parte de expense del money
            JsonNode expenseIdNode = object.get("expense");
            //Mapeo el string para transformarlo a json node
            ObjectMapper mapper = new ObjectMapper();
            expenseIdNode = mapper.readTree(expenseIdNode.asText());
            //Saco el valor de expense del nodo de la request
            object.remove("expense");
            //Seteo el id del expense que le voy a asignar al money con el id del expense de la request
            expense.setId(expenseIdNode.get("id").asLong());
            
            //Obtengo expense del json
            money = myMoney.fromJson(requestNode);
            //Le cargo la expense al money
            money.setExpense(expense);
            myMoney.create(money);
            myExpense expenseDB= myExpense.getById(expense.getId());
            //Si es de tipo de credito, se suma al total
            if(money.getType().equals("Credito")){
                expenseDB.setTotalCredit(expenseDB.getTotalCredit()+money.getMoneyValue());
            }
            //obtengo valor total de myExpense
            myExpense.calcTotal(expenseDB);
            myExpense.update(expenseDB);
            Ebean.commitTransaction();
            
        }catch(Exception e){
            Logger.error("Error creando money", e);
            return internalServerError();
        }
        finally{
          Ebean.endTransaction();  
        }
        return created(money.toJson());
    }
    public static Result deleteMoney(Long id){
        Ebean.beginTransaction();
        try{
            myMoney money= myMoney.getById(id);
            if(money!=null){
                myExpense expense = myExpense.getById(money.getExpense().getId());
                expense.setTotal(expense.getTotal()-money.getMoneyValue());
            //Si es de tipo de credito, se resta al total
            if(money.getType().equals("Credito")){
                expense.setTotalCredit(expense.getTotalCredit()- money.getMoneyValue());
            }
                myExpense.update(expense);
                myMoney.delete(money);
                Ebean.commitTransaction();
            }else{
                Logger.error("Error eliminando money");
            }
            return ok();
        }catch(Exception e){
            Logger.error("Error eliminando money");
            return internalServerError();
        }finally{
            Ebean.endTransaction(); 
        }
    }
    public static Result myExpenseSummary(Long month){
        
        ArrayNode expenseListNode = JsonNodeFactory.instance.arrayNode();
        try{
        List<myExpense> expenseList = myExpense.getByMonth(month);
        for(myExpense expense : expenseList){
            ObjectNode expenseNode = expense.toJson();
            expenseListNode.add(expenseNode);
        }
        }catch(Exception e){
            Logger.error("Error en funcion myExpenseSummary",e);
        }
        return ok(expenseListNode);
    }
}
