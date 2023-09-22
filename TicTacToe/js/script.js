let vh = window.innerHeight * 0.01;
document.documentElement.style.setProperty('--vh', `${vh}px`);

var player1_mark;
var player2_mark;
var curr_player;
var ai_level; // not -1
var redX = "#F83157"; 
var blueO = "#0d6efd";
var menu_div = document.getElementById("menu");
var game_div = document.getElementById("game");
var cells = document.querySelectorAll("td");
var scoreboard = document.getElementById("scoreboard");   
var player1_score = document.getElementById("player1_score");     
var player2_score = document.getElementById("player2_score");
var draw_score = document.getElementById("draw_score");



var win_cases = [
    [0,1,2],
    [3,4,5],
    [6,7,8],
    [0,3,6],
    [1,4,7],
    [2,5,8],
    [0,4,8],
    [2,4,6]
];
var Board = [0,0,0,0,0,0,0,0,0];

function get_data(opponent){
    if(document.getElementById("x").checked)
    {
        player_mark = "X";
        document.getElementById("player1").className = "mark_inactive";
        document.getElementById('player1_mark').innerHTML = `${player_mark} `;
        document.getElementById('player1_mark').style.color = redX;

        opponent_mark = "O";
        document.getElementById("player2").className = "mark_inactive";
        document.getElementById('player2_mark').innerHTML = `${opponent_mark} `;
        document.getElementById('player2_mark').style.color = blueO;
    }
    else
    {
        player_mark = "O";
        document.getElementById("player1").className = "mark_inactive";
        document.getElementById('player1_mark').innerHTML = `${player_mark} `;
        document.getElementById('player1_mark').style.color = blueO;

        opponent_mark = "X";
        document.getElementById("player2").className = "mark_inactive";
        document.getElementById('player2_mark').innerHTML = `${opponent_mark} `;
        document.getElementById('player2_mark').style.color = redX;
    }      
 
    if(opponent == "ai")
    {
        var diff_list = document.querySelectorAll(".btn");
        diff_list.forEach( (tag) => {
            if(tag.classList.contains("active"))
                ai_level = tag.id;
        });
                document.getElementById("level").style.display = "block";
        document.getElementById("diff_chosen").innerHTML = (ai_level == "E") ? "Easy" : ((ai_level == "M") ? "Medium" : "Hard");
        document.getElementById("player2").innerHTML = "AI";
    }
    else
    {
        ai_level = -1;
        document.getElementById("level").style.display = "none";
        document.getElementById("player2").innerHTML = "Player 2";
    }

        var buttons = document.querySelectorAll(".btn");
        buttons.forEach((button) => {
          button.addEventListener("click", function() {
            buttons.forEach((btn) => {
              btn.classList.remove("active");
            });
            this.classList.add("active");
          });
        });
    
    
    }


function showGame(){
    menu_div.style.animationName = "slide-menu-up";
    game_div.style.animationName = "slide-game-up";
    scoreboard.style.bottom = "0";
    

}
function showMenu(){
    menu_div.style.animationName = "slide-menu-down";
    game_div.style.animationName = "slide-game-down";
    scoreboard.style.bottom = "-100px";
}


function setRandom()
{
    if( Math.round(Math.random()) == 0 )
        return player_mark;
    else
        return opponent_mark;
}

function clear()
{
    Board = [0,0,0,0,0,0,0,0,0];
    cells.forEach((cell) => {
        cell.innerHTML = "";
        cell.style.backgroundColor = "";
    });
    msg2.innerHTML = "";
    remove_event();
}

function add_event()
{
    cells.forEach((cell) => {
        cell.addEventListener('mouseover', turnmouse);
        cell.addEventListener('mouseleave', turnmouse);
        cell.addEventListener('click', turnmouse);
        cell.style.cursor = "pointer";
    });
}
function remove_event()
{
    cells.forEach((cell) => {
        cell.removeEventListener('mouseover', turnmouse);
        cell.removeEventListener('mouseleave', turnmouse);
        cell.removeEventListener('click', turnmouse);
        cell.style.cursor = "default";
    });
}

function active_player()
{
    document.getElementById("player1").className = "mark_inactive";
    document.getElementById("player2").className = "mark_inactive";
    if(player_mark == curr_player)
    {
        document.getElementById("player1").className = `mark_${curr_player}_active`;
    }
    else
    {
        document.getElementById("player2").className = `mark_${curr_player}_active`;
    }
}

function turnmouse(event)
{
    turn(event.target.id, curr_player, event.type);            
}

function easy_ai() {
    const unplayedIndices = Board
      .map((value, index) => value === 0 ? index : null)
      .filter(index => index !== null);
    const randomIndex = Math.floor(Math.random() * unplayedIndices.length);
    return unplayedIndices[randomIndex];
  }
  function startt(turn)
{
    
    setTimeout(() => {
        add_event();
        check_ai_turn();
    }, 1500);
}


function medium_ai(Boardd, player)
{
    var availSpots = [];
    for(var i=0; i<Boardd.length; i++)
    {
        if(Boardd[i] == 0)
            availSpots.push(i);
    }

    // if terminal state reaches, return with the score
    if(checkWin(Boardd, opponent_mark)) 
        return {score: -10};
    else if(checkWin(Boardd, player_mark)) 
        return {score: 10};
    else if(availSpots.length == 0) 
        return {score: 0};

    //storing score and index for each move possible from the given board state
    var moves = [];

    for (var i = 0; i < availSpots.length; i++)
    {
        //create an object for each and store the index of that spot 
        var move = {};
        move.index = availSpots[i];
        Boardd[availSpots[i]] = player; // set the empty spot to the current player

        //collect the score resulted from calling minimax on the opponent of the current player
        if (player == player2_mark){
            var result = medium_ai(Boardd, player1_mark);
            move.score = result.score;
        }
        else{
            var result = medium_ai(Boardd, player2_mark);
            move.score = result.score;
        }     
        // reset the spot to empty for the next loop itereration   
        Boardd[availSpots[i]] = 0;
        // push the object to the array
        moves.push(move);
    }

    var bestMove;

    if(  Math.random() > 0.65 )
    {
        bestMove = Math.floor( Math.random() * moves.length );
    }
    else
    {
        if(player === player1_mark)
        {
            var bestScore = 10000;
            for(var i = 0; i < moves.length; i++){
                if(moves[i].score < bestScore)
                {
                    bestScore = moves[i].score;
                    bestMove = i;
                }
            }
        }
        else 
        {
            var bestScore = -10000;
            for(var i = 0; i < moves.length; i++){
                if(moves[i].score > bestScore)
                {
                    bestScore = moves[i].score;
                    bestMove = i;
                }
            }
        }
    }
    return moves[bestMove];
}

function hard_ai(Boardd, depth, alpha, beta, player)
{
    var availSpots = [];
    for(var i=0; i<Boardd.length; i++)
    {
        if(Boardd[i] == 0)
            availSpots.push(i);
    }

    // if terminal state reaches, return with the score
    if(checkWin(Boardd, opponent_mark)) 
        return {score: -20+depth};
    else if(checkWin(Boardd, player_mark))
        return {score: 20-depth};
    else if(availSpots.length == 0)
        return {score: 0};


    if(player === opponent_mark)
    {
        var bestScore = 10000;
        var bestMove = {};
        for(var i = 0; i < availSpots.length; i++)
        {
            Boardd[availSpots[i]] = player; // set the empty spot to the current player
            
            var value = hard_ai(Boardd, depth+1, alpha, beta, player_mark);
            if(value.score < bestScore)
            {
                bestScore = value.score;
                bestMove.index = availSpots[i];
                bestMove.score = bestScore;
            }

            // reset the spot to empty for the next loop itereration
            Boardd[availSpots[i]] = 0;

            beta = Math.min(beta,bestScore);
            if(beta <= alpha)
                break;
        }
        return bestMove;
    }
    else 
    {
        var bestScore = -10000;
        var bestMove = {};
        for(var i = 0; i < availSpots.length; i++)
        {
            Boardd[availSpots[i]] = player; // set the empty spot to the current player

            var value = hard_ai(Boardd, depth+1, alpha, beta, opponent_mark);
            if(value.score > bestScore)
            {
                bestScore = value.score;
                bestMove.index = availSpots[i];
                bestMove.score = bestScore;
            }

            // reset the spot to empty for the next loop itereration
            Boardd[availSpots[i]] = 0;

            alpha = Math.max(alpha,bestScore);
            if(beta <= alpha)
                break;    
        }
        return bestMove;
    }
}


function AI_move()
{
    if(ai_level == "E")
        return easy_ai();
    else if(ai_level == "M")
        return medium_ai(Board, opponent_mark).index;
    else if(ai_level == "I")
        return hard_ai(Board, 0, -10000, 10000, opponent_mark).index;
}

function check_ai_turn()
{
    if(ai_level != -1 && curr_player == opponent_mark)
    {
        remove_event();
        setTimeout(() => {
            turn(AI_move(), curr_player, "click");
        } , Math.floor( Math.random()*1700 + 1 ) );
    }
    else
        add_event();
}

function turn(cell_index, mark, mouseevent)
{
    var cell = document.getElementById(cell_index);
    if(Board[cell_index] == 0)
    {
        if(mouseevent == 'mouseover')
        {
            cell.style.opacity = 0.5;
            cell.style.color = "gray";
            cell.innerHTML = mark;
        }
        else if(mouseevent == 'mouseleave')
        {
            cell.style.opacity = 1;
            cell.innerHTML = "";
        }
        if(mouseevent == 'click')
        {
            Board[cell_index] = mark;
            cell.style.opacity = 1;
            cell.style.color = (mark == "X")? redX : blueO;
            cell.innerHTML = mark;

            var gameWon = checkWin(Board, mark);
            if(gameWon)
            {
                gameOver(gameWon);
            }
            else if(!checkTie())
            {
                curr_player = (curr_player == 'X')? 'O' : 'X';
                active_player();
                check_ai_turn();
            }
        }
    }
}

function checkTie()
{
    for(var i=0 ; i<Board.length; i++)
    {
        if(Board[i] == 0)
            return false;
    }
    remove_event();
    document.getElementById("next_match").disabled = false;
    msg2.innerHTML = `Draw!`;


    return true;
}

function checkWin(board, player)
{
    var gameWon = null;
    var score = 0;
    var i;

    for(i = 0; i<win_cases.length; i++)
    {
        score = 0;
        for(var j = 0; j<3; j++)
        {
            if(board[win_cases[i][j]] == player)
                score++;
            else
                break;
        }
        if(score == 3)
        {
            gameWon = {index: i, player : player};
            break;
        }
    }
    return gameWon;
}

function gameOver(gameWon) 
{
    for(var i = 0; i<3; i++)
    {
        document.getElementById(win_cases[gameWon.index][i]).style.backgroundColor = "#f5f52c";
    }
    remove_event();
    document.getElementById("next_match").disabled = false;

    var name = (gameWon.player == player_mark) ? `Player 1 (${gameWon.player}) ` : ((ai_level == -1) ? `Player 2 (${gameWon.player}) ` : `AI (${gameWon.player}) `);
    msg2.innerHTML = `${name} Wins!`;



}

// "ai"/"two"
function play(opponent)
{
    document.getElementById("next_match").disabled = true;
    clear();
    get_data(opponent);

    curr_player = setRandom();
    startt(curr_player);          
    active_player();
}

function playAgain()
{
    document.getElementById("next_match").disabled = true;
    setTimeout(() => {
        if(ai_level == -1)
            play("two");
        else
            play("ai");
    }, 500)
}