let current_guess = [];
let current_row = 0;
let cell_colours = ["rgb(85, 85, 85)", "rgb(106, 170, 100)", "rgb(201, 180, 88)"];
console.log("Script Loaded");

window.onload = () => {

  // add click listeners to grid cells
  let cells = document.getElementsByClassName("cell");
  [].forEach.call(cells, c => {
    c.addEventListener("click", (cellEv) => {
      changeColour(cellEv.target)
    })
  });

  // add listener for keyboard
  document.addEventListener("keydown", (e) => {
    editGuess(e.key)
  })

  function editGuess(key) {
    if (current_row < 6) {
      if (key.length === 1 && (/[a-zA-Z]/).test(key) && current_guess.length < 6) {
        document.getElementById('cell_' + current_row + '_' + current_guess.length).innerHTML = key.toUpperCase();
        current_guess.push(key.toLowerCase());
      } else if (key === 'Enter') {
        if (current_guess.length === 5) {
          // runResults();
          submitData();
          fillPossibleAnswers();
        }
      } else if (key === 'Backspace') {
        current_guess.pop()
        document.getElementById('cell_' + current_row + '_' + current_guess.length).innerHTML = '';
      }
    }
  }

  function changeColour(cell) {
    cell.classList.add("flip");
    if (current_row < 6) {
      let colour_index = cell_colours.findIndex(c => c === getCellRGB(cell))
      if (colour_index === 0 || colour_index === 1) {
        cell.style.backgroundColor = cell_colours[colour_index + 1]
      } else {
        cell.style.backgroundColor = cell_colours[0];
      }
    }
    cell.classList.remove("flip");
  }

  function runResults() {
    console.log("entered guess: ", current_guess.join('').toUpperCase());
    current_guess.forEach((letter, index) => {
      if (getCellRGB(document.getElementById('cell_' + current_row + '_' + index)) === "rgb(85, 85, 85)") {
        wordle_bank = wordle_bank.filter(word => !word.includes(letter))
      }
      if (getCellRGB(document.getElementById('cell_' + current_row + '_' + index)) === "rgb(106, 170, 100)") {
        wordle_bank = wordle_bank.filter(word => word.split('')[index] === letter)
      }
      if (getCellRGB(document.getElementById('cell_' + current_row + '_' + index)) === "rgb(201, 180, 88)") {
        wordle_bank = wordle_bank.filter(word => word.includes(letter) && word.split('')[index] !== letter)
      }
    })
    current_row += 1;
    current_guess = [];
    console.log("number of possible answers: ", wordle_bank.length);
    console.log("possible answers left: ", wordle_bank);
    fillPossibleAnswers();
  }

  function getCellRGB(cell) {
    let cell_RGB = window.getComputedStyle(cell, "").getPropertyValue("background-color");
    return cell_RGB;
  }

  function submitData()  {

    let letters = [];

    for (let row = 0; row < 6; row++) {
      for (let index = 0; index < 5; index++) {
        let letter = document.getElementById('cell_' + row + '_' + index).innerHTML.toLowerCase();
        if (letter.length != 0){
          let colour = getCellRGB(document.getElementById('cell_' + row + '_' + index));

          let letterObject = {
            letter : letter,
            index : index,
            colour : colour
          };
          letters.push(letterObject);
        }
      }
    }

    current_row++;
    console.log(JSON.stringify(letters));

    const options = {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(letters)
    }

    fetch('/api', options)
    .catch(err => console.error(err));
  }

  function fillPossibleAnswers() {
    const options = {
      method: 'GET',
    }
    let words;

    fetch('/api', options)
    .then(data => words = data)

    let list = document.getElementById("possible_answers");
    list.innerHTML = '';

    let num_poss = document.getElementById("possible_answers_num");
    num_poss.innerHTML = ": " + words.length;
    if (words.length === 0) {
      let li = document.createElement("li");
      li.innerHTML = "No possible answers remain. Please refresh the page.";
      list.appendChild(li);
    } else {
      words.forEach(word => {
        let li = document.createElement("li");
        li.innerHTML = word;
        list.appendChild(li);
      })
    }
  }

  function fillPossibleAnswersOld() {
    document.getElementsByClassName("word_list")[0].classList.remove("hidden");
    let list = document.getElementById("possible_answers");
    list.innerHTML = '';
    let num_poss = document.getElementById("possible_answers_num");
    num_poss.innerHTML = ": " + original_words.length;
    if (original_words.length === 0) {
      let li = document.createElement("li");
      li.innerHTML = "No possible answers remain. Please refresh the page.";
      list.appendChild(li);
    } else {
      original_words.forEach(word => {
        let li = document.createElement("li");
        li.innerHTML = word;
        list.appendChild(li);
      })
    }
  }

};
