const socket = new WebSocket("ws://" + window.location.hostname + ":8080/chat");

const chatBox = document.getElementById("chat-box");
const messageForm = document.getElementById("message-form");
const messageInput = document.getElementById("message-input");
const recipientInput = document.getElementById("recipient-input");
const sessionDisplay = document.getElementById("session-id");

let mySessionId = "";

function appendMessage(content, sender) {
    const msg = document.createElement("div");
    msg.className = `msg ${sender}`;
    msg.textContent = content;
    chatBox.appendChild(msg);
    chatBox.scrollTop = chatBox.scrollHeight;
}

socket.onopen = function () {
    appendMessage("🟢 Connected to server", "server");
};

socket.onmessage = function (event) {
    const data = event.data;
    if (data.startsWith("Your session ID: ")) {
        mySessionId = data.replace("Your session ID: ", "").trim();
        sessionDisplay.textContent = mySessionId;
    }
    appendMessage(data, data.includes("From") ? "server" : "info");
};

socket.onerror = function (error) {
    console.error(`[❌] WebSocket error: ${error.message}`);
    appendMessage("❌ WebSocket error", "server");
};

messageForm.addEventListener("submit", function (event) {
    event.preventDefault();
    const msg = messageInput.value.trim();
    const recipient = recipientInput.value.trim();
    if (msg && recipient && socket.readyState === WebSocket.OPEN) {
        const formatted = recipient + ":" + msg;
        socket.send(formatted);
        appendMessage("You to [" + recipient + "]: " + msg, "client");
        messageInput.value = "";
    }
});
