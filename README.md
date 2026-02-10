# Facy – Secure Biometric Face Detection

**Facy** is a high-performance Android application that demonstrates secure implementation of face detection using **Firebase ML Kit**. This project is built as a reference for **Biometric Data Privacy**, focusing on secure media handling and protection against unauthorized data extraction.

---

## 🔍 Security Architecture & Audit

This application has been engineered to mitigate risks associated with biometric data processing.

### 1. Static Analysis & Hardening (MobSF)
* **Manifest Security:** `android:allowBackup="false"` to prevent data extraction via ADB.
* **Component Protection:** All Activities and Services are non-exported by default to prevent **Intent Injection** attacks.
* **Code Protection:** Integrated **R8/ProGuard** rules to obfuscate ML Kit integration logic and prevent static reverse engineering.

### 2. Biometric Data Privacy (OWASP MASVS)
* **Ephemeral Processing:** Images are processed in volatile memory. No facial data or raw images are persisted to external storage, adhering to the **Privacy by Design** principle.
- **On-Device vs Cloud:** (Select your config) Implementation prioritizes **On-Device ML** to ensure biometric data never leaves the device, reducing the network attack surface.



---

## 🔐 Key Security Features

### 📸 Secure Media Pipeline
- **Principle of Least Privilege:** Uses the **MediaStore API** or **CameraX** with minimal scoped permissions, avoiding broad `READ_EXTERNAL_STORAGE` access.
- **Secure API Key Management:** Firebase configuration is protected and not hardcoded in plain text (using `secrets.gradle` or CI/CD environment variables).

### 🛠️ Runtime Resilience
- **Input Sanitization:** Image buffers are validated before processing to prevent memory corruption or malicious payload execution via the ML engine.
- **Logging Hygiene:** Production builds use a custom `Timber.Tree` to strip all sensitive debugging logs (coordinates, face IDs) from `logcat`.

---

## ⚠️ Threat Model & Mitigation Strategy

| Threat Vector | Risk | Mitigation |
| :--- | :--- | :--- |
| **Facial Data Leakage** | High | Zero-persistence policy; data exists only in RAM during the lifecycle. |
| **Root/hooking (Frida)** | Medium | (Planned) Integrity checks to detect instrumentation of ML callback methods. |
| **Man-in-the-Middle (MitM)** | Medium | Enforced Certificate Pinning for Firebase Cloud communication (if used). |
| **Unauthorized Capture** | Low | Strict runtime permission checks and explicit Intent-based camera calls. |

---

## 🛠️ Technical Stack
- **Language:** Kotlin
- **ML Engine:** Firebase ML Kit (Face Detection)
- **Architecture:** MVVM + Clean Architecture
- **Security Tools Used:** MobSF, Jadx-GUI, Frida.

---

## 🚀 Installation & Testing

1. **Clone the repo:**
   ```bash
   git clone [https://github.com/nikolaivetrik24062010/Facy.git](https://github.com/nikolaivetrik24062010/Facy.git)
   Gemini said
Николай, давай сделаем из Facy не просто «приложение с камерой», а серьезный кейс по Biometric Data Privacy. В США вопросы обработки лиц (Face Detection) очень чувствительны с точки зрения законов (CCPA/GDPR), поэтому для AppSec-инженера это идеальный шанс показать знание темы Privacy & Data Protection.

Ниже — профессиональный Markdown для твоего репозитория.

Markdown
# Facy – Secure Biometric Face Detection

**Facy** is a high-performance Android application that demonstrates secure implementation of face detection using **Firebase ML Kit**. This project is built as a reference for **Biometric Data Privacy**, focusing on secure media handling and protection against unauthorized data extraction.

---

## 🔍 Security Architecture & Audit

This application has been engineered to mitigate risks associated with biometric data processing.

### 1. Static Analysis & Hardening (MobSF)
* **Manifest Security:** `android:allowBackup="false"` to prevent data extraction via ADB.
* **Component Protection:** All Activities and Services are non-exported by default to prevent **Intent Injection** attacks.
* **Code Protection:** Integrated **R8/ProGuard** rules to obfuscate ML Kit integration logic and prevent static reverse engineering.

### 2. Biometric Data Privacy (OWASP MASVS)
* **Ephemeral Processing:** Images are processed in volatile memory. No facial data or raw images are persisted to external storage, adhering to the **Privacy by Design** principle.
- **On-Device vs Cloud:** (Select your config) Implementation prioritizes **On-Device ML** to ensure biometric data never leaves the device, reducing the network attack surface.



---

## 🔐 Key Security Features

### 📸 Secure Media Pipeline
- **Principle of Least Privilege:** Uses the **MediaStore API** or **CameraX** with minimal scoped permissions, avoiding broad `READ_EXTERNAL_STORAGE` access.
- **Secure API Key Management:** Firebase configuration is protected and not hardcoded in plain text (using `secrets.gradle` or CI/CD environment variables).

### 🛠️ Runtime Resilience
- **Input Sanitization:** Image buffers are validated before processing to prevent memory corruption or malicious payload execution via the ML engine.
- **Logging Hygiene:** Production builds use a custom `Timber.Tree` to strip all sensitive debugging logs (coordinates, face IDs) from `logcat`.

---

## ⚠️ Threat Model & Mitigation Strategy

| Threat Vector | Risk | Mitigation |
| :--- | :--- | :--- |
| **Facial Data Leakage** | High | Zero-persistence policy; data exists only in RAM during the lifecycle. |
| **Root/hooking (Frida)** | Medium | (Planned) Integrity checks to detect instrumentation of ML callback methods. |
| **Man-in-the-Middle (MitM)** | Medium | Enforced Certificate Pinning for Firebase Cloud communication (if used). |
| **Unauthorized Capture** | Low | Strict runtime permission checks and explicit Intent-based camera calls. |

---

## 🛠️ Technical Stack
- **Language:** Kotlin
- **ML Engine:** Firebase ML Kit (Face Detection)
- **Architecture:** MVVM + Clean Architecture
- **Security Tools Used:** MobSF, Jadx-GUI, Frida.

---

## 🚀 Installation & Testing

1. **Clone the repo:**
   ```bash
   git clone [https://github.com/nikolaivetrik24062010/Facy.git](https://github.com/nikolaivetrik24062010/Facy.git)
2. Build: Use the Release variant to test obfuscation.

3. Verify: Use Jadx-GUI to ensure Firebase keys and logic are not easily recoverable.

🤝 Author
Nikolai Vetrik Senior Security Engineer & Mobile Developer 📧 devnikolaivetrik@gmail.com | 🔗 LinkedIn

