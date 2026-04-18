export function buildSensorContext() {
  return `
Voici les données actuelles des capteurs du dashboard :

ÉLECTRICITÉ
- Puissance actuelle : 480 kW (+8% vs hier)
- Consommation semaine : 3 200 kWh
- Coût estimé : 128 €

GAZ
- Pression réseau : 5.8 bar (stable)
- Consommation journalière : 1 400 m³ (-3% vs hier)
- Débit actuel : 18.5 m³/h

TEMPÉRATURE
- Température actuelle : 19°C (+3°C vs normale)
- Zone la plus chaude : Zone B à 22°C (anomalie détectée)
- Amplitude journalière : 9°C (12°C → 21°C)

EAU
- Débit actuel : 22 L/s
- Consommation journalière : 2 100 m³ (-5% vs hier)
- Indice qualité : 98/100 (excellent)

VENT
- Vitesse moyenne : 32 km/h (+12% vs hier)
- Rafale maximale : 80 km/h (alerte rafale active)
- Direction dominante : Sud-Ouest

QUALITÉ DE L'AIR
- Indice qualité (IQA) : 42/100 (bon)
- CO2 : 415 ppm (+5 ppm vs hier)
- PM2.5 : 8 µg/m³ (en dessous du seuil)

CAPTEURS INDUSTRIELS
- Lignes actives : 3/4 (Ligne C en maintenance)
- Alertes actives : 2
- Taux de fonctionnement : 72% (objectif : 80%)
`.trim()
}
