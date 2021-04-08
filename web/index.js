let TOTAL_BOMBS, GAME_ID;
const root = document.documentElement;
let BOARD_CONTAINER = document.querySelector(".board");

function formatQueryParams(queryParams) {
  const urlQueryParams = new URLSearchParams();

  Object.keys(queryParams).map((key) => {
    urlQueryParams.append(key, queryParams[key]);
  });

  return "?" + urlQueryParams.toString();
}

/**
 * Como não pudiamos usar a fetch API nativa, dei uma improvisada
 * encapsulei o XMLHttpRequest em uma interface mais "amigável"
 * @param { url, method, body, queryParams }
 * @returns
 */
function fetch({ url, method, body, queryParams }) {
  return new Promise(function (resolve, reject) {
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
        if (xhr.status == 200 || xhr.status == 204) {
          resolve(JSON.parse(xhr.response));
        } else {
          reject(xhr.response);
        }
      }
    };

    const urlRequest = `${url}${
      queryParams ? formatQueryParams(queryParams) : ""
    }`;

    xhr.open(method, urlRequest, true);
    xhr.setRequestHeader("Content-Type", "application/json");

    if (body) {
      xhr.send(JSON.stringify(body));
    } else {
      xhr.send();
    }
  });
}

function removeChoiceLevel() {
  document.querySelector(".choice-level-wrapper").classList.add("hidden");
}

function showChoiceLevel() {
  document.querySelector(".choice-level-wrapper").classList.remove("hidden");
}

function removeBoard() {
  document.querySelector(".board").classList.add("hidden");
}

function showBoard() {
  document.querySelector(".board").classList.remove("hidden");
}

function removeButton() {
  document.querySelector("a.button").classList.add("hidden");
}

function showButton() {
  document.querySelector("a.button").classList.remove("hidden");
}

/**
 * Adiciona e remove botão dependendo do status
 * @param {String} state
 */
function updateButton(state) {
  if (state == "WIN" || state == "LOSE") {
    showButton();
  } else {
    removeButton();
  }
}

function choiceLevel() {
  const selectValue = document.getElementById("typeGameSelect").value;

  switch (selectValue) {
    case "1":
      return {
        lines: 9,
        cols: 9,
        bombs: 10,
      };

    case "2":
      return {
        lines: 16,
        cols: 16,
        bombs: 40,
      };

    case "3":
      return {
        lines: 40,
        cols: 16,
        bombs: 99,
      };
  }
}

/**
 *
 * @param {Array<Array>} board
 */
function renderBoard(board) {
  const countCols = board[0].length;
  const countLines = board.length;

  const containerSize =
    (countCols > countLines ? window.innerWidth : window.innerHeight) - 300;

  root.style.setProperty("--total-cols", countCols);
  root.style.setProperty("--total-lines", countLines);
  root.style.setProperty("--container-size", `${containerSize}px`);
  root.style.setProperty("--cell-size", `${containerSize / countCols}px`);

  BOARD_CONTAINER = document.querySelector(".board");

  BOARD_CONTAINER.innerHTML = "";

  getCellWithCordinates(board).forEach((cell) => {
    BOARD_CONTAINER.appendChild(createSpanElement(cell));
  });

  showBoard();
}

/**
 * Retorna array com cordenadas
 * @param {Array<Array>} board
 * @returns {Array}
 */
function getCellWithCordinates(board) {
  const countLines = board[0].length;
  const cells = board.reduce((cells, col) => [...cells, ...col]);

  return cells.map((cell, index) => {
    return {
      value: cell,
      movement: {
        col: Math.trunc(index / countLines),
        line: index % countLines,
      },
    };
  });
}

/**
 *
 * @param {*} cell
 * @returns {HTMLSpanElement}
 */
function createSpanElement({ value, movement }) {
  const spanEl = document.createElement("span");
  spanEl.classList.add("cell");
  spanEl.setAttribute("type", value);
  spanEl.innerHTML = `<font face="Arial">${formatSpanIcon(value)}</font>`;

  if (value === "F" || value == "_") {
    spanEl.addEventListener("contextmenu", (e) => {
      e.preventDefault();
      switchFlag(movement);
    });
  }

  if (value === "_") {
    spanEl.addEventListener("click", (e) => {
      e.preventDefault();
      move(movement);
    });
  }
  return spanEl;
}

/**
 * Retorna icone pro span
 * @param {String} value
 */
function formatSpanIcon(value) {
  switch (value) {
    case "_":
    case "0":
      return "";

    case "*":
      return "💣";

    case "F":
      return "🚩";

    default:
      return value;
  }
}

/**
 * Formata estado do jogo
 * @param {String} value
 * @returns
 */
function formatStateGame(value) {
  switch (value) {
    case "LOSE":
      return "Você foi explodido 💥";

    case "WIN":
      return "Você Ganhou 🏆";

    case "INGAME":
    default:
      return "";
  }
}

/**
 * Atualiza contador de bandeiras
 * @param {Number} countFlags
 */
function updateFlags(countFlags) {
  const title = document.getElementById("title");
  title.innerText = `Campo Minado - 💣${countFlags}`;
}

async function newGame() {
  const { board, id, totalBombs } = await fetch({
    url: "./api/new-game",
    method: "GET",
    queryParams: choiceLevel(),
  });

  TOTAL_BOMBS = totalBombs;
  GAME_ID = id;

  updateFlags(totalBombs);
  removeChoiceLevel();
  renderBoard(board);
}

async function switchFlag(movement) {
  const { board, countFlags, status: state } = await fetch({
    url: `./api/switchflag/${GAME_ID}`,
    method: "PUT",
    body: movement,
  });

  updateStateGame({ state, board, countFlags });
}

async function move(movement) {
  const { board, status: state, countFlags } = await fetch({
    url: `./api/movement/${GAME_ID}`,
    method: "POST",
    body: movement,
  });

  updateStateGame({ state, board, countFlags });
}

/**
 * Atualiza contador de bandeiras
 * @param {String} state
 */
function updateStateGame({ state, board, countFlags }) {
  const stateMessage = document.querySelector("h2.status");
  stateMessage.innerText = formatStateGame(state);
  updateButton(state);
  updateFlags(countFlags);
  renderBoard(board);
}
