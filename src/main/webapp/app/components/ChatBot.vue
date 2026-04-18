<template>
  <div class="chatbot">
    <button class="chat-toggle" @click="isOpen = !isOpen" :class="{ open: isOpen }">
      <span v-if="!isOpen" class="icon">💬</span>
      <span v-else class="icon">✕</span>
    </button>

    <Transition name="chat-slide">
      <div v-if="isOpen" class="chat-panel">
        <div class="chat-header">
          <span class="chat-live-dot"></span>
          <span class="chat-title">Assistant IA</span>
          <span class="chat-subtitle">Dashboard IoT</span>
        </div>

        <div class="chat-messages" ref="messagesEl">
          <div v-if="messages.length === 0" class="chat-empty">
            <p>Posez-moi une question sur vos données de capteurs.</p>
          </div>
          <div
            v-for="(msg, i) in messages"
            :key="i"
            class="msg"
            :class="msg.role"
          >
            <div class="msg-bubble">{{ msg.content }}</div>
          </div>
          <div v-if="loading" class="msg assistant">
            <div class="msg-bubble typing">
              <span></span><span></span><span></span>
            </div>
          </div>
        </div>

        <div class="chat-input">
          <input
            v-model="input"
            @keydown.enter="send"
            placeholder="Votre question…"
            :disabled="loading"
            ref="inputEl"
          />
          <button @click="send" :disabled="loading || !input.trim()">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/>
            </svg>
          </button>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { buildSensorContext } from '../data/sensorContext.js'

const isOpen = ref(false)
const input = ref('')
const loading = ref(false)
const messages = ref([])
const messagesEl = ref(null)
const inputEl = ref(null)

async function send() {
  const content = input.value.trim()
  if (!content || loading.value) return

  messages.value.push({ role: 'user', content })
  input.value = ''
  loading.value = true
  await scrollToBottom()

  try {
    const res = await fetch('/api/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ messages: messages.value, context: buildSensorContext() }),
    })
    const text = await res.text()
    messages.value.push({ role: 'assistant', content: text })
  } catch {
    messages.value.push({ role: 'assistant', content: 'Erreur de connexion au serveur.' })
  } finally {
    loading.value = false
    await scrollToBottom()
    inputEl.value?.focus()
  }
}

async function scrollToBottom() {
  await nextTick()
  if (messagesEl.value) messagesEl.value.scrollTop = messagesEl.value.scrollHeight
}
</script>

<style scoped>
.chatbot {
  position: fixed;
  bottom: 24px;
  right: 24px;
  z-index: 500;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 12px;
}

.chat-toggle {
  width: 52px;
  height: 52px;
  border-radius: 50%;
  background: #6366f1;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 20px rgba(99, 102, 241, 0.5);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  flex-shrink: 0;
}

.chat-toggle:hover {
  transform: scale(1.08);
  box-shadow: 0 6px 28px rgba(99, 102, 241, 0.65);
}

.icon {
  font-size: 1.2rem;
  line-height: 1;
  color: #fff;
}

.chat-panel {
  width: 340px;
  height: 480px;
  background: #0f0f1a;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 18px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
}

.chat-header {
  padding: 16px 18px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.07);
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(99, 102, 241, 0.08);
}

.chat-live-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #22c55e;
  box-shadow: 0 0 6px #22c55e;
  animation: pulse 2s ease-in-out infinite;
  flex-shrink: 0;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}

.chat-title {
  font-size: 0.9rem;
  font-weight: 700;
  color: #fff;
}

.chat-subtitle {
  font-size: 0.72rem;
  color: rgba(255, 255, 255, 0.35);
  margin-left: auto;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.chat-empty {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.chat-empty p {
  font-size: 0.82rem;
  color: rgba(255, 255, 255, 0.3);
  line-height: 1.5;
  max-width: 220px;
}

.msg {
  display: flex;
}

.msg.user { justify-content: flex-end; }
.msg.assistant { justify-content: flex-start; }

.msg-bubble {
  max-width: 80%;
  padding: 10px 14px;
  border-radius: 14px;
  font-size: 0.84rem;
  line-height: 1.5;
  white-space: pre-wrap;
}

.msg.user .msg-bubble {
  background: #6366f1;
  color: #fff;
  border-bottom-right-radius: 4px;
}

.msg.assistant .msg-bubble {
  background: rgba(255, 255, 255, 0.07);
  color: rgba(255, 255, 255, 0.88);
  border-bottom-left-radius: 4px;
}

.typing {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 12px 16px;
}

.typing span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.4);
  animation: bounce 1.2s ease-in-out infinite;
}

.typing span:nth-child(2) { animation-delay: 0.2s; }
.typing span:nth-child(3) { animation-delay: 0.4s; }

@keyframes bounce {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-6px); }
}

.chat-input {
  padding: 12px 14px;
  border-top: 1px solid rgba(255, 255, 255, 0.07);
  display: flex;
  gap: 8px;
}

.chat-input input {
  flex: 1;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  padding: 9px 14px;
  color: #fff;
  font-size: 0.84rem;
  font-family: inherit;
  outline: none;
  transition: border-color 0.15s;
}

.chat-input input:focus {
  border-color: rgba(99, 102, 241, 0.6);
}

.chat-input input::placeholder {
  color: rgba(255, 255, 255, 0.28);
}

.chat-input button {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: #6366f1;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  transition: background 0.15s, transform 0.15s;
  flex-shrink: 0;
}

.chat-input button:hover:not(:disabled) {
  background: #818cf8;
  transform: scale(1.05);
}

.chat-input button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.chat-slide-enter-active,
.chat-slide-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.chat-slide-enter-from,
.chat-slide-leave-to {
  opacity: 0;
  transform: translateY(12px) scale(0.97);
}

@media (max-width: 400px) {
  .chat-panel { width: calc(100vw - 32px); }
}
</style>
