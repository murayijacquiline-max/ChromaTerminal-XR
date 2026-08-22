# ChromaTerminal XR 🚀

A floating, retro-futuristic spatial terminal and interactive shell built for **Android XR**. **ChromaTerminal XR** bridges command-line productivity with spatial computing UI components, offering dynamic color palettes, a virtual file system, and hybrid GUI control chips.
[📄 View Presentation Slides (PDF)](./Building%20Android%20XR%20Apps.pdf)

---

## 📸 Key Features

* **Spatial Windowing**: Engineered for Android XR Home Space floating panel runtime (`XR_ACTIVITY_START_MODE_HOME_SPACE`).
* **Hybrid Interface**: Seamlessly switch between manual keyboard terminal input and direct 3D visual action chips (`AssistChip`).
* **Dynamic Palette Engine**: Instant runtime theme switching (`CYAN`, `AMBER`, `MATRIX`, `VIOLET`).
* **Virtual Shell Commands**: Interactive shell parsing with simulated system diagnostics and file system navigation.
* **Cyberpunk Aesthetics**: Custom ASCII banners, real-time heap load progress bars, and tokenized syntax highlighting (`<SYS>`, `<NET>`, `<OK>`, `<ERR>`).

---

## 🛠️ Command Reference

| Command | Arguments | Description |
| :--- | :--- | :--- |
| `help` | None | Displays active terminal commands and usage options |
| `ls` | None | Lists virtual files in the current spatial directory |
| `cat` | `<filename>` | Outputs contents of specified file |
| `pwd` | None | Displays current spatial working directory |
| `sysinfo` | None | Prints Android XR platform metrics, heap memory, and anchor status |
| `theme` | `cyan` \| `amber` \| `matrix` \| `violet` | Switches interface color theme in real-time |
| `clear` | None | Flushes output buffer |

---

## 🏗️ Architecture Stack

* **Language**: Kotlin 2.0+
* **UI Toolkit**: Jetpack Compose (Material 3)
* **Spatial Target**: Android XR Runtime (API 34+)
* **State Management**: Reactive Compose State (`remember`, `mutableStateOf`)
