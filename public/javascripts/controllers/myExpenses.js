/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

myApp.controller('myExpensesCtrl', function($scope,$http,$mdToast) {
    $scope.months = [1,2,3,4,5,6,7,8,9,10,11,12];
    $scope.activeMonth;
    $scope.myExpenseList = [];
    $scope.expenseListByMonth = [];
    $scope.expense;
    $scope.money;
    $scope.myDate = {
         value: new Date()
       };
    $scope.dateString="";
    $scope.monthTotal=0;
    $scope.monthCreditTotal=0;

      
    $scope.onInit = function() {
        var d = new Date();
        var currentMonth = d.getMonth() + 1;
        $scope.onMonthClicked(currentMonth);
    };

    $scope.onMonthClicked = function(month){
        $scope.activeMonth = month;
        $scope.fillmyExpenseList(month);
    };

    $scope.showToast = function(msg,success) {
        var toastClass = success ? "success-toast" : "error-toast";
        $mdToast.show(
          $mdToast.simple()
            .textContent(msg)
            .hideDelay(3000)
            .theme(toastClass)
        );
    };

    $scope.fillmyExpenseList = function(month) {
        $scope.myExpenseList = [];
        $http.get('/api/expenses/' + month).
        success(function (data) {
            var expenseList = data;           
            expenseList.forEach(function(expense) {
                    $scope.myExpenseList.push(expense);          
            });                             
        })
        .error(function (){
            console.log("Error al obtener gastos");
        });
    };  

    $scope.fillExpenseListByMonth = function(month) {
        $scope.expenseListByMonth = [];
        $http.get('/api/expenses/' + month).
        success(function (data) {
            var expenseList = data;           
            expenseList.forEach(function(expense) {
                    $scope.expenseListByMonth.push(expense);          
            });                             
        })
        .error(function (){
            console.log("Error al obtener gastos");
        });
    };        

    $scope.update = function(){
        $scope.myExpenseList =[];
          $http.get('/api/expenses').
            success(function (data) {
                var myExpenseCompleteList = data;
                
                myExpenseCompleteList.forEach(function(expense) {
                        $scope.myExpenseList.push(expense);     
            });
            })
            .error(function (){
                console.log("Error al obtener gastos");
            }); 
    };
    $scope.date = function(date){

        var string ="";
        if(day<10){
            string += "0";
            var day = parseInt(date/100000);
            var month = parseInt(date/1000);
            month = parseInt(month%10);
            var year = date%1000;
        }else{
        var day = parseInt(date/1000000);

        var month = parseInt(date/10000);

        month = parseInt(month%100);

        var year = date%10000;
        
        string += day;
        
        }
        string += "/";
        if(month<10){
            string += "0";
        }
        string += month;
        string += "/";
        string += year;

        return string;
    };
    $scope.createCategory = function () {
        $http.post('/api/expenses', $scope.expense)
                .success(function(data){
                    expense=data;
                    $scope.myExpenseList.push(expense);
                    $scope.showToast("Categoria creada exitosamente.",true);
 
                })
                .error(function(){
                    $scope.showToast("Categoria no pudo ser creada.",false);
                    console.log("Error al crear nuevo gasto");
                });
    };
    $scope.createExpense = function () {
        
        $scope.dateString=$scope.myDate.value.getDate().toString();
        
        if($scope.myDate.value.getMonth()<10){
            $scope.dateString += "0";
            $scope.dateString+=$scope.myDate.value.getMonth().toString();
        }else{
            $scope.dateString+=$scope.myDate.value.getMonth().toString();
        }
        
        $scope.dateString+=$scope.myDate.value.getFullYear().toString();
        
        var number= parseInt($scope.dateString);
        $scope.money.moneyDate=number;
        $http.post('/api/money', $scope.money)
                .success(function(data){
                    $scope.update();
                    $scope.showToast("Gasto creado exitosamente.",true);
                })
                .error(function(){
                    $scope.showToast("Gasto no pudo ser creado.",false);
                    console.log("Error al crear nuevo gasto");
                });
    };
    
     $scope.deleteMoney = function(moneyId) {
        $http.delete("/api/money/" + moneyId)
                .success(function(data){
                    $scope.update();
                })
                .error(function(){
                    console.log("Error al eliminar categoria");
                });
    };
    
    $scope.deleteExpense = function(expenseId) {
        $http.delete("/api/expenses/" + expenseId)
                .success(function(data){
                    $scope.myExpenseList = $scope.myExpenseList.filter(function (filter) {
                        return filter.id !== expenseId;
                       });
                })
                .error(function(){
                    console.log("Error al eliminar categoria");
                });
    };

    $scope.getExpenseSummary = function(month){
        $http.get('/api/expensesummary/' + month).
            success(function (data) {
                $scope.monthTotal =0;
                $scope.monthCreditTotal= 0;
                var myExpenseSummaryList = data;
                if(myExpenseSummaryList.length >0){
                    myExpenseSummaryList.forEach(function(expense) {
                            $scope.monthTotal+= expense.total;
                            if(expense.totalCredit>0){
                                $scope.monthCreditTotal+= expense.totalCredit;
                            }
                    });
                }else{
                    $scope.monthTotal=0;
                }
            })
            .error(function(){
                    console.log("Error al obtener expenseSummary");
                });
            };
    $scope.monthToString = function(month){
        var string;
        switch(month){
            case 1:
                string= "Enero";
                break;
            case 2:
                string= "Febrero";
                break;
            case 3:
                string= "Marzo";
                break;
            case 4:
                string= "Abril";
                break;
            case 5:
                string= "Mayo";
                break;
            case 6:
                string= "Junio";
                break;
            case 7:
                string= "Julio";
                break;
            case 8:
                string= "Agosto";
                break;
            case 9:
                string= "Septiembre";
                break;
            case 10:
                string= "Octubre";
                break;
            case 11:
                string= "Noviembre";
                break;
            case 12:
                string= "Diciembre";
                break;
        }
        return string;
    };


});

