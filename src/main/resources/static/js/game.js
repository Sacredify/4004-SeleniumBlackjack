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

function setAdminPrivelege(enabled) {
    document.getElementById('open').disabled = !enabled;
    document.getElementById('start').disabled = !enabled;
    document.getElementById('shutdown').disabled = !enabled;
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
    setAdminPrivelege(false);
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
    var split = message.split('_');
    var logMessage = split[0].concat(split[2]);
    console.log(split);
    switch (split[1]) {
        case 'OTHER+CONNECTED':
        case 'CONNECTED':
            log(logMessage);
            break;
        case 'NOT+ACCEPTING':
            log(logMessage);
            //disconnect(); for now done by the server...this is the work around
            break;
        case 'ADMIN':
            log(logMessage);
            setAdminPrivelege(true);
            break;
        default:
            console.log('Unknown message received');
            break;
    }
}

/**
 * Open connections for other players.
 */
function acceptOthers() {
    if (ws != null) {
        clientLog('Sent connections available command.');
        ws.send('ACCEPT');
        document.getElementById('open').disabled = true;
    } else {
        alert('WebSocket connection not established, please connect.');
    }
}

/**
 * Log from the client.
 * @param message the message.
 */
function clientLog(message) {
    log('<strong>Client</strong>: ' + message);
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
    log('Info: Sent shutdown command.');
    $.post(
        "http://localhost:8080/shutdown",
        null,
        function (data) {
            alert("Website shutting down.");
        }
    );
}
