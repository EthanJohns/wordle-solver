let current_guess = [];
let current_row = 0;
let cell_colors = ["#555555", "#6aaa64", "#c9b458"];
console.log("Script Loaded");

window.onload = () => {

  // add click listeners to grid cells
  let cells = document.getElementsByClassName("cell");
  [].forEach.call(cells, c => {
    c.addEventListener("click", (cellEv) => {
      changeColor(cellEv.target)
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
          runResults()
        }
      } else if (key === 'Backspace') {
        current_guess.pop()
        document.getElementById('cell_' + current_row + '_' + current_guess.length).innerHTML = '';
      }
    }
  }

  function changeColor(cell) {
    cell.classList.add("flip");
    if (current_row < 6) {
      let color_index = cell_colors.findIndex(c => c === getCellHex(cell))
      if (color_index === 0 || color_index === 1) {
        cell.style.backgroundColor = cell_colors[color_index + 1]
      } else {
        cell.style.backgroundColor = cell_colors[0]
      }
    }
    cell.classList.remove("flip");
  }

  function runResults() {
    console.log("entered guess: ", current_guess.join('').toUpperCase());
    current_guess.forEach((letter, index) => {
      if (getCellHex(document.getElementById('cell_' + current_row + '_' + index)) === '#555555') {
        wordle_bank = wordle_bank.filter(word => !word.includes(letter))
      }
      if (getCellHex(document.getElementById('cell_' + current_row + '_' + index)) === '#6aaa64') {
        wordle_bank = wordle_bank.filter(word => word.split('')[index] === letter)
      }
      if (getCellHex(document.getElementById('cell_' + current_row + '_' + index)) === '#c9b458') {
        wordle_bank = wordle_bank.filter(word => word.includes(letter) && word.split('')[index] !== letter)
      }
    })
    current_row += 1;
    current_guess = [];
    console.log("number of possible answers: ", wordle_bank.length);
    console.log("possible answers left: ", wordle_bank);
    fillPossibleAnswers();
  }

  function getCellHex(cell) {
    let cell_RGB = window.getComputedStyle(cell, "").getPropertyValue("background-color");
    return `#${cell_RGB.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/).slice(1).map(n => parseInt(n, 10).toString(16).padStart(2, '0')).join('')}`;
  }

  function fillPossibleAnswers() {
    document.getElementsByClassName("word_list")[0].classList.remove("hidden");
    let list = document.getElementById("possible_answers");
    list.innerHTML = '';
    let num_poss = document.getElementById("possible_answers_num");
    num_poss.innerHTML = ": " + wordle_bank.length;
    if (wordle_bank.length === 0) {
      let li = document.createElement("li");
      li.innerHTML = "No possible answers remain. Please refresh the page.";
      list.appendChild(li);
    } else {
      wordle_bank.forEach(word => {
        let li = document.createElement("li");
        li.innerHTML = word;
        list.appendChild(li);
      })
    }
  }

};