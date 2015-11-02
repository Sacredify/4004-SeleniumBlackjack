var ws = null;

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
}

function setGameOptionsEnabled(enabled) {
    document.getElementById('stay').disabled = !enabled;
    document.getElementById('hit').disabled = !enabled;
    document.getElementById('split').disabled = !enabled;
}

function setAdmin(enabled) {
    document.getElementById('open').disabled = !enabled;
    document.getElementById('shutdown').disabled = !enabled;
    document.getElementById('numberPlayers').disabled = !enabled;
}

function enableStart(enabled) {
    document.getElementById('start').disabled = !enabled;
}

function setUID(uid) {
    if (uid != null) {
        var stripped = uid.replace(/\./g, ' ');
        document.getElementById('consoleText').innerHTML = 'Console (UID: ' + stripped + ')';
    } else {
        document.getElementById('consoleText').innerHTML = 'Console';
    }
}
/**
 * Connect to the server.
 */
function connect() {
    // hardcoded endpoint, oh no!
    ws = new SockJS('/game');
    ws.onopen = function () {
        setConnected(true);
        clientLog('WebSocket connection opened.');
    };
    ws.onmessage = function (event) {
        dispatch(event.data);
    };
    ws.onclose = function () {
        setConnected(false);
        setUID();
        clientLog('WebSocket connection closed.');
    };
}

/**
 * Disconnect from the server.
 */
function disconnect() {
    if (ws != null) {
        ws.close();
        ws = null;
    }
    setConnected(false);
    setAdmin(false);
    enableStart(false);
    removeCards();
}

/**
 * Echo a message.
 */
function echo() {
    if (ws != null) {
        var message = document.getElementById('message').value;
        log('Sent: ' + message);
        ws.send(message);
    } else {
        alert('WebSocket connection not established, please connect.');
    }
}

/**
 * Determine what to do with the message.
 *
 * @param message the message.
 */
function dispatch(message) {
    // split message into three: [SENDER, KEY, PAYLOAD]
    var split = message.split('|');
    var logMessage = split[0].concat(split[2]);
    console.log(split);
    switch (split[1]) {
        case 'OTHER+DISCONNECTED':
        case 'OTHER+READY+TO+START':
        case 'OTHER+CONNECTED':
        case 'CONNECTED':
            log(logMessage);
            var connectedMessage = logMessage.split(' ');
            var last = connectedMessage[connectedMessage.length - 1];
            if (split[1] === 'CONNECTED') {
                setUID(last);
            }
            break;
        case 'NOT+ACCEPTING':
            log(logMessage);
            //disconnect(); for now done by the server...this is the work around
            break;
        case 'ADMIN':
            log(logMessage);
            setAdmin(true);
            enableStart(false);
            break;
        case 'ADD+CARD':
            addCard(split[2]);
            break;
        case 'READY+TO+START':
            log(logMessage);
            enableStart(true);
            break;
        default:
            console.log('Unknown message received');
            break;
    }
}

/**
 * Add a new card to that shit.
 */
function addCard(card) {
    var li = document.createElement('li');
    li.innerHTML = card;
    document.getElementById('hand').appendChild(li);
}

/**
 * Remove all cards.
 */
function removeCards() {
    console.log('Emptied cards.');
    document.getElementById('hand').innerHTML = "";
}

/**
 * Open connections for other players.
 */
function acceptOthers() {
    if (ws != null) {
        var numP = document.getElementById('numberPlayers').value;

        clientLog('Opening the lobby with specified settings. When the correct number of players have connected, the start button will become available.');
        var send = 'ACCEPT|' + numP;
        ws.send(send);
        document.getElementById('open').disabled = true;
        document.getElementById('numberPlayers').disabled = true;
    } else {
        alert('WebSocket connection not established, please connect.');
    }
}

/**
 * Log from the client.
 * @param message the message.
 */
function clientLog(message) {
    var pad = '00';
    var date = new Date();
    var hour = "" + date.getHours();
    var hourPad = pad.substring(0, pad.length - hour.length) + hour;
    var min = "" + date.getMinutes();
    var minPad = pad.substring(0, pad.length - min.length) + min;
    var hourMin = hourPad + ':' + minPad;
    var prefix = '<strong>' + hourMin + ' Client' + '</strong>: ';
    log(prefix + message);
}

/**
 * Log to the console
 * @param message the message.
 */
function log(message) {
    var console = document.getElementById('console');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.innerHTML = message;

    console.appendChild(p);
    while (console.childNodes.length > 25) {
        console.removeChild(console.firstChild);
    }
    console.scrollTop = console.scrollHeight;
}

/**
 * Shutdown the spring boot server.
 */
function shutdown() {
    clientLog('Sent shutdown command.');
    $.post(
        "http://localhost:8080/shutdown",
        null,
        function (data) {
            alert("Website shutting down.");
        }
    );
}