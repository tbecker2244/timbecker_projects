data = "chicken,1,3/22/2021\nturkey,2,3/18/2021\ntomatos,2,4/01/2021\nmilk,7,2/15/2021\nstrawberries,3,3/25/2021\npineapple,3,4/19/2021\nbroccoli,3,3/31/2021";

red = "#cf142b";
orange = "#ffb84d";
green = "#33ff33";


function searchFilterExpired() {
    // Declare variables
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("expiredFoodSearch");
    filter = input.value.toUpperCase();
    table = document.getElementById("expiredFood");
    tr = table.getElementsByTagName("tr");
  
    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName("td")[0];
      if (td) {
        txtValue = td.textContent || td.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = "";
        } else {
          tr[i].style.display = "none";
        }
      }
    }
  }

  function searchFilterExpiring() {
    // Declare variables
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("soonExpireFoodSearch");
    filter = input.value.toUpperCase();
    table = document.getElementById("expireSoonFood");
    tr = table.getElementsByTagName("tr");
  
    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName("td")[0];
      if (td) {
        txtValue = td.textContent || td.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = "";
        } else {
          tr[i].style.display = "none";
        }
      }
    }
  }

function sortExpiredFoods() {
    sortBy = document.getElementById("dropdownfilter").selectedIndex;
    ascdesc = document.getElementById("ascdescexpired").selectedIndex;
    str = "td:first";
    if(sortBy == 1){
        str = "td:nth-child(3)";
    }

    var tbody = $("#expiredFood").find('tbody');
    if(ascdesc == 0){
        newRows = tbody.find('tr').sort(function (a, b) {
            return $(str, a).text().localeCompare($(str, b).text());
        });
    }
    else{
        newRows = tbody.find('tr').sort(function (a, b) {
            return $(str, b).text().localeCompare($(str, a).text());
        });
    }

    tbody.append(newRows);

}
function sortExpiringFoods() {
    sortBy = document.getElementById("dropdownfilterexpiring").selectedIndex;
    ascdesc = document.getElementById("ascdescexpiring").selectedIndex;

    str = "td:first";
    if(sortBy == 1){
        str = "td:nth-child(3)";
    }

    var tbody = $("#expireSoonFood").find('tbody');
    if(ascdesc == 0){
        newRows = tbody.find('tr').sort(function (a, b) {
            return $(str, a).text().localeCompare($(str, b).text());
        });
    }
    else{
        newRows = tbody.find('tr').sort(function (a, b) {
            return $(str, b).text().localeCompare($(str, a).text());
        });
    }

    tbody.append(newRows);

}

function getTodaysDate(){
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = mm + '/' + dd + '/' + yyyy;
    return today;
}

function addExpiredFood(counter, foodItem, quantity, date){
    table = document.getElementById("expiredFood").getElementsByTagName("tbody")[0];
    const date1 = new Date(date);
    const date2 = new Date(getTodaysDate());
    const diff = date2 - date1;
    

    const diffTime = Math.abs(date2 - date1);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    var row = table.insertRow(counter-1);

    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3); 

    cell1.innerHTML = foodItem;
    cell2.innerHTML = quantity;
    cell3.innerHTML = formatDateString(date1);
    if (diffDays == 0){
        cell4.innerHTML = "<span href='#' class='button-expired'>"+"Expired today." + "</span>";
    }
    else if(diffDays == 1){
        cell4.innerHTML = "<span href='#' class='button-expired'>"+"Expired yesterday." + "</span>";
    }
    else
    cell4.innerHTML = "<span href='#' class='button-expired'>"+"Expired " + diffDays + " days ago."+  "</span>";
    
}

function showUnexpiredFoods(){
    isChecked = document.getElementById("showUnexpired").checked;
    if(isChecked){
        var table = document.getElementById("expireSoonFood");
        for (var i = 1, row; row = table.rows[i]; i++) {
            expirDate = new Date(row.cells[2].innerText);
            todayDate = new Date(getTodaysDate());
            
            if(todayDate >= expirDate){
                row.style.display = 'none';
            }
            else{
                row.style.display = '';
            }
            
        }
    document.getElementById("expireSpan").innerHTML = "Showing all unexpired food items in fridge.";  
    sortExpiringFoods();
    }
    else{
        displayAllFoods();
    }
    
}

function formatDateString(MyDate){


    return ('0' + (MyDate.getMonth()+1)).slice(-2)+ '/' + ('0' + MyDate.getDate()).slice(-2) + '/' + MyDate.getFullYear();
}

function addExpiringFood(counter, foodItem, quantity, date){
    table = document.getElementById("expireSoonFood").getElementsByTagName("tbody")[0];
    const date1 = new Date(date);
    const date2 = new Date(getTodaysDate());
    const diff = Math.abs(date1 - date2);
    

    //const diffTime = Math.abs(date2 - date1);
    const diffDays = Math.ceil(diff / (1000 * 60 * 60 * 24));

    var row = table.insertRow(counter-1);

    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3); 
    var cell5 = row.insertCell(4);

    cell1.innerHTML = foodItem;
    cell2.innerHTML = quantity;
    cell3.innerHTML = formatDateString(date1);
    cell5.innerHTML = '<button onclick="deleteRow(this)" class="btn cancel btn-link">Delete Item</button><button type="button" class="btn edit btn-danger" value="Edit Item" onclick="editRow(this)">Edit Item</button>';
    if(diffDays == 0){
        cell4.innerHTML = "<span href='#' class='button-expired'>"+"Expired today."+  "</span>";
    }
    // not expired
    else if(date1 > date2){
        if(diffDays == 1){
            cell4.innerHTML = "<span href='#' class='button-warning'>"+"Expiring tomorrow."+  "</span>";
        }
        else if(diffDays <= 3){
            cell4.innerHTML = "<span href='#' class='button-warning'>"+"Expiring in " + diffDays + " days."+  "</span>";
        }
        else{
            cell4.innerHTML = "<span href='#' class='button-success'>"+"Expiring in " + diffDays + " days."+  "</span>";
        }
    }
    else{
        if(diffDays == 1){
            cell4.innerHTML = "<span href='#' class='button-expired'>"+"Expired yesterday."+  "</span>";
        }
        else{
            cell4.innerHTML = "<span href='#' class='button-expired'>"+"Expired " + diffDays + " days ago."+  "</span>";
        }
    }    
}


function editRow(row){
    rowObj = row.parentNode.parentNode;

    foodItem = rowObj.cells[0].innerText;
    quantity = rowObj.cells[1].innerText;
    expirDate = rowObj.cells[2].innerText;
    
    formatted = new Date(expirDate).toLocaleDateString('fr-CA');
    status = rowObj.cells[3].innerText;

    htmlText = "<td><input type='text' value='"+foodItem+"'></td><td><input type='number' value='"+quantity+"'></td><td><input type='date' value='"+formatted+"'></td><td>"+status+"</td><td><button onclick='deleteRow(this)' class='btn cancel btn-link'>Delete Item</button><button type='button' class='btn save btn-primary' value='Save Item' onclick='saveRow(this)'>Save Item</button></td>";
    rowObj.innerHTML = htmlText;
}

function formatDate(inputDate) {
    var date = new Date(inputDate);
    if (!isNaN(date.getTime())) {
        z = date.getDate() + 1;
        return date.getMonth() + 1 + '/' + z + '/' + date.getFullYear();
    }
}

function changeDateFormat(inputDate){  // expects Y-m-d
    var splitDate = inputDate.split('-');
    if(splitDate.count == 0){
        return null;
    }

    var year = splitDate[0];
    var month = splitDate[1];
    var day = splitDate[2]; 

    return month + '/' + day + '/' + year;
}

function saveRow(button){
    localStorage.setItem("test", "this is the value");
    rowObj = button.parentNode.parentNode;

    foodItem = rowObj.cells[0].firstChild.value;
    quantity = rowObj.cells[1].firstChild.value;
    if(rowObj.cells[2].firstChild.value.length == 0 || foodItem.length == 0 || quantity.length == 0){
        alert("Please enter item name, quantity, and expiration date");
        return;
    }

    expirDate = changeDateFormat(rowObj.cells[2].firstChild.value);

    const date1 = new Date(expirDate);
    const date2 = new Date(getTodaysDate());
    const diff = Math.abs(date1 - date2);
    

    //const diffTime = Math.abs(date2 - date1);
    const diffDays = Math.ceil(diff / (1000 * 60 * 60 * 24));
    status = "";
    color = "";
    if(diffDays == 0){
        status = "<span href='#' class='button-expired'>"+"Expired today."+  "</span>";
    }
    // not expired
    else if(date1 > date2){
        if(diffDays == 1){
            status = "<span href='#' class='button-warning'>"+"Expiring tomorrow."+  "</span>";
        }
        else if(diffDays <= 3){
            status = "<span href='#' class='button-warning'>"+"Expiring in " + diffDays + " days."+  "</span>";
        }
        else{
            status = "<span href='#' class='button-success'>"+"Expiring in " + diffDays + " days."+  "</span>";
        }
    }
    else{
        if(diffDays == 1){
            status = "<span href='#' class='button-expired'>"+"Expired yesterday."+  "</span>";
        }
        else{
            status = "<span href='#' class='button-expired'>"+"Expired " + diffDays + " days ago."+  "</span>";
        }
    }     
    htmlText = "<td>"+foodItem+"</td><td>"+quantity+"</td><td>"+expirDate+"</td><td>"+status+"</td><td>"+'<button onclick="deleteRow(this)" class="btn cancel btn-link">Delete Item</button><button type="button" class="btn edit btn-danger" value="Edit Item" onclick="editRow(this)">Edit Item</button>';

    if(quantity == 0){
        confirm("Add " + foodItem + " to next week's grocery list?");
        z = rowObj;
        z.parentNode.removeChild(z);
    }
    else{
        rowObj.innerHTML = htmlText;
    }
    
    updateExpiredFoods();
    sortExpiringFoods();
    updateInventory();
}

function addFood(){
    table = document.getElementById("expireSoonFood").getElementsByTagName("tbody")[0];
    row = table.insertRow(0);
    row.innerHTML = "<td><input type='text'></td><td><input type='number'></td><td><input type='date'></td><td></td><td><button onclick='deleteRow(this)' class='btn cancel btn-link'>Delete Item</button><button type='button' class='btn save btn-primary' value='Save Item' onclick='saveRow(this)'>Save Item</button></td>";
    //sortExpiringFoods();
}

function deleteRowEdit(o){
    z = o.parentNode;
    z.parentNode.removeChild(z);
    updateExpiredFoods();
    updateInventory();
}

function deleteRow(o){
    var p=o.parentNode.parentNode;
    p.parentNode.removeChild(p);
    updateExpiredFoods();
    updateInventory();
}



function loadData(){
    if(localStorage.getItem("inventorylist") == null){
        return
    }
    
    var lines = localStorage.getItem("inventorylist").split('\n');

    counter = 1;
    expiringSoonCounter = 1;
    for (var i = 0; i < lines.length; i++) {
        if(lines[i].trim().length == 0){
            continue;
        }
        console.log(lines[i]);
        var cols = lines[i].split(',');

        foodItem = cols[0];
        quantity = cols[1];
        date = cols[2];

        const date1 = new Date(date);
        const date2 = new Date(getTodaysDate());
        const diff = date2 - date1;
        const diffTime = Math.abs(date2 - date1);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        if(diff >= 0){
            addExpiredFood(counter, foodItem, quantity, date)
            counter = counter + 1;
        }
        
        // else if(diffDays <= 3){
            addExpiringFood(expiringSoonCounter, foodItem, quantity, date);
            expiringSoonCounter = expiringSoonCounter + 1; 
        //}
    }

    sortExpiredFoods();
    sortExpiringFoods();
}

function displayAllFoods(){
    var table = document.getElementById("expireSoonFood");
    for (var i = 1, row; row = table.rows[i]; i++) {
        row.style.display = ''; 
    }
    document.getElementById("expireSpan").innerHTML = "Showing all food items in fridge.";
    sortExpiringFoods();
}

function updateExpiredFoods(){
    numRows = document.getElementById("expiredFood").rows.length;
    for(var i = 1; i < numRows; i++){
        document.getElementById("expiredFood").deleteRow(-1);
    }
    var table = document.getElementById("expireSoonFood");
    counter = 1;
    for (var i = 1, row; row = table.rows[i]; i++) {
        expirDate = new Date(row.cells[2].innerText);
        todayDate = new Date(getTodaysDate());
        const diff = expirDate - todayDate;

        const diffDays = Math.ceil(diff / (1000 * 60 * 60 * 24));
        
        if(diffDays <= 0){
            addExpiredFood(counter, row.cells[0].innerText, row.cells[1].innerText, row.cells[2].innerText);
            counter = counter + 1;
        }
    }

    sortExpiredFoods();
}

function updateInventory(){
    var table = document.getElementById("expireSoonFood");
    inventory = "";
    for (var i = 1, row; row = table.rows[i]; i++) {
        expirDate = row.cells[2].innerText;
        foodItem = row.cells[0].innerText;
        quantity = row.cells[1].innerText;
        inventory += foodItem + "," + quantity + "," + expirDate + "\n";
    }

    console.log(inventory);
    localStorage.setItem("inventorylist", inventory);
}

function filterExpiringDays(){

    days = document.getElementById("expireInDays").value
    if(days.length == 0){
        document.getElementById("expireSpan").innerHTML = "Showing all foods in fridge"
        showUnexpiredFoods();
        return;
    }
    else if(days < 0){
        alert("Please input a positive value");
        document.getElementById("expireSpan").innerHTML = "Showing all foods in fridge"
        showUnexpiredFoods();
        return;
    }
    else{
        if(days > 1){
            document.getElementById("expireSpan").innerHTML = "Showing food expiring in " + days + " days"
        }
        else{
            document.getElementById("expireSpan").innerHTML = "Showing food expiring in " + days + " day"
        }
    }
    var table = document.getElementById("expireSoonFood");
    for (var i = 1, row; row = table.rows[i]; i++) {
        expirDate = new Date(row.cells[2].innerText);
        todayDate = new Date(getTodaysDate());
        const diff = expirDate - todayDate;

        const diffDays = Math.ceil(diff / (1000 * 60 * 60 * 24));
        
        if(diffDays <= 0 || diffDays > days){
            row.style.display = 'none';
        }
        else{
            row.style.display = '';
        }
        
    }    
    sortExpiringFoods();

}
