red = "#cf142b";
orange = "#ffb84d";
green = "#33ff33";


  function searchFilterExpiring() {
    // Declare variables
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("soonExpireFoodSearch");
    filter = input.value.toUpperCase();
    table = document.getElementById("foodList");
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

function sortExpiringFoods() {
    ascdesc = document.getElementById("ascdescexpiring").selectedIndex;

    str = "td:first";

    var tbody = $("#foodList").find('tbody');
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


function editRow(row){
    rowObj = row.parentNode.parentNode;

    foodItem = rowObj.cells[0].innerText;
    quantity = rowObj.cells[1].innerText;

    htmlText = "<td><input type='text' value='"+foodItem+"'></td><td><input type='number' value='"+quantity+"'></td><td><button onclick='deleteRow(this)' class='btn cancel btn-link'>Delete Item</button><button type='button' class='btn save btn-primary' value='Save Item' onclick='saveRow(this)'>Save Item</button></td>";
    rowObj.innerHTML = htmlText;
}



function saveRow(button){
    localStorage.setItem("test", "this is the value");
    rowObj = button.parentNode.parentNode;

    foodItem = rowObj.cells[0].firstChild.value;
    quantity = rowObj.cells[1].firstChild.value;

    htmlText = "<td align='center'>"+foodItem+"</td><td align='center'>"+quantity+"</td><td align='center'>"+'<button onclick="deleteRow(this)" class="btn cancel btn-link">Delete Item</button><button type="button" class="btn edit btn-danger" value="Edit Item" onclick="editRow(this)">Edit Item</button>'+"<td align='center'>"+'<input type="checkbox"></td>';
    rowObj.innerHTML = htmlText;

    updateInventory();
}

function addFood(){
    table = document.getElementById("foodList").getElementsByTagName("tbody")[0];
    row = table.insertRow(0);
    row.innerHTML = "<td><input type='text'></td><td><input type='number'></td><td><button onclick='deleteRow(this)' class='btn cancel btn-link'>Delete Item</button><button type='button' class='btn save btn-primary' value='Save Item' onclick='saveRow(this)'>Save Item</button></td>";
    //sortExpiringFoods();
}

function deleteRowEdit(o){
    z = o.parentNode;
    z.parentNode.removeChild(z);
    updateInventory();
}

function deleteRow(o){
    var p=o.parentNode.parentNode;
    p.parentNode.removeChild(p);
    updateInventory();
}



function loadData(){
    if(localStorage.getItem("groceryList") == null){
        return
    }
    
    var lines = localStorage.getItem("groceryList").split('\n');
    table = document.getElementById("foodList").getElementsByTagName("tbody")[0];
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
        row = table.insertRow(0);
        htmlText = "<td align='center'>"+foodItem+"</td><td align='center'>"+quantity+"</td><td align='center'>"+'<button onclick="deleteRow(this)" class="btn cancel btn-link">Delete Item</button><button type="button" class="btn edit btn-danger" value="Edit Item" onclick="editRow(this)">Edit Item</button>'+"<td align='center'>"+'<input type="checkbox"></td>';
        row.innerHTML = htmlText;
    }

    sortExpiringFoods();
}

function displayAllFoods(){
    var table = document.getElementById("foodList");
    for (var i = 1, row; row = table.rows[i]; i++) {
        row.style.display = ''; 
    }
    document.getElementById("expireSpan").innerHTML = "Showing all the items for your next trip!";
    sortExpiringFoods();
}

function updateInventory(){
    var table = document.getElementById("foodList");
    inventory = "";
    for (var i = 1, row; row = table.rows[i]; i++) {
        foodItem = row.cells[0].innerText;
        quantity = row.cells[1].innerText;
        inventory += foodItem + "," + quantity + "\n";
    }

    console.log(inventory);
    localStorage.setItem("groceryList", inventory);
}

function clearList(){
    $("#foodList > tbody").empty();
    localStorage.clear();
}