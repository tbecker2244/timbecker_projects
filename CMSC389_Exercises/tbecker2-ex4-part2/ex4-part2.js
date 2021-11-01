// =========================================
// DO NOT MODIFY This code block
const url =
  "http://api.nobelprize.org/v1/prize.json";

const winners = [];
console.log("Starting fetch");
fetch(url)
  .then(resp => resp.json())
  .then(data => winners.push(...data.prizes));
// =========================================

// The following line displays the winner data in console for you.

console.log(winners);
var sheet = document.createElement('style')

/*
Your task is to complete the functions described below according to the
description given in comments.
*/

/*
(1) Display the list of 2021 winners. 

Output: An array of strings where each contains the category and the winner
names in the following format:

<category> : <firstname 1> <surname 1> / <firstname 2> <surname 2> / ...

- The first letter of the category should be capitalized. 
- If surname does not exist, use just firstname.
- If there is no winner for a category, use 'Not Awarded' as the winner name.
- The output should be sorted by category in ascending order
*/
function winner2021() {

  const reducer = (previousValue, currentValue) => previousValue + " / " + currentValue;

  var winner2021_result = winners.filter(winner => (winner.year == "2021")).map(({category, laureates}) => ({category, laureates})).sort();
  var update = winner2021_result.map( x => x.category + " : " + x.laureates.map(y => y.firstname + " " + y.surname).reduce(reducer));
  var capitalize = update.map(x => x.charAt(0).toUpperCase() + x.slice(1))
  

  document.innerHTML = convertToHTML(capitalize.sort())

}

/*
(2) Display the list of all Nobel peace winners, sorted by year, in an ascending
order.

Output: An array of strings where each contains the year and the winner names in
the following format:

<year> : <firstname 1> <surname 1> / <firstname 2> <surname 2> / ...

- If surname does not exist, use just firstname.
- If there is no winner for a year, use 'Not Awarded' as the winner name part
- 'Not Awarded' must be displayed in bold
- 2021 winner(s) must be displayed in blue, bold font.
- The output should be sorted by award year in ascending order
*/
function peaceWinners() {

// FUNCTION BODY HERE

  const reducer = (previousValue, currentValue) => previousValue + " / " + currentValue;

  var order = winners.filter(winner => winner.category == "peace").reverse().map(({year, laureates}) => ({year, laureates}));
  var laurs = order.map(({laureates}) => laureates !== undefined ? laureates.map(y => (y.surname != undefined) ? (y.firstname + " " + y.surname) : y.firstname).reduce(reducer) : "Not Awarded".bold())
  var final = order.map((element, index) => element.year + " : " + laurs[index])

  sheet.innerHTML = "li:last-child {color:blue; font-weight:bold;}";
  document.body.appendChild(sheet)

  document.innerHTML = convertToHTML(final)
  
}

/*
(3) Display the total number of all Nobel Chemistry winners and also display the
counts of the years in which only one laureate was awarded, two laureates were
awarded, and three laureates were awarded. 

Output: Just an integer, followed by the 1-laureate, 2-laureate, and
3-laureate count)

For example, if only one laureates for 60 years, two laureates for 23 year, and
three laureates for 28 years, then, the output should be: 

190 (60, 23, 28)

- Consider a group as a winner.
- Display the output as underlined and 3 times bigger than the font of the body
  font size.
- Note that the prize was not awarded for some years. 
*/
function countChemistryWinners() {

// FUNCTION BODY HERE
 
  const reducer = (previousValue, currentValue) => previousValue + currentValue;
  var chemistry_count = winners.filter(winner => winner.category == "chemistry").filter(x => x.laureates !== undefined).map(({laureates}) => (laureates.length));
  var total_count = chemistry_count.reduce(reducer);
  var one_winner = chemistry_count.filter(x => x == 1).length;
  var two_winners = chemistry_count.filter(x => x == 2).length;
  var three_winners = chemistry_count.filter(x => x == 3).length;

  var variable = total_count + " (" + one_winner + ", " + two_winners + ", " + three_winners + ")"
  const final = [];
  final.push(variable)
  
  document.body.removeChild(sheet)
  laureateList.style.textDecoration = "underline";
  laureateList.style.fontSize = "3em"
  laureateList.style.color = "black";
  
  document.innerHTML = convertToHTML(final)

}

/* 
(4) Display the names of the winner(s) who won the prize 2 or more times

Output: An array of strings where each contains the winner name, category, and
year in the following format:

<firstname 1> <surname 1> (<category> <year>)

Note that same id is used in the data for each distinct laureate
*/
function multiTimeWinner() {

  names = []
  repeated_names = []
  answer = []
  var laurs = winners.map(({laureates}) => laureates !== undefined ? laureates.map(y => (y.surname != undefined) ? (y.firstname + " " + y.surname) : y.firstname): []);
  laurs.map(y => y.map(x => names.push(x)))
  names.map((element, index) => names.includes(element, index+1) ? repeated_names.push(element) : element)
  const uniqueSet = new Set(repeated_names);
  repeated_names = [...uniqueSet];
  var mapped_laurs = winners.map(({laureates, category, year}) => ({laureates, category, year}))
  var total_names = mapped_laurs.map(x => x.laureates !== undefined ? x.laureates.map(y => (y.surname != undefined) ? (y.firstname + " " + y.surname) : y.firstname): [])
  total_names.map((x, index) => x.map(y => repeated_names.includes(y) ? answer.push(y + " (" + mapped_laurs[index].category + ", "+ mapped_laurs[index].year + ")") : "")).sort()
  
  laureateList.style.textDecoration = "none";
  laureateList.style.fontSize = "1em";
  document.innerHTML = convertToHTML(answer.sort())

}


// Utility function that takes an array of strings and converts into arrays into
// separate HTML list elements. (Hint: Use this to generate a series of list
// items from an array.)
function convertToHTML(query) {
  const results = query.map(e => `<li>${e}</li>`).join("");
  laureateList.innerHTML = results;
}

// A javascript reference to the unordered list with classname list. 
// (Hint: Use this!)
const laureateList = document.querySelector(".list");
