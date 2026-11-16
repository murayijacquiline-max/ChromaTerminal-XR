# ChromaTerminal XR & A.E.G.I.S. 🚀

A next-generation spatial workspace, developer shell, and interactive 3D holographic AI companion built natively for **Android XR** (`androidx.xr.compose:1.0.0-beta02`). **ChromaTerminal XR** bridges high-velocity developer workflows with room-anchored spatial computing and multimodal AI.

<img width="721" height="441" alt="ChromaTerminal XR Spatial Interface" src="https://github.com/user-attachments/assets/49fc3985-23d9-4ee0-9cbe-44344fd65484"/>

[📄 View Architecture & Pitch Deck (PDF)](./Building%20Android%20XR%20Apps.pdf)

---

## 📹 Video Demo & Community Discussions

Watch live spatial demonstrations running in the **Android Studio XR Emulator** and join the discussion:

* 📱 **X (Twitter):** [View Launch Thread & Demo](https://x.com/KigananeTrippie)
* 💼 **LinkedIn:** [View Product Architecture Post](https://www.linkedin.com/in/jacquiline-murayi-94438532a)
* 🤖 **Reddit (`r/androiddev`):** [Read Technical Breakdown & Community Feedback](https://www.reddit.com/r/androiddev/comments/1xwcrs3/built_a_native_android_xr_spatial_terminal_with/)

---

## ✨ What's New & Core Architecture

* **A.E.G.I.S. Spatial AI Companion**: A life-sized, room-anchored 3D holographic assistant (`anime_companion.glb`) powered by **Gemini 1.5 Flash**, designed for natural language task routing, voice-assisted workflows, and contextual spatial presence.
* **Native Jetpack Compose for XR (`1.0.0-beta02`)**: Built using `Subspace`, `SpatialPanel`, and `Orbiter` containers with real-time 6DoF passthrough anchoring and Home Space runtime support (`XR_ACTIVITY_START_MODE_HOME_SPACE`).
* **Asynchronous AI Routing (`TerminalAIManager`)**: Coroutine-driven inference pipeline that parses Gemini responses into structured spatial UI tags (`<SYS>`, `<CODE>`, `<MSG>`, `<NET>`, `<OK>`, `<ERR>`).
* **Non-GPU Development Fallback**: Implements `LocalInspectionMode.current` safeguards so Android Studio Compose previews and layouts render cleanly on standard desktop hardware without dedicated GPUs.
* **Hybrid Input & Dynamic Themes**: Seamlessly switch between keyboard shell input, 3D visual `AssistChip` actions, and real-time runtime palettes (`CYAN`, `AMBER`, `MATRIX`, `VIOLET`).

---

## 🛠️ Command Reference

| Command | Arguments | Description |
| :--- | :--- | :--- |
| `help` | None | Displays active terminal commands and A.E.G.I.S. routing options |
| `ls` | None | Lists virtual files in the current spatial directory |
| `cat` | `<filename>` | Outputs contents of specified file |
| `pwd` | None | Displays current spatial working directory |
| `sysinfo` | None | Prints Android XR platform metrics, heap memory, and 6DoF anchor status |
| `theme` | `cyan` \| `amber` \| `matrix` \| `violet` | Switches interface color palette in real-time |

---

## 📦 Getting Started & Local Setup

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/murayijacquiline-max/ChromaTerminal-XR.git](https://github.com/murayijacquiline-max/ChromaTerminal-XR.git)
