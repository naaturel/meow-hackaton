<template>
  <!--
    ============================================================
    ReportCustomizer.vue
    Modale de personnalisation du rapport PDF.

    Ce composant est "dumb" (sans logique métier) : il reçoit
    les options via v-model et émet un événement quand le client
    confirme. Toute la génération PDF reste dans Temperature.vue.
    ============================================================
  -->
  <Teleport to="body">
    <!--
      On utilise <Teleport to="body"> pour monter la modale
      directement sur le <body>, hors du flux du composant parent.
      Ça évite les problèmes de z-index et d'overflow: hidden.
    -->
    <div v-if="modelValue" class="overlay" @click.self="$emit('update:modelValue', false)">
      <!--
        @click.self : ferme la modale uniquement si on clique
        sur le fond sombre, pas sur la modale elle-même.
      -->
      <div class="modal" role="dialog" aria-modal="true" aria-labelledby="modal-title">

        <!-- En-tête de la modale -->
        <div class="modal-header">
          <h2 id="modal-title" class="modal-title"> Personnaliser le rapport</h2>
          <button class="close-btn" @click="$emit('update:modelValue', false)" aria-label="Fermer">✕</button>
        </div>

        <div class="modal-body">

          <!-- ── SECTION 1 : Identité du client ── -->
          <fieldset class="field-group">
            <legend class="group-legend">👤 Identité du client</legend>

            <label class="field-label">
              Nom du client
              <input
                class="field-input"
                type="text"
                placeholder="ex : Acme Corporation"
                :value="options.clientName"
                @input="update('clientName', $event.target.value)"
              />
            </label>

            <label class="field-label">
              Sous-titre / description
              <input
                class="field-input"
                type="text"
                placeholder="ex : Site de production — Hall B"
                :value="options.clientSubtitle"
                @input="update('clientSubtitle', $event.target.value)"
              />
            </label>

            <!--
              Logo : on lit le fichier image uploadé et on le
              convertit en base64 pour pouvoir l'injecter dans
              le PDF (html2canvas ne charge pas les blob URLs).
            -->
            <label class="field-label">
              Logo (PNG, JPG — max 200 Ko)
              <div class="logo-upload-row">
                <input
                  class="field-input file-input"
                  type="file"
                  accept="image/png, image/jpeg, image/svg+xml"
                  @change="onLogoUpload"
                  ref="fileInputRef"
                />
                <!-- Prévisualisation du logo si déjà chargé -->
                <div v-if="options.logoBase64" class="logo-preview">
                  <img :src="options.logoBase64" alt="Logo prévisualisé" />
                  <button class="remove-logo" @click="removeLogo" title="Supprimer le logo">✕</button>
                </div>
              </div>
            </label>
          </fieldset>

          <!-- ── SECTION 2 : Contenu du rapport ── -->
          <fieldset class="field-group">
            <legend class="group-legend"> Contenu</legend>

            <!--
              Période : détermine combien de jours de données
              sont inclus dans les graphiques et le tableau.
            -->
            <label class="field-label">
              Période analysée
              <select
                class="field-input"
                :value="options.period"
                @change="update('period', $event.target.value)"
              >
                <option value="7">7 derniers jours</option>
                <option value="14">14 derniers jours</option>
                <option value="30">30 derniers jours</option>
                <option value="90">90 derniers jours</option>
              </select>
            </label>

            <!-- Cases à cocher pour inclure/exclure des sections -->
            <div class="field-label">Sections à inclure</div>
            <div class="checkbox-group">
              <label class="checkbox-label">
                <input type="checkbox" :checked="options.showKpis" @change="update('showKpis', $event.target.checked)" />
                Indicateurs KPI
              </label>
              <label class="checkbox-label">
                <input type="checkbox" :checked="options.showCharts" @change="update('showCharts', $event.target.checked)" />
                Graphiques
              </label>
              <label class="checkbox-label">
                <input type="checkbox" :checked="options.showTable" @change="update('showTable', $event.target.checked)" />
                Tableau récapitulatif
              </label>
              <label class="checkbox-label">
                <input type="checkbox" :checked="options.showNotes" @change="update('showNotes', $event.target.checked)" />
                Notes / commentaires
              </label>
            </div>
          </fieldset>

          <!-- ── SECTION 3 : Notes libres ── -->
          <fieldset class="field-group" v-if="options.showNotes">
            <legend class="group-legend"> Notes pour le client</legend>
            <label class="field-label">
              Commentaire libre (apparaît en bas du rapport)
              <textarea
                class="field-input field-textarea"
                placeholder="ex : Période de canicule exceptionnelle — prévoir une révision du système de climatisation."
                :value="options.notes"
                @input="update('notes', $event.target.value)"
                rows="3"
              ></textarea>
            </label>
          </fieldset>

          <!-- ── SECTION 4 : Apparence ── -->
          <fieldset class="field-group">
            <legend class="group-legend"> Apparence</legend>
            <label class="field-label">
              Couleur d'accentuation
              <!--
                Ce champ color permet de choisir la couleur
                principale du PDF (bordures, titres, etc.)
              -->
              <div class="color-row">
                <input
                  type="color"
                  class="color-picker"
                  :value="options.accentColor"
                  @input="update('accentColor', $event.target.value)"
                />
                <span class="color-hex">{{ options.accentColor }}</span>
                <!-- Bouton reset vers la couleur par défaut rouge -->
                <button class="reset-color" @click="update('accentColor', '#dc2626')">Réinitialiser</button>
              </div>
            </label>
          </fieldset>

        </div>

        <!-- Pied de modale : actions -->
        <div class="modal-footer">
          <button class="btn-cancel" @click="$emit('update:modelValue', false)">Annuler</button>
          <!--
            Émet l'événement 'confirm' que Temperature.vue
            écoute pour lancer la génération du PDF.
          -->
          <button class="btn-confirm" @click="$emit('confirm')">
             Générer le PDF
          </button>
        </div>

      </div>
    </div>
  </Teleport>
</template>

<script setup>
/**
 * Props & emits
 *
 * modelValue (Boolean) — contrôle l'affichage de la modale (v-model)
 * options (Object)     — objet réactif des réglages, partagé avec le parent
 *
 * Emits :
 * update:modelValue    — ferme/ouvre la modale (v-model)
 * confirm              — déclenche la génération PDF dans le parent
 */
const props = defineProps({
  modelValue: Boolean, // true = modale visible
  options: {
    type: Object,
    required: true,
    /**
     * Structure attendue de `options` :
     * {
     *   clientName    : String,  — nom affiché dans l'en-tête du PDF
     *   clientSubtitle: String,  — ligne secondaire sous le nom
     *   logoBase64    : String,  — image encodée en base64 (ou '')
     *   period        : String,  — '7' | '14' | '30' | '90'
     *   showKpis      : Boolean,
     *   showCharts    : Boolean,
     *   showTable     : Boolean,
     *   showNotes     : Boolean,
     *   notes         : String,
     *   accentColor   : String,  — couleur hex ex: '#dc2626'
     * }
     */
  }
})

const emit = defineEmits(['update:modelValue', 'confirm'])

// Ref pour reset l'input file si on supprime le logo
import { ref } from 'vue'
const fileInputRef = ref(null)

/**
 * update(key, value)
 * Met à jour une clé de l'objet options sans le remplacer entièrement.
 * On utilise une copie partielle pour garder la réactivité Vue.
 */
function update(key, value) {
  emit('update:options', { ...props.options, [key]: value })
}

/**
 * onLogoUpload(event)
 * Lit le fichier image sélectionné et le convertit en base64.
 * Nécessaire car html2canvas ne peut pas charger les blob:// URLs.
 */
function onLogoUpload(event) {
  const file = event.target.files[0]
  if (!file) return

  // Limite de taille indicative (200 Ko)
  if (file.size > 200_000) {
    alert('Le fichier est trop lourd (max 200 Ko). Compresse l\'image et réessaie.')
    return
  }

  const reader = new FileReader()
  // Une fois le fichier lu, on stocke le résultat base64
  reader.onload = (e) => update('logoBase64', e.target.result)
  reader.readAsDataURL(file) // Encode en "data:image/png;base64,..."
}

/**
 * removeLogo()
 * Supprime le logo et remet l'input file à zéro.
 */
function removeLogo() {
  update('logoBase64', '')
  if (fileInputRef.value) fileInputRef.value.value = ''
}
</script>

<style scoped>
/* ── Fond sombre derrière la modale ── */
.overlay {
  position: fixed;
  inset: 0; /* top/right/bottom/left: 0 */
  background: rgba(0, 0, 0, 0.65);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  padding: 20px;
  backdrop-filter: blur(3px);
}

/* ── Boîte modale ── */
.modal {
  background: #1e2130;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 14px;
  width: 100%;
  max-width: 520px;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 24px 60px rgba(0, 0, 0, 0.5);
}

/* ── En-tête ── */
.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.07);
}
.modal-title {
  margin: 0;
  font-size: 1.05rem;
  font-weight: 700;
  color: #f1f5f9;
}
.close-btn {
  background: none;
  border: none;
  color: #94a3b8;
  font-size: 1.1rem;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background 0.15s;
}
.close-btn:hover { background: rgba(255,255,255,0.08); color: #f1f5f9; }

/* ── Corps scrollable ── */
.modal-body {
  overflow-y: auto;
  padding: 20px 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* ── Groupes de champs (fieldset sans style natif) ── */
.field-group {
  border: 1px solid rgba(255, 255, 255, 0.07);
  border-radius: 10px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  margin: 0;
}
.group-legend {
  font-size: 0.78rem;
  font-weight: 700;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  padding: 0 6px;
}

/* ── Labels et inputs ── */
.field-label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 0.82rem;
  color: #cbd5e1;
  font-weight: 500;
}
.field-input {
  background: #13131f;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 7px;
  color: #f1f5f9;
  font-size: 0.85rem;
  padding: 9px 12px;
  outline: none;
  transition: border-color 0.15s;
  width: 100%;
  box-sizing: border-box;
}
.field-input:focus { border-color: #ef4444; }
.field-textarea { resize: vertical; min-height: 70px; font-family: inherit; }

/* ── Upload logo ── */
.logo-upload-row { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }
.file-input { cursor: pointer; padding: 7px 10px; }
.logo-preview {
  position: relative;
  display: inline-flex;
}
.logo-preview img {
  height: 40px;
  max-width: 120px;
  object-fit: contain;
  border-radius: 4px;
  border: 1px solid rgba(255,255,255,0.1);
  background: white;
  padding: 3px;
}
.remove-logo {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #dc2626;
  border: none;
  color: white;
  border-radius: 50%;
  width: 18px;
  height: 18px;
  font-size: 0.6rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* ── Checkboxes ── */
.checkbox-group { display: flex; flex-direction: column; gap: 8px; }
.checkbox-label {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 0.83rem;
  color: #cbd5e1;
  cursor: pointer;
}
.checkbox-label input[type="checkbox"] {
  width: 15px;
  height: 15px;
  accent-color: #ef4444; /* Colorise la checkbox en rouge */
  cursor: pointer;
}

/* ── Couleur accentuation ── */
.color-row { display: flex; align-items: center; gap: 10px; }
.color-picker {
  width: 40px;
  height: 36px;
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 6px;
  padding: 2px;
  background: #13131f;
  cursor: pointer;
}
.color-hex { font-size: 0.8rem; color: #94a3b8; font-family: monospace; }
.reset-color {
  background: none;
  border: 1px solid rgba(255,255,255,0.1);
  color: #94a3b8;
  border-radius: 6px;
  padding: 4px 10px;
  font-size: 0.75rem;
  cursor: pointer;
  transition: background 0.15s;
}
.reset-color:hover { background: rgba(255,255,255,0.06); }

/* ── Pied de modale ── */
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 16px 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.07);
}
.btn-cancel {
  background: none;
  border: 1px solid rgba(255,255,255,0.12);
  color: #94a3b8;
  border-radius: 8px;
  padding: 10px 18px;
  font-size: 0.85rem;
  cursor: pointer;
  transition: background 0.15s;
}
.btn-cancel:hover { background: rgba(255,255,255,0.05); }
.btn-confirm {
  background: #dc2626;
  border: none;
  color: white;
  border-radius: 8px;
  padding: 10px 20px;
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
}
.btn-confirm:hover { background: #b91c1c; }
</style>